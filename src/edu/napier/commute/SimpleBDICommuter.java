package edu.napier.commute;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SimpleBDICommuter extends Commuter {
	/*
	 * BDI code
	 */
	private int patience=0;
	private int maxPatience =0;
	private Random rnd = new Random();
	private HashMap<TransportMode, Double> beliefs = new HashMap<TransportMode, Double>();
	private TransportMode current = null;
	
	public SimpleBDICommuter(int id, String desc, String strHome, String strWork, LocalTime depHome, LocalTime depWork) {
		
		super (id,desc,strHome,strWork,depHome, depWork) ;
	}
	
	public void selectTravelOption(int day) {
		//Select the travel option for the current day
		
		System.out.println("BDI day " +day);
		//Create a request object 
		CJourney requestIn = new CJourney(this.basicJourneyIn);
		requestIn.setTime(this._workStart);
		CJourney requestOut = new CJourney(this.basicJourneyIn);
		requestOut.setTime(this._workEnd);
/*
		 * BDI code
		 */
		
		if (day==0) {
			maxPatience = rnd.nextInt(5) +5;
			patience = maxPatience;

			System.out.println("Patience "+ patience);
			
			// Get the best to and from journey for each mode
			for (TransportMode mode : this.myModes) {
				System.out.println("Mode" +mode.toString());
				// Set the mode, to and from journey, and the respective durations
			
				
				CJourney optionTo = getCheapestOptionInTime(TransportManager.getOptions(requestIn, mode));
				CJourney optionFrom= getCheapestOptionInTime(TransportManager.getOptions(requestOut, mode));
				
				if (optionTo == null || optionFrom == null)
					continue;
				
				optionTo.setTransportMode(mode);
				optionFrom.setTransportMode(mode);
				

				double totalTravelTime = optionTo.getTravelTimeMin() + optionFrom.getTravelTimeMin();

				// Add the option to the bestOptions map
				beliefs.put(mode, new Double(totalTravelTime));
			}

			/*
			 * 
			 
			System.out.println("Beliefs");
			for (TransportMode m : beliefs.keySet()) {
				System.out.println(m.toString() +":"+ beliefs.get(m));
			}
			//find quickest

			System.out.println("Done");*/
			
			double bestTime = Double.MAX_VALUE;
			for(TransportMode m: myModes) {
				Double time = beliefs.get(m);
				if (time != null) {
					if ((current == null) || (time< bestTime)) {
						current = m;
						bestTime = time;
					}
				}
			}
		}
		else {
			//day > 0


			//f = getFeedback
			int penalty =0;
			double newTime=beliefs.get(TransportMode.CAR);

			if (myFeedback != null) {
				for (Feedback df: myFeedback) {
					System.out.println("Feedback - "+ this._description +": "+ df.getDescription());
					// Check capacity constraints - do this here

					if(df.getType()==Feedback.Type.CAR_PARKINGCAPCITY) {
						//Out of car park space
						penalty = df.getTimePenaltyMins();
						newTime += penalty;

					}
					if(df.getType() == Feedback.Type.BIKE_PARKINGCAPACITY) {
						//Out of car park space
						penalty = df.getTimePenaltyMins();
						newTime += penalty;

					}
				}
				if (newTime  > beliefs.get(current) )//it's got worse!
					patience --;

				if (newTime < beliefs.get(current)   ) {//it's got better
					if (patience < maxPatience)
						patience ++;
				}
				
				double bestTime = Double.MAX_VALUE;

				for(TransportMode m : myModes) {
					Double time = beliefs.get(m);
					if (time != null)
						if (time<bestTime)
							bestTime = time;
				}


				if (newTime > bestTime)
					patience --;

				//Update beliefs
				beliefs.put(current, new Double(newTime));


				if (patience ==0) {
					TransportMode oldCurrent = current; 
					//change mode for next day!
					bestTime = Double.MAX_VALUE;
					for(TransportMode m : myModes) {
						Double time = beliefs.get(m);
						if (time != null)
							if (time<bestTime) {
								bestTime = time;
								current = m;
							}
					}
					if(oldCurrent != current) {
						System.out.println("Mode switch!");
					}
				}

			}


		}


		if (current != null) {


			System.out.println("Commuter " + _id + "Travelling by " + current);
			//For the moment assume the same mode is used IN and OUT
			ArrayList<CJourney> tmp = TransportManager.getOptions(requestIn, current);
			choiceIn = tmp.get(0);
			choiceIn.setCommuter(this);
			choiceIn.setTransportMode(current);
			this.modeIn = current;
			
			tmp = TransportManager.getOptions(requestOut, current);
			choiceOut = tmp.get(0);
			choiceOut.setTransportMode(current);
			this.modeOut = current;
			choiceOut.setCommuter(this);
			
			if (choiceIn != null)
				TransportManager.addJourney(current, choiceIn, this, SimParams.direction.IN);
			if (choiceOut != null)
				TransportManager.addJourney(current, choiceOut, this, SimParams.direction.OUT);

		}
		

	}

	/**
	 * @param journeys List of CJourneys to look through
	 * @return The CJourney that takes the least time
	 */
	protected CJourney getCheapestOptionInTime(ArrayList<CJourney> journeys) {
		if (journeys == null)
			return null;

		CJourney best = null;
		for (CJourney j : journeys) {
			if (best == null || j.getTravelTimeMin() < best.getTravelTimeMin())
				best = j;
		}
		return best;
	}
}
