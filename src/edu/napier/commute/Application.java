package edu.napier.commute;

import java.time.LocalTime;

public class Application {

	public static void main(String[] args) {
		/*
		 * Configure simlation
		 */
		SimParams params = SimParams.getInstance();
		params.setDays(30);; //Run for 1 month
		params.setDefaultCarParkSize (2);//For testing!
		params.setDefaultBikeParkSize (2);//For testing!

		params.setOsmFile("./scotland-latest.osm");
		params.setTLSdirectory("C:\\commuteData\\data\\tlsData");
		
		Simulator.init();
		ProblemReader pr = new ProblemReader("C:\\commuteData\\data\\problems\\NapierMerch.csv");
		Simulator.run();
		
	}
}
