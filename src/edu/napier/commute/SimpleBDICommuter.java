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
	private double walkTime=-1;
	
	public SimpleBDICommuter(int id, String desc, String strHome, String strWork, LocalTime depHome, LocalTime depWork) {
		
		super (id,desc,strHome,strWork,depHome, depWork) ;
	}
	
	public void selectTravelOption(int day) {
		//Select the travel option for the current day
		
		System.out.println("BDI day " +day + "Commuter " + this._description);
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


			double newTime = 0;

			//newTime=beliefs.get(TransportMode.CAR);  // this assumes everyone has a car belief.

			try
			{
				newTime=beliefs.get(current);
			}
			catch (Exception e)
			{
				// mode not viable
				
				int type = 0;

				for(int i = 0; i < TransportMode.values().length; ++i)
				{
					if (newTime != 0)
						break;

					try
					{
						newTime = beliefs.get(TransportMode.valueOf(type));
					}
					catch (Exception e2)
					{
						// value not in list
						// increment and loop
						type++;
					}
				}
			}

			

			double actualTime = beliefs.get(current);
					
			if (myFeedback != null) {
				CJourney optionTo = getCheapestOptionInTime(TransportManager.getOptions(requestIn, current));
				CJourney optionFrom= getCheapestOptionInTime(TransportManager.getOptions(requestOut, current));
				optionTo.setTransportMode(current);
				optionFrom.setTransportMode(current);	
				actualTime = optionTo.getTravelTimeMin() + optionFrom.getTravelTimeMin();//Recalc time
				for (Feedback df: myFeedback) {
					System.out.println("Feedback - "+ this._description +": "+ df.getDescription());
					// Check capacity constraints - do this here

					if(df.getType()==Feedback.Type.CAR_PARKINGCAPCITY) {
						//Out of car park space
						penalty = df.getTimePenaltyMins();
						actualTime += penalty;

					}
					if(df.getType() == Feedback.Type.BIKE_PARKINGCAPACITY) {
						//Out of car park space
						penalty = df.getTimePenaltyMins();
						actualTime += penalty;

					}
				}
				if (actualTime  > beliefs.get(current) )//it's got worse!
					patience --;

				if (actualTime < beliefs.get(current)   ) {//it's got better
					if (patience < maxPatience)
						patience ++;
				}
				
				double bestTime = Double.MAX_VALUE;
				
				//Update beliefs
				beliefs.put(current, new Double(actualTime));

				for(TransportMode m : myModes) {
					Double time = beliefs.get(m);
					if (time != null)
						if (time<bestTime)
							bestTime = time;
				}


				if (actualTime > bestTime)
					patience --;

				
				

				if (patience <=0) {
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
					patience = maxPatience;
				}

			}


		}
		
		//Walk selector
		if (walkTime == -1) {
			ArrayList<CJourney> ops =  TransportManager.getOptions(requestIn, TransportMode.WALK);
			walkTime = ops.get(0).getTravelTimeMin();
		}

		SimParams params = SimParams.getInstance();
		if (params.getWalkValue()> -1) {
			double probToWalk =  1/(Math.pow(params.getWalkValue(),(walkTime/5)));

			if (rnd.nextDouble()<probToWalk)
				current = TransportMode.WALK;
		}
		if (walkTime < params.getWalkRadius())//If walk is less than a specified time then always walk
			current = TransportMode.WALK;
		
		
		if (current != null) {


			System.out.println("Commuter " + this._description+ "Travelling by " + current);
			//For the moment assume the same mode is used IN and OUT
			ArrayList<CJourney> tmp = TransportManager.getOptions(requestIn, current);
			choiceIn = tmp.get(0);
			choiceIn.setCommuter(this);
			choiceIn.setTransportMode(current);
			this.modeIn = current;
			this.modeOut = current;
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
