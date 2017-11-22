package edu.napier.commute;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SimpleBDICommuter2 extends Commuter {
	/*
	 * BDI code
	 */
	private int patience=0;
	private int maxPatience =0;
	private Random rnd = new Random();
	private HashMap<TransportMode, CJourney> beliefs = new HashMap<TransportMode, CJourney>();
	private HashMap<TransportMode, Double> baseTimes = new HashMap<TransportMode, Double>();
	private CJourney current = null;
	
	public SimpleBDICommuter2(int id, String desc, String strHome, String strWork, LocalTime depHome, LocalTime depWork) {
		
		super (id,desc,strHome,strWork,depHome, depWork) ;
	}
	
	public void selectTravelOption(int day) {
		//Select the travel option for the current day
		
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
			//get default mode

			// Create a map to store the best option for each travel mode
			//HashMap<String, JourneyOption> bestOptions = new HashMap<String, JourneyOption>();

			// Get the best to and from journey for each mode
			for (TransportMode mode : this.myModes) {
				
				// Set the mode, to and from journey, and the respective durations
			
				CJourney optionTo = getCheapestOptionInTime(TransportManager.getOptions(requestIn, mode));
				CJourney optionFrom= getCheapestOptionInTime(TransportManager.getOptions(requestOut, mode));
				
				if (optionTo == null || optionFrom == null)
					continue;
				
				optionTo.setTransportMode(mode);
				optionFrom.setTransportMode(mode);
				

				double totalTravelTime = optionTo.getTravelTimeMin() + optionFrom.getTravelTimeMin();

				// Add the option to the bestOptions map
				baseTimes.put(mode, new Double(totalTravelTime));
				beliefs.put(mode, optionTo); //Crude, only using To, not From
			}


			for (CJourney j : beliefs.values()) {

				if (current == null || (j.getTravelTimeMin()) < (current.getTravelTimeMin()))
					current = j;

			}


		}
		else {
			//day > 0


			//f = getFeedback
			int penalty =0;
			if (myFeedback != null) {
				for (Feedback df: myFeedback) {
					System.out.println("Feedback - "+ this._description +": "+ df.getDescription());
					// Check capacity constraints - do this here
					double newTime = current.getTravelTimeMin();
					if(df.getType()==Feedback.Type.CAR_PARKINGCAPCITY) {
						//Out of car park space
						penalty = df.getTimePenaltyMins();
						newTime = (penalty + baseTimes.get(TransportMode.CAR));

					}
					if(df.getType() == Feedback.Type.BIKE_PARKINGCAPACITY) {
						//Out of car park space
						penalty = df.getTimePenaltyMins();
						newTime = (penalty + baseTimes.get(TransportMode.BIKE));

					}

					if (newTime  > this.choiceIn.getTravelTimeMin()+ this.choiceOut.getTravelTimeMin() )//it's got worse!
						patience --;

					if (newTime < this.choiceIn.getTravelTimeMin()+ this.choiceOut.getTravelTimeMin()  ) {//it's got better
						patience ++;
					}
					double bestTime = Double.MAX_VALUE;

					for (CJourney j : beliefs.values()) {
						if (j.getTransportMode() != current.getTransportMode())
							if ((j.getTravelTimeMin()*2) < bestTime)
								bestTime = (j.getTravelTimeMin()*2) ;
					}

					if (newTime > bestTime)
						patience --;

					//Update beliefs
					beliefs.remove(choiceIn.getTransportMode());
					choiceIn.setTravelTimeMS(newTime);
					//beliefs.put(choiceIn.getTransportMode(), current);
					beliefs.put(choiceIn.getTransportMode(), choiceIn);


					if (patience ==0) {
						//change mode for next day!
						bestTime = Double.MAX_VALUE;
						CJourney newMode=null;
						for (CJourney j : beliefs.values()) {

							if (j.getTravelTimeMin() < bestTime) {
								bestTime = j.getTravelTimeMin();
								newMode =j;
							}
						}
						current = newMode;
						patience = maxPatience;
					}
				}
			}


		}


		if (current != null) {


			System.out.println("Commuter " + _id + "Travelling by " + current.getTransportMode());



			//For the moment assume the same mode is used IN and OUT
			ArrayList<CJourney> tmp = TransportManager.getOptions(requestIn, current.getTransportMode());
			choiceIn = tmp.get(0);
			choiceIn.setCommuter(this);
			tmp = TransportManager.getOptions(requestOut, current.getTransportMode());
			choiceOut = tmp.get(0);
			choiceOut.setCommuter(this);
			
			if (choiceIn != null)
				TransportManager.addJourney(current.getTransportMode(), choiceIn, this, SimParams.direction.IN);
			if (choiceOut != null)
				TransportManager.addJourney(current.getTransportMode(), choiceOut, this, SimParams.direction.OUT);

		}

		// Pick randomly if there is no best option
		//return new RandomSelector().getJourneyChoice(toWorkOptions, fromWorkOptions, employee);





		/*
		 * End BDI code
		 * 
		 */

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
