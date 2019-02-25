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
			break;
		case 's':
			size = 343;
			break;
		}
		
		String fileName = args[1];
		
		int days = 30;
		
		if (args.length >= 3)
		{
			days = Integer.parseInt(args[2]);
		}
		
		SimParams params = SimParams.getInstance();
		params.setOutFile("SimLog.csv");
			if (args.length >= 4)
			{
				params.setOutFile(args[3]);
			}
			
		
			if (args.length >= 5)
			{
				params.setWalkRadius(Integer.parseInt(args[4]));
			}
			
			if (args.length == 6)
			{
				params.setWalkValue(Double.parseDouble(args[5]));
			}
			
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
