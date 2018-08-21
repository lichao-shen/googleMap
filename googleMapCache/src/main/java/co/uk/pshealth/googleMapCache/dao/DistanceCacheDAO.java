package co.uk.pshealth.googleMapCache.dao;

import java.time.LocalDateTime;

public class DistanceCacheDAO {

	private long id;	
	private String originPostcode;
	private String destinationPostcode;
	private long travelDistanceMetres;
	private long travelTimeSeconds;
	private LocalDateTime timestamp;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getOriginPostcode() {
		return originPostcode;
	}
	public void setOriginPostcode(String originPostcode) {
		this.originPostcode = originPostcode;
	}
	public String getDestinationPostcode() {
		return destinationPostcode;
	}
	public void setDestinationPostcode(String destinationPostcode) {
		this.destinationPostcode = destinationPostcode;
	}
	public long getTravelDistanceMetres() {
		return travelDistanceMetres;
	}
	public void setTravelDistanceMetres(long travelDistanceMetres) {
		this.travelDistanceMetres = travelDistanceMetres;
	}
	public long getTravelTimeSeconds() {
		return travelTimeSeconds;
	}
	public void setTravelTimeSeconds(long travelTimeSeconds) {
		this.travelTimeSeconds = travelTimeSeconds;
	}
	
}
