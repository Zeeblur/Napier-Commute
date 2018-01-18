package edu.napier.commute;

import java.time.LocalTime;

public class Application {

	public static void main(String[] args) {
		/*
		 * Configure simlation
		 */
		
		if (args.length < 2)
		{
			System.out.println("Command arguments not given");
			return;
		}
		
		// for quick results, 
		char campus = args[0].charAt(0);
		
		int size = 0;
		switch (campus)
		{
		case 'c':
			size = 353;
			break;
		case 'm':
			size = 65;
		case 's':
			size = 343;
			break;
		}
		
		String fileName = args[1];
		
		int days = 30;
		
		if (args.length == 3)
		{
			days = Integer.parseInt(args[2]);
		}
		SimParams params = SimParams.getInstance();
		params.setOsmFile("./scotland-latest.osm.pbf");
		params.setTLSdirectory("./tlsData");
		params.setCarDirectory("./car/");

		params.setDays(days); //Run for 1 month
		params.setDefaultCarParkSize (size);//For testing!
		params.setDefaultBikeParkSize (20);//For testing!
		
		Simulator.init();
		ProblemReader pr = new ProblemReader(fileName + ".csv");
		Simulator.run();
		
	}
}
