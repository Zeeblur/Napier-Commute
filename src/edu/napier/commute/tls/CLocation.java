package edu.napier.commute.tls;


import edu.napier.geo.common.Location;

public class CLocation extends Location {

	// String that identifies this location (postcode)
	private String identifier;

	public CLocation(String identifier) {
		super();
		this.identifier = identifier;
	}

	public CLocation(double lat, double lon, double alt) throws IllegalArgumentException {
		super(lat, lon, alt);
	}

	public CLocation(double lat, double lon, String source) throws IllegalArgumentException {
		super(lat, lon, source);
	}

	public CLocation(double lat, double lon) throws IllegalArgumentException {
		super(lat, lon);
	}

	public CLocation(CLocation loc) throws IllegalArgumentException {
		super(loc.getLat(), loc.getLon(), loc.getAlt());
		setIdentifier(loc.getIdentifier());
		setDescription(loc.getDescription());
		setSource(loc.getSource());
	}
	
	public CLocation() {
		super();
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + super.toString() + ")";
	}
	

	public CLocation copy(){
		return new CLocation(this);
	}

	/*
	 * The functions below are used to identify matching locations in the routing caches
	 */
	
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof Location))
			return false;

		Location other = (CLocation) o;
		if (this.getLat() != other.getLat())
			return false;
		if (this.getLon() != other.getLon())
			return false;

		return true;
	}

	public int hashCode() {
		return (int) ((int) this.getLat() * this.getLon() + this.identifier.hashCode());
	}

}
