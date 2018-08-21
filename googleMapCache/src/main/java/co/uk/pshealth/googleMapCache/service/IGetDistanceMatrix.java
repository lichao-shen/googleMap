package co.uk.pshealth.googleMapCache.service;

import java.util.List;

import co.uk.pshealth.googleMapCache.dao.DistanceCacheDAO;
import co.uk.pshealth.googleMapCache.model.DistanceRequest;
import co.uk.pshealth.googleMapCache.model.DistanceResult;


public interface IGetDistanceMatrix {

	public DistanceResult getDistanceMatrix(String tenantId, String apiKey, DistanceRequest reqs) throws Exception;
	
}
