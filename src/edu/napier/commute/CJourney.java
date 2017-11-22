package edu.napier.commute;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.napier.commute.tls.Leg;
import edu.napier.commute.tls.TravelMode;
import edu.napier.geo.common.Journey;
import edu.napier.geo.common.Location;
import facade.GHJourney;



public class CJourney extends Journey  {

	// All locations within the route
	List<Leg> legs = new ArrayList<Leg>();

	// Monetary cost of the journey
	double cost;
	// Emission cost of the journey
	double emissions;

	// Travel mode used
	TransportMode transportMode;

	LocalTime time;

	//Commuter associated with this Journey
	private Commuter commuter;
	
	
	public Commuter getCommuter() {
		return commuter;
	}

	public void setCommuter(Commuter commuter) {
		this.commuter = commuter;
	}

	public CJourney() {
		super(null, null,"");
	}

	public CJourney(Location a, Location b) {
		super(a, b,"");

		legs = new ArrayList<Leg>();
		legs.add(new Leg(a, b));
		legs.add(new Leg(b, b));
	}

	public CJourney(Journey journey) {
		this(journey.getPointA(), journey.getPointB());

		setDescription(journey.getDescription());
		setSource(journey.getSource());
		setDistanceKM(journey.getDistanceKM());
		setTravelTimeMS(journey.getTravelTimeMS());

	}


	public CJourney copy() {
		CJourney j = new CJourney(this);
		j.setCost(getCost());
		j.setEmissions(getEmissions());
		j.setTime(getTime());
		j.setTransportMode(getTransportMode());
		j.setLegs(getLegs());
		return j;
	}
	
	

	public String getStartIdentifier() {
		
		if (legs == null)
			return "null";
		return legs.get(0).getIdentifier();
		
	}

	public String getEndIdentifier() {
		if (legs == null)
			return "null";
		return legs.get(legs.size() - 1).getIdentifier();
		
	}

	public double getTravelTimeMin() {
		return getTravelTimeSec() / 60.0d;
	}

	public double getTravelTimeSec() {
		return getTravelTimeMS() * 0.001d;
	}

	public List<Location> getLocationList() {
		List<Location> locList = new ArrayList<Location>();
		for (Location l : legs) {
			locList.add(l);
		}
		return locList;
	}

	public List<Leg> getLegs() {
		return legs;
	}

	public void setLegs(List<Leg> legs) {
		this.legs = legs;
		if (legs != null) {
			this.locationA = legs.get(0);
			this.locationB = legs.get(legs.size() - 1);

		}
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getEmissions() {
		return emissions;
	}

	public void setEmissions(double emissions) {
		this.emissions = emissions;
	}

	public TransportMode getTransportMode() {
		return transportMode;
	}

	public void setTransportMode(TransportMode mode) {
		this.transportMode = mode;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public String toString() {
		String val = "{";
		for (int i = 0; i < legs.size(); i++) {
			val += legs.get(i);
			if (i < legs.size() - 1)
				val += ", ";
		}
		return val + "}";
	}

}
