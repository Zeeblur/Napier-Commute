package edu.napier.commute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.graphhopper.routing.util.FlagEncoder;

import edu.napier.geo.common.Journey;
import edu.napier.geo.common.JourneyFactory;
import edu.napier.geo.common.Location;
import facade.GHFacade;


public class CarProvider extends TransportProvider {
	private static HashMap<String , Object> options;//Used for storing GraphHopper options
	private static GHFacade gh = null;
	
	public static void init() {
		gh = new GHFacade();
		//Setup GraphHopper using the optionsMap	
		//String osmfile = "scotland-latest.osm.pbf";
		String folderPath = "./car/"; //SimParams.getInstance().getCarDirectory();

		FlagEncoder car = gh.getEncoder(gh.CAR);

		FlagEncoder[] encoders = {car};
		options = gh.getOptionsMap();
		options.put("pathToOSM", SimParams.getInstance().getOsmFile());
		options.put("pathToFolder", folderPath);
		options.put("profilesForGraph",encoders);
		options.put("profilesForRequest", car);
		options.put("enableCH",false);
		options.put("maxVisitedNodes",10000000);//extra 0
		options.put("includeElevation", true); //  Configured dimension (3) is not equal to dimension of loaded graph (2) (trying to load a non elevated graph elevated)
		options.put("algorithm", GHFacade.ASTAR); //gh.DIJKSTRABI);   // changed back to dik
		options.put("weighting", GHFacade.FASTEST);//added
		
		
		/*
		 * 	
			
		
			ghOptions.put("weighting", GHFacade.FASTEST);
			ghOptions.put("pathToOSM", osmPath);
			ghOptions.put("pathToFolder", folderPath);
		 */
	}



	
	
	@Override
	ArrayList<CJourney> getOptions(CJourney request) {
		SimParams params = SimParams.getInstance();
		// TODO Auto-generated method stub
		if (gh==null)//If first time calling...
			init();//..initialise gHopper
		
		// need to check if returns null then handle?
		Journey[] carOption = gh.getJourney(request.getPointA(), request.getPointB(), options);
		
		//Convert from Journey[] to ArrayLIst<CJouney>
		ArrayList<CJourney> options = new ArrayList<CJourney>();
		for(Journey j : carOption) {
			CJourney cj = new CJourney(j);
			cj.setEmissions(params.getEmSmallCarOnePass() * cj.getDistanceKM());
			cj.setCost(params.getCostSmallCarOnePass() * cj.getDistanceKM());
			options.add(cj);
		}
		
		/*
		 * GHJourney journey = null;
			try {
				journey = (GHJourney) facade.getJourney(origin, destination, ghOptions);
				journey = facade.route(journey);
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println("Origin = " + origin);
				System.out.println("Destination = " + destination);

				System.exit(-1);
			}
		 */

		return options;
	}



	@Override
	void setup() {
		// TODO Auto-generated method stub
		
	}

}
