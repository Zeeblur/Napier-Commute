package edu.napier.commute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.napier.geo.common.Journey;

public class TransportManager {
  /*
   * This class is responsible for managing a pool of Transport Providers and simulators
   * If you add a new Provider, you must modify this class - update init and update the enum
   * 
   */
	
	//This is the master list of valid transport modes
	/*	public enum transportMode{CAR("car"),BUS("bus");
			
			// Member to hold the name
			   private String string;

			   // constructor to set the string
			   transportMode(String name){string = name;}

			   // the toString just returns the given name
			   @Override
			   public String toString() {
			       return string;
			   }
		};  */
	
	private static HashMap<TransportMode,  TransportProvider> providerPool = new HashMap<TransportMode,TransportProvider>();
	private static HashMap<TransportMode,  TransportSimulator> simulatorPool = new HashMap<TransportMode,TransportSimulator>();
	
	public static void init() {
		/*
		 * Create the various transport providers - 1 per mode
		 */
	   providerPool.put(TransportMode.BUS, new BusProvider());
	   providerPool.put(TransportMode.CAR, new CarProvider());
	   providerPool.put(TransportMode.RAIL, new RailProvider());
	   providerPool.put(TransportMode.BIKE, new BikeProvider());
	   providerPool.put(TransportMode.WALK, new WalkProvider());
	   
	   for(TransportProvider tp: providerPool.values())
		   tp.setup();
	   /*
		 * Create the various transport simulators - 1 per mode
		 */
	   simulatorPool.put(TransportMode.BUS, new BusSimulator());
	   simulatorPool.put(TransportMode.CAR, new CarSimulator());
	   simulatorPool.put(TransportMode.RAIL, new RailSimulator());
	   simulatorPool.put(TransportMode.BIKE, new BikeSimulator());
	   simulatorPool.put(TransportMode.WALK, new WalkSimulator());
		}
	
	public static ArrayList<CJourney> getOptions(CJourney request, TransportMode tm) {
		TransportProvider tp = providerPool.get(tm);
		return tp.getOptions(request);
	}
	
	public static void addJourney(TransportMode m, CJourney j, Commuter c, SimParams.direction dir) {
		simulatorPool.get(m).addJourney(c, j,dir);
	}
	
	public static void generateFeedback() {
		for (TransportSimulator ts : simulatorPool.values())
			ts.setFeedback();
	}
	
	/*
	 * Reset the simulators for a new day
	 */
	public static void newDay() {
		for(TransportSimulator ts : simulatorPool.values())
			ts.newDay();
	}
	
}
