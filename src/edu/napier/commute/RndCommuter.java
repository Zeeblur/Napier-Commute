package edu.napier.commute;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;

import edu.napier.geo.common.Journey;

public class RndCommuter extends Commuter {
	
	public RndCommuter(int id, String desc, String strHome, String strWork, LocalTime depHome, LocalTime depWork) {
		
		super (id,desc,strHome,strWork,depHome, depWork) ;
	}
	
	public void selectTravelOption(int day) {
		//Select the travel option for the current day
		choiceIn = null;//Journey to work
		choiceOut = null;//Journey from work
		
		//Create a request object 
		CJourney requestIn = new CJourney(this.basicJourneyIn);
		requestIn.setTime(this._workStart);
		CJourney requestOut = new CJourney(this.basicJourneyIn);
		requestOut.setTime(this._workEnd);
		
		//Make a random selection
		Random rnd = new Random();
		TransportMode myMode = myModes.get(rnd.nextInt(myModes.size()));

		ArrayList<CJourney> choices = new ArrayList<CJourney>();

		System.out.println("Commuter " + _id + "Travelling by " + myMode);
		//Let's assume we always use the first option, if multuple options come back
		choices = TransportManager.getOptions(requestIn, myMode);
		if (choices.size()>0) {
			choiceIn = choices.get(0);
			choiceIn.setCommuter(this);
			modeIn = myMode;
		}

		choices = TransportManager.getOptions(requestOut, myMode);
		if (choices.size()>0) {
			choiceOut = choices.get(0);
			choiceOut.setCommuter(this);
			modeOut = myMode;
		}


		//For the moment assume the same mode is used IN and OUT
		if (choiceIn != null)
			TransportManager.addJourney(myMode, choiceIn, this, SimParams.direction.IN);
		if (choiceOut != null)
			TransportManager.addJourney(myMode, choiceOut, this, SimParams.direction.OUT);
	}
}
