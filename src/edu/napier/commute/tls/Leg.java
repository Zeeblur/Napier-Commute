package edu.napier.commute.tls;


import edu.napier.geo.common.Location;

public class Leg extends CLocation{

	public TravelMode travelMode;
	public Location to;
	
	public Leg(){
		super();
	}

	public Leg(double lat, double lon, double alt) throws IllegalArgumentException{
		super(lat, lon, alt);
	}

	public Leg(Location curr, Location to) throws IllegalArgumentException{
		super(curr.getLat(), curr.getLon(), curr.getAlt());
		setTo(to);
	}

	
	
	
	public TravelMode getTravelMode() {
		return travelMode;
	}

	public void setTravelMode(TravelMode travelMode) {
		this.travelMode = travelMode;
	}

	public Location getTo() {
		return to;
	}

	public void setTo(Location to) {
		this.to = to;
	}
}
