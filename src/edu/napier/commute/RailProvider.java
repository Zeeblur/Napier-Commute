package edu.napier.commute;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import edu.napier.commute.tls.TLSParser;
import edu.napier.geo.common.Journey;

public class RailProvider extends TransportProvider {

	@Override
	ArrayList<CJourney> getOptions(CJourney request) {
		SimParams params = SimParams.getInstance();
		System.out.println("Getting rail details");
		
		/**
		 * Uses data from Traveline Scotland to find a rail route
		*/
				// Get the key for the map
				String key = request.getPointA().getDescription()+ "," + request.getPointB().getDescription();
				key = key.replace(" ", "");

				
				// Get journeys
				List<CJourney> options =   journeys.get(key);
				//if (options.size() >0)
				//	journeyOptions = new ArrayList<CJourney>(options);
				ArrayList<CJourney> journeyOptions = new ArrayList<CJourney>();

				if (options == null) {
					System.out.println( "No journey found for (" + key
							+ "). Please update the files used by the rail routing agent if there should a route between these locations.");
				} else {
					
					for (CJourney j : options) {
						j.setCost(params.getCostTrainTicket());//Need data for this
						j.setEmissions(j.getDistanceKM() * params.getCostBusDayTicket());
						LocalTime serviceTime = request.getTime();
						serviceTime = serviceTime.minusMinutes((int)Math.round(j.getTravelTimeMin()));
						j.setTime(serviceTime);
						journeyOptions.add(j);
					}
				}

		return journeyOptions;
	}

	private HashMap<String, List<CJourney>> journeys = new HashMap<String, List<CJourney>> ();
	
	protected void setup() {
		TLSParser parser = new TLSParser();
		
		Path temp = Paths.get(SimParams.getInstance().getTLSdirectory()/*"tlsData"*/);
		
		//System.out.println("***" +temp);
		
		try (Stream<Path> paths = Files.walk(Paths.get(SimParams.getInstance().getTLSdirectory()/*"tlsData"*/))) {
			paths.forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					System.out.println("Parsing TLS data " + filePath);
					try {
						HashMap<String, List<CJourney>> options = parser.parse(new File(filePath.toString()));
					
						if(options.keySet().size() == 1){
							System.err.println("[Warning]\tIgnoring bus data source, bus route only going one way: "+ options.keySet() +" - "+ filePath);
						}else{
							for(String key : options.keySet()){
								List<CJourney> opt = options.get(key);
								opt.removeIf(new Predicate<CJourney>(){
									@Override
									public boolean test(CJourney j) {
										//System.out.println(j.getTravelMode().getMode());
										//return !j.getTravelMode().getMode().equals("bus");//?
										return !(j.getTransportMode() == TransportMode.RAIL);
									}									
								});
								if(opt.size() > 0){
									if(journeys.get(key) == null)
										journeys.put(key, new ArrayList<CJourney>());
									journeys.get(key).addAll(opt);
								}
							}
						}
					} catch (ParserConfigurationException | SAXException | IOException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
