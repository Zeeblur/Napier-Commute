package edu.napier.commute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.graphhopper.routing.util.FlagEncoder;

import edu.napier.geo.common.Journey;
import edu.napier.geo.common.JourneyFactory;
import edu.napier.geo.common.Location;
import facade.GHFacade;


public class BikeProvider extends TransportProvider {
	private static HashMap<String , Object> options;//Used for storing GraphHopper options
	private static GHFacade gh = null;
	
	public static void init() {
		gh = new GHFacade();
		//Setup GraphHopper using the optionsMap	
		//String osmfile = "scotland-latest.osm.pbf";
		
				String folderPath = "./bike/";

				FlagEncoder car = gh.getEncoder(gh.BIKE);

				/*FlagEncoder[] encoders = {car};
				options = gh.getOptionsMap();
				options.put("pathToOSM", SimParams.getInstance().getOsmFile());
				options.put("pathToFolder", folderPath);
				options.put("profilesForGraph",encoders);
				options.put("profilesForRequest", car);
				options.put("enableCH",false);
				options.put("maxVisitedNodes",1000000);
				options.put("includeElevation", false);
				options.put("algorithm",gh.DIJKSTRABI);*/
				FlagEncoder[] encoders = {car};
				options = gh.getOptionsMap();
				options.put("pathToOSM", SimParams.getInstance().getOsmFile());
				options.put("pathToFolder", folderPath);
				options.put("profilesForGraph",encoders);
				options.put("profilesForRequest", car);
				options.put("enableCH",false);
				options.put("maxVisitedNodes",10000000);//extra 0
				options.put("includeElevation", true);//true
				options.put("algorithm",GHFacade.ASTAR);//gh.DIJKSTRABI);
				options.put("weighting", GHFacade.FASTEST);//added
	}



	
	
	@Override
	ArrayList<CJourney> getOptions(CJourney request) {
		SimParams params  = SimParams.getInstance();
		if (gh==null)//If first time calling...
			init();//..initialise gHopper
		
		Journey[] carOption = gh.getJourney(request.getPointA(), request.getPointB(), options);
		
		//Convert from Journey[] to ArrayLIst<CJouney>
		ArrayList<CJourney> options = new ArrayList<CJourney>();
		for(Journey j : carOption) {
			CJourney cj = new CJourney(j);
			cj.setTravelTimeMS(cj.getTravelTimeMS() +1200000);//Add 20 mins to cover showering and changing
			cj.setEmissions(params.getEmBike()* cj.getDistanceKM());
			cj.setCost(params.getCostBike() * cj.getDistanceKM());
			options.add(cj);
		}

		return options;
	}



	@Override
	void setup() {
		// TODO Auto-generated method stub
		
	}

}
