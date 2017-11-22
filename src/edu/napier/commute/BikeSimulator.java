package edu.napier.commute;

import java.util.ArrayList;
import java.util.HashMap;

import edu.napier.geo.common.Journey;
import edu.napier.geo.common.Location;


public class BikeSimulator extends TransportSimulator {
/*
 * Obtain and store all of the journeys mde by this mode on this day - then provide feedback.
 * This simulator counts the journeys to work and checks the available parking spaces
 * 
 */
	private HashMap<String,ArrayList<CJourney>> journeysIN= new HashMap<String,ArrayList<CJourney>>();
	
	@Override
	void newDay() {
		journeysIN.clear();
		
	}

	@Override
	void addJourney(Commuter i, CJourney j, SimParams.direction dir) {
		if (dir == SimParams.direction.IN) {
			String workplace = j.getPointB().getDescription();
			if (!journeysIN.containsKey(workplace)) {
				journeysIN.put(workplace, new ArrayList<CJourney>());
			}
			ArrayList<CJourney> arrivals = journeysIN.get(workplace);
			arrivals.add(j);
			
			
		}//Ignore journeys home, at the moment

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
		
		for(ArrayList<CJourney> workplace : journeysIN.values()) {
			int spaces = SimParams.getInstance().getDefaultBikeParkSize();
			for(CJourney j : workplace) {
				if (spaces <= 0) {
					Commuter c = j.getCommuter();
					ArrayList<Feedback> feedbackList = new ArrayList<Feedback>();
					Feedback fb = new Feedback();
					fb.setDescription("Out of parking spaces");
					fb.setTimePenaltyMins(15);
					fb.setDirection(SimParams.direction.IN);
					fb.setType(Feedback.Type.BIKE_PARKINGCAPACITY);
					feedbackList.add(fb);
					fb = new Feedback();
					fb.setDescription("Out of parking spaces");
					fb.setTimePenaltyMins(15);
					fb.setDirection(SimParams.direction.OUT);
					fb.setType(Feedback.Type.BIKE_PARKINGCAPACITY);
					feedbackList.add(fb);
					c.setFeedBack(feedbackList);
				}
				spaces--;
			}
				
		}

	}
	

}
