package co.uk.pshealth.googleMapCache.model;

public class Element {

	private long distance;
	private long duration;
	
	public Element(long l, long m) {
		this.distance = l;
		this.duration = m;
	}
	public long getDistance() {
		return distance;
	}
	public void setDistance(long distance) {
		this.distance = distance;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	
}
