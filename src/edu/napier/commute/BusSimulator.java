package edu.napier.commute;

import java.util.HashMap;

import edu.napier.geo.common.Journey;

public class BusSimulator extends TransportSimulator {
	/*
	 * Obtain and store all of the journeys mde by this mode on this day - then provide feedback.
	 * Differentiate between Journeys IN and journeys OUT
	 * 
	 */
		private HashMap<Commuter,Journey> journeysIN= new HashMap<Commuter, Journey>();
		private HashMap<Commuter,Journey> journeysOUT= new HashMap<Commuter, Journey>();
		
		
		@Override
		void newDay() {
			journeysIN.clear();
			journeysOUT.clear();

		}

		@Override
		void addJourney(Commuter i, CJourney j, SimParams.direction dir) {
			if (dir == SimParams.direction.IN)
				journeysIN.put(i, j);
			else
				journeysOUT.put(i, j);
		}

		@Override
		void setFeedback() {
			/**
			 * Work out feedback here
			 *
			 */
			
			

		}
}
