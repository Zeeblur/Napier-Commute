package edu.napier.commute;
/*
 * Holds details of feedback passed from TransportSimulator to Commuter
 * 
 */
public class Feedback {
	
	public enum Type{CAR_PARKINGCAPCITY,BIKE_PARKINGCAPACITY;
		
	}
	private String description;//A text string that can be used when printing out details
	private SimParams.direction direction; //Is this journey to/from work?
	/*/
	 * The following values represent penalties to be added on to the journey under consideration
	 */
	private int timePenaltyMins;
	private int costPenaltyPence;
	private int CO2penaltyG;
	private Type _type;

	
	public String getDescription() {
		return description;
	}
	public SimParams.direction getDirection() {
		return direction;
	}
	public void setDirection(SimParams.direction direction) {
		this.direction = direction;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getTimePenaltyMins() {
		return timePenaltyMins;
	}
	public void setTimePenaltyMins(int timePenaltyMins) {
		this.timePenaltyMins = timePenaltyMins;
	}
	public int getCostPenaltyPence() {
		return costPenaltyPence;
	}
	public void setCostPenaltyPence(int costPenaltyPence) {
		this.costPenaltyPence = costPenaltyPence;
	}
	public int getCO2penaltyG() {
		return CO2penaltyG;
	}
	public void setCO2penaltyG(int cO2penaltyG) {
		CO2penaltyG = cO2penaltyG;
	}
	public Type getType() {
		return _type;
	}
	public void setType(Type _type) {
		this._type = _type;
	}

	
}
