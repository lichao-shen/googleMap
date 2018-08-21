package co.uk.pshealth.googleMapCache.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.uk.pshealth.googleMapCache.db.DBServices;
import co.uk.pshealth.googleMapCache.entity.DistanceMapRepository;
import co.uk.pshealth.googleMapCache.entity.Test;
import co.uk.pshealth.googleMapCache.exception.DistanceMatrixException;
import co.uk.pshealth.googleMapCache.model.DistanceRequest;
import co.uk.pshealth.googleMapCache.model.DistanceResult;
import co.uk.pshealth.googleMapCache.service.GoogleDistanceAPI;
//import co.uk.pshealth.googleMapCache.service.IGetDistanceMatrix;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
@Path("/distance/")
public class MapCacheController {

	private static final String TENANT_ID = "tenantId";
	private static final String API_KEY = "apiKey";
	
	private final Logger slf4jLogger = LoggerFactory.getLogger(MapCacheController.class);
	
	@Autowired
	//IGetDistanceMatrix distanceMatrix;
	GoogleDistanceAPI distanceMatrix;
	
	/*
	@Autowired
	DistanceMapRepository  disCacheTest;
	
	@Context
 	private HttpHeaders headers;  requestContext
 
	@Context
	private UriInfo uriInfo; */
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public DistanceResult lookupMapDistanceMatrix(@HeaderParam(TENANT_ID) String tenantId, @HeaderParam(API_KEY) String apiKey, DistanceRequest req) throws DistanceMatrixException {		
				
		slf4jLogger.debug("the tenantId {} and  api key is {}", tenantId, apiKey);
		
		
		//Test test = new Test("testName");
		//disCacheTest.save(test);
		
		DistanceResult distanceResult = distanceMatrix.getDistanceMatrix(tenantId, apiKey, req);
		
		return distanceResult;
				
	}	
	
}
