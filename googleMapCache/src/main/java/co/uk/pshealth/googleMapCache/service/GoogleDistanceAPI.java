package co.uk.pshealth.googleMapCache.service;


import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.DistanceMatrixRow;

import co.uk.pshealth.googleMapCache.controller.MapCacheController;
import co.uk.pshealth.googleMapCache.dao.DistanceCacheDAO;
import co.uk.pshealth.googleMapCache.db.DBServices;
import co.uk.pshealth.googleMapCache.exception.DistanceMatrixException;
import co.uk.pshealth.googleMapCache.model.DistanceRequest;
import co.uk.pshealth.googleMapCache.model.DistanceResult;
import co.uk.pshealth.googleMapCache.model.Element;
import co.uk.pshealth.googleMapCache.model.Row;
import co.uk.pshealth.googleMapCache.util.Utils;


@Component
public class GoogleDistanceAPI implements IGetDistanceMatrix {
	
	//private static final String API_KEY = "AIzaSyAG85u75B-uXYy_exa4jIW10wFMM4MpUzA";	
	//private static final GeoApiContext context = new GeoApiContext.Builder().apiKey(API_KEY).build();
	private static final int DESTINATION_ELEMENT_MAX = 100; 
	private static final int ENTRY_EXPIRED_DAYS = 365;
	//private String apiKey;
	
	private final Logger slf4jLogger = LoggerFactory.getLogger(GoogleDistanceAPI.class);
	
	@Autowired
	DBServices dbServices;
	
	public GoogleDistanceAPI() {
		//this.apiKey = apiKey;
	}
	
	@Transactional
	@Override
	public DistanceResult getDistanceMatrix(String tenantId, String apiKey, DistanceRequest reqs) throws DistanceMatrixException {
				
		slf4jLogger.debug("  calling getDistanceMatrix ");
		
		String[] origins = reqs.getOrigin_postcodes();				
		Set<String> hashDestinations = Arrays.stream(reqs.getDestination_postcodes()).collect(Collectors.toSet());
		String[] destinations = hashDestinations.toArray(new String[hashDestinations.size()]);
		List<String> responseDestinations = new ArrayList<>(destinations.length); // response returned to client
		List<Element> eles = new ArrayList<>();
		List<Row> rows = new ArrayList<>();
		//List<DistanceCacheDAO> distanceMatrixList = new ArrayList<DistanceCacheDAO>(destinations.length);
		List<String> queryDestinations = new ArrayList<String>();
		String[][] splitDestinations = null;
		List<Long> delDistance = new ArrayList<>(); 
		boolean outofYearlyCredit = false;
		
		
		// look up cache consider only one origin post code.
		for (String origin : origins) {
			for (String destination : destinations) {				
				List<DistanceCacheDAO> dis = dbServices.lookupDistanceCache(origin, destination);
				if (dis.isEmpty()) {
					queryDestinations.add(destination);					
				} else if (dis.size() == 1) {					
					LocalDate mapTimestamp = dis.get(0).getTimestamp().toLocalDate();
					LocalDate currentDate = LocalDate.now();					
					if( ChronoUnit.DAYS.between(mapTimestamp, currentDate) <= ENTRY_EXPIRED_DAYS ) {
						// distance cache is within a year
						Element ele = new Element(dis.get(0).getTravelDistanceMetres(), dis.get(0).getTravelTimeSeconds());
						eles.add(ele);
						responseDestinations.add(destination);
					} else {
						queryDestinations.add(destination);
						delDistance.add(dis.get(0).getId());
					}					
				}				
			}
			
			slf4jLogger.debug(" tenant {} : origin {} : cache result {} ", tenantId, origin, (Object)responseDestinations);
		} 
		
		
		slf4jLogger.debug(" tenant {} : origin {} destination sent to google: {} ", tenantId, origins[0], (Object)queryDestinations);
		
		
		String contractId = dbServices.findContractIdByTenantId(tenantId);
		outofYearlyCredit = dbServices.ifYearlyCreditUsedup(contractId);
		
		// query google map service and insert into cache and credit history. google lookup restriction is 25 * 25				
		if (queryDestinations.size() > 0 && !this.ifCreditUsedUp(tenantId, contractId, outofYearlyCredit)) {
			// delete outdated entries in cache
			if (delDistance.size() > 0) {
				slf4jLogger.debug(" tenant {} : delete obsolete entries {} ", tenantId, delDistance);
				dbServices.delEntriesInCache(delDistance);
			}		
			
			try {
				String[] queryDestinationsArray = queryDestinations.toArray(new String[queryDestinations.size()]);
				GeoApiContext context = new GeoApiContext.Builder().apiKey(apiKey).build();
				if (queryDestinationsArray.length <= DESTINATION_ELEMENT_MAX) {
					DistanceMatrix googleDistanceMatrix = DistanceMatrixApi.getDistanceMatrix(context, origins, queryDestinationsArray).await();				
					this.processDistanceMatrix(tenantId, contractId, origins[0], queryDestinationsArray, googleDistanceMatrix, eles);					
				} else {
					splitDestinations = Utils.splitStringArray(queryDestinationsArray, DESTINATION_ELEMENT_MAX);
					for (String[] temp : splitDestinations) {
						// GeocodingApi.newRequest(geoApiContext).address(newRequestString)
						DistanceMatrix googleDistanceMatrix = DistanceMatrixApi.getDistanceMatrix(context, origins, temp).await();				
						this.processDistanceMatrix(tenantId, contractId, origins[0], temp, googleDistanceMatrix, eles);	
					}
				}
			} catch (Exception e) {
				throw new DistanceMatrixException(e.getMessage());
				
			}
			//add queryDestinations at the end of responseDestinations 
			responseDestinations.addAll(queryDestinations);
			
		}
		
		
		DistanceResult response = new DistanceResult();
		response.setOrigin_addresses(origins);
		response.setDestination_addresses(responseDestinations);
		Row responseRow = new Row();
		responseRow.setElements(eles);
		rows.add(responseRow);		
		response.setRows(rows);
		response.setOutofYearlyCredits(outofYearlyCredit);
		
		slf4jLogger.debug(" tenant {} : origin {} destination {} look up finished. ", tenantId, (Object)response.getOrigin_addresses(), (Object)response.getDestination_addresses());
		
		return response;
	}
	
		
	private void processDistanceMatrix(String tenantId, String contractId, String originPostcode, String[] queryDestinationsArray, DistanceMatrix googleDistanceMatrix, List<Element> eles) {
		
		slf4jLogger.debug(" Look up google origin {} : destination {} ", originPostcode, (Object)queryDestinationsArray);
		
		int counter = 0;
		for (DistanceMatrixRow row : googleDistanceMatrix.rows) {			
			for (DistanceMatrixElement e : row.elements) {				
				Element ele = null;
				long meters;
				long seconds;
				if (e.status == e.status.OK ) {
					meters = e.distance.inMeters;
					seconds = e.duration.inSeconds;
				} else {
					meters = -1;
					seconds = -1;
				}					
				ele = new Element(meters, seconds);
				eles.add(ele);
				slf4jLogger.debug("Insert into Cache origin {} : destination {} : meter {} : seconds {} ", originPostcode, queryDestinationsArray[counter], meters, seconds);
				dbServices.insertDistanceMatrixCache(originPostcode, queryDestinationsArray[counter++], meters, seconds);				
			}
			
	    }
		
		int elesUsed = queryDestinationsArray.length;
		dbServices.insertTransaction(tenantId, contractId, elesUsed);
		
		slf4jLogger.debug(" tenant {} : Google element credit used {}", tenantId, elesUsed);
	}
	
	
	private boolean ifCreditUsedUp(String tenantId, String contractId, boolean outofYearlyCredit) {
		
		if (outofYearlyCredit) {
			if (dbServices.ifDailyCreditUsedup(tenantId, contractId)) {				 
				 slf4jLogger.info("  tenant {} : contract {} has reached yearly and daily credit limit ", tenantId, contractId);
				 return true;
			 } else {
				 slf4jLogger.info("  tenant {} : contract {} has reached yearly but not daily credit limit ", tenantId, contractId);
				 return false;
			 }			
		} else {			
			return false;
		}
		
	}
	
	
	
}
