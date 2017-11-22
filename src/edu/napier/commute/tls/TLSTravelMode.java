package edu.napier.commute.tls;

import java.util.Date;


public class TLSTravelMode extends TravelMode {

	public String service;
	public String operator;
	public String operatorCode;
	
	public Date departure;
	public Date arrival;
	
	public TLSTravelMode(){
		super("");
	}
	
	/* Builder functions */
	
	public TLSTravelMode mode(String mode){
		setMode(mode);
		return this;
	}	
	public TLSTravelMode serviceNumber(String serviceNumber){
		setService(serviceNumber);
		return this;
	}	
	public TLSTravelMode operator(String operator){
		setOperator(operator);
		return this;
	}
	public TLSTravelMode departure(Date departure){
		setDeparture(departure);
		return this;
	}
	public TLSTravelMode arrival(Date arrival){
		setArrival(arrival);
		return this;
	}
	

	public String toString(){
		return "[Mode: "+ getMode() +", Serive Number:"+ service +", Operator:"+ operator +", Operator Code:"+ operatorCode+", Depature: "+ departure+", Arrival: "+ arrival +"]";
	}

	@Override
	public TLSTravelMode copy(){
		TLSTravelMode t = new TLSTravelMode();
		t.setMode(this.getMode());
		t.setArrival(this.getArrival());
		t.setDeparture(this.getDeparture());
		t.setOperator(this.getOperator());
		t.setOperatorCode(this.getOperatorCode());
		t.setService(this.getService());		
		return t;
	}
		
	
	/* Getters and Setters */
	
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}

	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public Date getDeparture() {
		return departure;
	}
	public void setDeparture(Date departure) {
		this.departure = departure;
	}

	public Date getArrival() {
		return arrival;
	}
	public void setArrival(Date arrival) {
		this.arrival = arrival;
	}
	
	
	
		
}
