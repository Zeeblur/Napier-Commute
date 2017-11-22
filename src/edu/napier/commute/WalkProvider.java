package edu.napier.commute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.graphhopper.routing.util.FlagEncoder;

import com.graphhopper.routing.util.FootFlagEncoder;
import edu.napier.geo.common.Journey;
import edu.napier.geo.common.JourneyFactory;
import edu.napier.geo.common.Location;
import facade.GHFacade;
import facade.GHJourney;


public class WalkProvider extends TransportProvider {
	private static HashMap<String , Object> options;//Used for storing GraphHopper options
	private static GHFacade gh = null;
	
	public static void init() {
		gh = new GHFacade();
		//Setup GraphHopper using the optionsMap	

		String folderPath = "./walk/";

		FlagEncoder car = gh.getEncoder(gh.FOOT);

		FlagEncoder[] encoders = {car};
		options = gh.getOptionsMap();
		options.put("pathToOSM", SimParams.getInstance().getOsmFile());
		options.put("pathToFolder", folderPath);
		options.put("profilesForGraph",encoders);
		options.put("profilesForRequest", car);
		options.put("enableCH",false);
		options.put("maxVisitedNodes",1000000);
		options.put("includeElevation", false);
		options.put("algorithm",gh.DIJKSTRABI);
		
		
	}



	
	
	@Override
	ArrayList<CJourney> getOptions(CJourney request) {
		SimParams params = SimParams.getInstance();
		if (gh==null)//If first time calling...
			init();//..initialise gHopper
		
		Journey[] carOption = gh.getJourney(request.getPointA(), request.getPointB(), options);
		//journey = facade.route(journey);


		
		
		
		//Convert from Journey[] to ArrayLIst<CJouney>
		ArrayList<CJourney> options = new ArrayList<CJourney>();
		for(Journey j : carOption) {
			CJourney cj = new CJourney(j);
			cj.setEmissions(params.getEmWalk()* cj.getDistanceKM());
			cj.setCost(params.getCostWalk() * cj.getDistanceKM());
			options.add(cj);
		}

		return options;
		/*
		SimParams params = SimParams.getInstance();
		if (gh==null)//If first time calling...
			init();//..initialise gHopper
		
		Journey[] carOption = gh.getJourney(request.getPointA(), request.getPointB(), options);
		
		//Convert from Journey[] to ArrayLIst<CJouney>
		ArrayList<CJourney> options = new ArrayList<CJourney>();
		for(Journey j : carOption) {
			CJourney cj = new CJourney(j);
			cj.setEmissions(params.getEmWalk()* cj.getDistanceKM());
			cj.setCost(params.getCostWalk() * cj.getDistanceKM());
			options.add(cj);
		}

		return options;*/
	}



	@Override
	void setup() {
		// TODO Auto-generated method stub
		
	}

}
