package co.uk.pshealth.googleMapCache.model;

import javax.ws.rs.ext.Provider;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.stereotype.Component;

@Provider 
@XmlRootElement
public class DistanceRequest {

	//@XmlElement(name="tenantId")
	//private String tenantId;
	
	@XmlElement(name="origin_postcodes")
	private String[] origin_postcodes;
	
	@XmlElement(name="destination_postcodes")
	private String[] destination_postcodes;
	
	public DistanceRequest() {}
	
	/*
	public String getTenantId() {
		return tenantId;
	}
	
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	} */
	
	public String[] getOrigin_postcodes() {
		return origin_postcodes;
	}
	
	public void setOrigin_postcodes(String[] origin_postcodes) {
		this.origin_postcodes = origin_postcodes;
	}
	
	public String[] getDestination_postcodes() {
		return destination_postcodes;
	}
	public void setDestination_postcodes(String[] destination_postcodes) {
		this.destination_postcodes = destination_postcodes;
	}
	
	
}
