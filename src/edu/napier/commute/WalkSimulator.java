package edu.napier.commute;

import java.util.ArrayList;
import java.util.HashMap;

import edu.napier.geo.common.Journey;
import edu.napier.geo.common.Location;


public class WalkSimulator extends TransportSimulator {
/*
 * Obtain and store all of the journeys mde by this mode on this day - then provide feedback.
 * This simulator counts the journeys to work and checks the available parking spaces
 * 
 */
	@Override
	void newDay() {
	
		
	}

	@Override
	void addJourney(Commuter i, CJourney j, SimParams.direction dir) {
		
			
			
	

	}

	@Override
	void setFeedback() {
		/**
		 * Work out feedback here
		 *
		 * We have lists of journeys that end at each place of work
		 * Check if the number ending is > the car park capacity
		 * 
		 */
		//Need to order them based on arrival time
		
		//Feedback to commuters
		


	}
	

}
