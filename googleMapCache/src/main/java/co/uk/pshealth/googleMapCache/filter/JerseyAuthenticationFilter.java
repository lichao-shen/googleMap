package co.uk.pshealth.googleMapCache.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.AuthenticationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import co.uk.pshealth.googleMapCache.controller.MapCacheController;
import co.uk.pshealth.googleMapCache.db.DBServices;

@Provider
public class JerseyAuthenticationFilter implements ContainerRequestFilter {

	private static final String TENANT_ID = "tenantId";
	private static final String API_KEY = "apiKey";
	private static Map<String,String> tenantAPIMap = new ConcurrentHashMap<>();
	
	private final Logger slf4jLogger = LoggerFactory.getLogger(JerseyAuthenticationFilter.class);
	
	@Context
    private ResourceInfo resourceInfo;
	
	@Autowired
	DBServices dbServices;
	
	/*  filter or interceptor handler ???????  context.getHeaders();
	 * this filter authenticates incoming request by finding google api key by tenant Id. api key will be added into 
	 * http header or throw exception.
	 * @see javax.ws.rs.container.ContainerRequestFilter#filter(javax.ws.rs.container.ContainerRequestContext)
	 */
	@Override
	public void filter(ContainerRequestContext containerRequest) throws IOException {
		
		String tenantId = containerRequest.getHeaderString(TENANT_ID);
		String apiKey = this.findApiKeyById(tenantId);		
		
		if (apiKey != null) {
			containerRequest.getHeaders().add(API_KEY, apiKey);
		} else {
			// no google api key found. what if this tenant is removed ??  referesh contract
			slf4jLogger.error("authorization for tenant {} failed !!! ", tenantId);			
			throw new WebApplicationException(Status.UNAUTHORIZED);
			//throw new AuthenticationException(Status.UNAUTHORIZED);
		}		
	}	
	
	private String findApiKeyById(String tenantId) {
		
		String apiKey = tenantAPIMap.get(tenantId);
		
		if (apiKey == null) {			
			// or just JDBC ?
			apiKey = tenantAPIMap.computeIfAbsent(tenantId, k -> dbServices.findapiKeyById(tenantId));									
		} 
		
		return apiKey;
	}

}
