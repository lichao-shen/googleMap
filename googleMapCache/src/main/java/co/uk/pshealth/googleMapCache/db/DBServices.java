package co.uk.pshealth.googleMapCache.db;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import co.uk.pshealth.googleMapCache.dao.DistanceCacheDAO;
import co.uk.pshealth.googleMapCache.dao.DistanceCacheRowMapper;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;


//@Repository
@Component
public class DBServices {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<DistanceCacheDAO> lookupDistanceCache(String origin_postcode, String destination_postcode) {
				
		return jdbcTemplate.query("select * from distanceCache where OriginPostcode=? and DestinationPostcode=?"
			   , new Object[]{origin_postcode, destination_postcode}, new DistanceCacheRowMapper());
		
	}
	
	
	public void insertDistanceMatrixCache(String origin, String destination, long travelDistanceMetres, long travelTimeSeconds) {
		
		System.out.println(" insert into distance cache ");
		
		jdbcTemplate.update("insert into distancecache( originPostcode, destinationPostcode"
				+ ", travelDistanceMetres, travelTimeSeconds, timestamp) values(?,?,?,?,?)"
				, origin, destination, travelDistanceMetres, travelTimeSeconds, new Timestamp(System.currentTimeMillis()));
		
	}
	
	public void insertTransaction(String tenantId, String contractId, int elesUsed) {
		
		jdbcTemplate.update("insert into transaction( tenantId, contractId, elementUsed, timestamp) values(?,?,?,?)"
				     ,tenantId, contractId, elesUsed, new Timestamp(System.currentTimeMillis()));
		
		System.out.println(" insert into entry into transaction table ");
		
		
	}
	
	public String findapiKeyById(String tenantId) {
			
		String apikey = null;
		
		try {			
			apikey = jdbcTemplate.queryForObject("select APIKey from Tenant where Id=?", new Object[] { tenantId }, String.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		return apikey;
	}
	
	public String findContractIdByTenantId(String tenantId) {
		
		String contractId = null;
		
		try {			
			contractId = jdbcTemplate.queryForObject("select id from tenantcontract where tenantId=? and status = 1", new Object[] { tenantId }, String.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		return contractId;
	}
	
	/*
	public int getContractIdByTenantId(String tenantId) throws EmptyResultDataAccessException{

		int contractId;
		contractId = jdbcTemplate.queryForObject("select id from tenantcontract where TenantId=?", new Object[] { tenantId }, int.class);
		
		return contractId;
		
	} */

	
	
	public boolean ifYearlyCreditUsedup(String contractId) throws EmptyResultDataAccessException {
		
		boolean usedUp = false;
				
		usedUp = jdbcTemplate.queryForObject("select outOfYearlyCredits from TenantContract where id = ? ", new Object[] { contractId }, boolean.class);
		
		return usedUp;
		
	}
	
	public boolean ifDailyCreditUsedup(String tenantId, String contractId) throws EmptyResultDataAccessException {
		
		boolean usedUp = false;
		//int contractId = this.getContractIdByTenantId(tenantId);
		try{
			usedUp = jdbcTemplate.queryForObject("select outOfDailyCredits from creditHistory where tenantId=? and tenantContractId=? and historyDate = curdate()", new Object[] { tenantId, contractId }, boolean.class);
		} catch (EmptyResultDataAccessException e) {
			return false;
		}
		return usedUp;
		
	}
	
		
	
	public int delEntriesInCache(List<Long> id) {
		
		long[] ids = id.stream().mapToLong(l -> l).toArray();
		String delStr = Arrays.toString(ids).replaceAll("\\[", "(").replaceAll("\\]", ")");
		
		return jdbcTemplate.update(" delete from distancecache where id in " + delStr);
		
	}
	
	
	
}
