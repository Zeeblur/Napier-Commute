package edu.napier.commute;

import java.time.LocalTime;
import java.util.ArrayList;

import edu.napier.geo.common.Location;

abstract class Commuter {

	protected Location _home;
	protected Location _work;
	protected LocalTime _workStart;
	protected LocalTime _workEnd;
	protected int _id;
	protected String _description;
	protected CJourney basicJourneyIn;
	protected CJourney basicJourneyOut;
	protected ArrayList<TransportMode> myModes = new ArrayList<TransportMode>();
	protected ArrayList<Feedback> myFeedback;
	protected CJourney choiceIn = null;
	protected CJourney choiceOut = null;
	protected TransportMode modeIn;
	protected TransportMode modeOut;

	public Commuter(int id, String desc, String strHome, String strWork, LocalTime workStart, LocalTime workEnd) {
		_id = id;
		_description = desc;
		
		
		//Need to geoLoate home and work;
		_home = GeoCoder.find(strHome);
		_work = GeoCoder.find(strWork);
		basicJourneyIn = new CJourney(_home,_work);
		basicJourneyOut = new CJourney(_home,_work);
		_workStart = workStart;
		_workEnd = workEnd;
		
	}

	public boolean addTransportMode(TransportMode mode) {
		myModes.add(mode);
		
		//Check to see if this mode is possible for this commuter
		//Return false if it is not possible
		CJourney request = new CJourney(this.basicJourneyIn);
		request.setTime( LocalTime.of(0,0,0,0));
		if (TransportManager.getOptions(request, mode).size() >0)
			return true;
		else
			return false;
		
	}
	abstract  void selectTravelOption(int day);//This method contains the selection logic
	
	public void setFeedBack(ArrayList<Feedback> feedback) {
		//Set called after each day with feedback
		myFeedback = feedback;
	}

	public String getResultCSV() {
		String buffer = this._id + "," + this._description + ",";
		buffer += this._home.getDescription() +"," + this._workStart + ","+ this.modeIn +"," ;
		if (choiceIn != null)
		  buffer += this.choiceIn.getCost() + "," +this.choiceIn.getDistanceKM()+","+this.choiceIn.getEmissions() +"," + this.choiceIn.getTravelTimeMin() +",";
		else
			buffer += ",,,,";
		
		buffer += this._work.getDescription() +"," + this._workEnd + ","+ this.modeOut +",";
		if (choiceOut != null)
		  buffer += this.choiceOut.getCost() + ","+this.choiceOut.getDistanceKM()+","+this.choiceOut.getEmissions() +"," + this.choiceOut.getTravelTimeMin() +",";
		else
			buffer += ",,,,";
		buffer += "\n";
		
		return buffer;
	}

}