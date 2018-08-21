package co.uk.pshealth.googleMapCache.model;

import java.util.List;

import org.springframework.stereotype.Component;


@Component
public class DistanceResult {

	private String[] origin_addresses;
	private List<String> destination_addresses;
	private List<Row> rows;
	private boolean outofYearlyCredits;
	
	
	public String[] getOrigin_addresses() {
		return origin_addresses;
	}
	public void setOrigin_addresses(String[] origin_addresses) {
		this.origin_addresses = origin_addresses;
	}
	public List<String> getDestination_addresses() {
		return destination_addresses;
	}
	public void setDestination_addresses(List<String> destination_addresses) {
		this.destination_addresses = destination_addresses;
	}
	public List<Row> getRows() {
		return rows;
	}
	public void setRows(List<Row> rows) {
		this.rows = rows;
	}
	public boolean isOutofYearlyCredits() {
		return outofYearlyCredits;
	}
	public void setOutofYearlyCredits(boolean outofYearlyCredits) {
		this.outofYearlyCredits = outofYearlyCredits;
	}
	
	
	
	
	 
}
