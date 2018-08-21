package co.uk.pshealth.googleMapCache;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.Resource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import co.uk.pshealth.googleMapCache.controller.MapCacheController;
import co.uk.pshealth.googleMapCache.exception.DistanceMatrixException;
import co.uk.pshealth.googleMapCache.model.DistanceRequest;
import co.uk.pshealth.googleMapCache.model.DistanceResult;


@RunWith(SpringRunner.class)
@SpringBootTest
@SqlGroup({
	@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),	
	@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
	//@Sql(scripts = "classpath:storedProc.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "classpath:drop.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})

public class GoogleMapCacheApplicationTests {
	
	private DistanceRequest request;
	private DistanceResult result;
	private String[] origins;
	private String[] destinations;
	private String[] destinationsTwo;
	
	
	final private static String tenantId = "1";
	final private static String apiKey = "AIzaSyAG85u75B-uXYy_exa4jIW10wFMM4MpUzA";
	
	
	@Autowired
	private JdbcTemplate template;
	
	@Autowired
	private MapCacheController googleMapController;
	
	@Before
	public void setup() {
		
		origins = new String[]{"Sn1 2ed"};
		destinations = new String[]{"AB31 5TQ","AB45 1FQ","IP1 2QA","DE65 6YB","LA12 7BT","CT1 2NF","CA7 9DD",
				"CA28 7QE"};
		destinationsTwo = new String[]{"AB31 5TQ","AB45 1FQ","IP1 2QA","DE65 6YB","LA12 7BT","CT1 2NF","CA7 9DD",
				"CA28 7QE","BN1 6SG","DE1 1UR","RG21 4FS"};
		
		
		request = new DistanceRequest();
		request.setOrigin_postcodes(origins);
		request.setDestination_postcodes(destinations);
		
		
	}
	
	
	
	/*
	@Test
	public void contextLoads() {
	} */ 
	
	
	@Test
	public void callGoogleMapMatrix() throws DistanceMatrixException {

		result = googleMapController.lookupMapDistanceMatrix(tenantId, apiKey, this.request);
		
		assertFalse(result.isOutofYearlyCredits());
		assertEquals(8, result.getDestination_addresses().size());
		assertEquals(271837, result.getRows().get(0).getElements().get(0).getDistance());		
		assertEquals(10705, result.getRows().get(0).getElements().get(0).getDuration());		
		
	} 
	

}
