package edu.napier.commute.tls;



public class TravelMode  {

	// String name of the travel mode (uses the DF_NAME of classes)
	private String mode = "NOT_SET";

	
	
	public TravelMode(){
		
	}
	
	public TravelMode(String mode){
		setMode(mode);
	}
	
	
	/* Builder functions */
	
	public TravelMode mode(String mode){
		setMode(mode);
		return this;
	}

	

	/**
	 * Returns [Mode: TRAVEL_MODE]
	 * @see getModeShortName() if presenting output to a user
	 */
	public String toString() {
		return "[Mode: " + mode + "]";
	}

	public TravelMode copy(){
		TravelMode t = new TravelMode();
		t.setMode(this.getMode());
		return t;
	}
	
	/* Getters and Setters */
	
	
	
	/**
	 * @return The string representation of the travel mode (matches the DF_NAME of the routing agents)
	 * @see getModeShortName() if presenting output to a user
	 */
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * Converts the mode to a shortend version (eg "Bus-Routing" to "Bus")
	 * @return The shortend version of the mode (DF_NAME)
	 */
	public String getModeShortName() {
		/*
		switch (mode) {
		case BusRouting.DF_NAME:
			return "Bus";
		case RailRouting.DF_NAME:
			return "Rail";
		case CarRouting.DF_NAME:
			return "Car";
		case FootRouting.DF_NAME:
			return "Foot";
		case BikeRouting.DF_NAME:
			return "Bike";
		default:*/
			return mode;
		//}
	}

	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof TravelMode))
			return false;

		TravelMode other = (TravelMode) o;
		if (this.hashCode() != other.hashCode())
			return false;

		return true;
	}


	@Override
	public int hashCode() {
		return getMode().hashCode();
	}

}
