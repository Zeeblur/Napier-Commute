package edu.napier.commute.tls;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.napier.commute.CJourney;
import edu.napier.commute.TransportMode;
import edu.napier.geo.common.Location;
import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.OSRef;

public class TLSParser {
	
	
	public TLSParser(){

	}
	
	public HashMap<String, List<CJourney>> parse(File file) throws ParserConfigurationException, SAXException, IOException{
		HashMap<String, List<CJourney>> journeys = new HashMap<String, List<CJourney>>();
		
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(file);
		doc.getDocumentElement().normalize();
		
		NodeList nList = doc.getElementsByTagName("Itinerary");
		for(int i = 0; i < nList.getLength(); i++){
			Node itinerary = nList.item(i);
			if(itinerary.getNodeName().equals("Itinerary")){
				List<CJourney> journeyOptions = parseItinerary(itinerary);
				if(journeyOptions.size() > 0){
					List<Leg> legs = journeyOptions.get(0).getLegs();
					String start = legs.get(0).getIdentifier();
					String end = legs.get(legs.size()-1).getIdentifier();
					String key = start +","+ end;
					List<CJourney> mapVal = journeys.get(key);
					if(mapVal == null){
						journeys.put(key, journeyOptions);
					}else{
						mapVal.addAll(journeyOptions);
					}
				}
			}
		}
		return journeys;
  	}
	
	/**
	 * Parses all possible routes for an itinerary between two points
	 * @param itinerary
	 * @return All journeys for this itinerary
	 */
	public List<CJourney> parseItinerary(Node itinerary){
		List<CJourney> journeyOptions = new ArrayList<CJourney>();
		NodeList legs = itinerary.getChildNodes();
		// Parse all possible routes for this itinerary (all routes between A and B)
		for(int i = 0; i < legs.getLength(); i++){
			if(legs.item(i).getNodeName().equals("Legs")){
				journeyOptions.add(parseLegs(legs.item(i)));
			}
		}
		return journeyOptions;
	}
	
	/**
	 * Parses one route option
	 * @param legs
	 * @return A journey representing the route
	 */
	public CJourney parseLegs(Node legs){
		List<Leg> legList = new ArrayList<Leg>();
		NodeList legNodes = legs.getChildNodes();
		
		// Parse all legs within this node to fill the route
		for(int i = 0; i < legNodes.getLength(); i++){
			if(legNodes.item(i).getNodeName().equals("Leg")){
				Leg leg = parseLeg(legNodes.item(i), legNodes.getLength());
				if(leg != null){
					legList.add(leg);
				}
			}
		}
				
		// Create a CJourney to represent the route (use the first and last node for locations)
		CJourney journey = new CJourney(legList.get(0), legList.get(legList.size()-1));
		journey.setLegs(legList);
		journey.setDescription("One route option loaded using "+ this.getClass().getName());
		boolean containsRail = false;
		for(Leg l : legList){
			if(l.getTravelMode().getMode().equals("rail"))//RailRouting.DF_NAME))
				containsRail = true;
		}
		//journey.setTransportMode(new TravelMode().mode(containsRail ? "rail"/*RailRouting.DF_NAME*/ : "bus"/*BusRouting.DF_NAME*/));
		if(containsRail) {
			journey.setTransportMode(TransportMode.RAIL);
		}else
			journey.setTransportMode(TransportMode.BUS);
		
		// Get the expected start and end time of the journey
		Date start = null;
		Date end = null;
		for(int i = 0; i < legList.size(); i++){
			TravelMode mode = legList.get(i).getTravelMode();
			if(mode instanceof TLSTravelMode){
				if(start == null){
					start = ((TLSTravelMode) mode).getDeparture();
				}
				end = ((TLSTravelMode) mode).getArrival();
			}
		}
		// Calculate the expected duration of the journey
		if(start != null && end != null){
			long diff = end.getTime() - start.getTime();
			journey.setTravelTimeMS(diff);
		}
		
		// Calculate distance
		double distanceKM = 0.0d;
		if(legList.size() > 0){
			Leg prev = legList.get(0);
			for(int i = 1; i < legList.size(); i++){
				distanceKM += GeneralHelper.distKMLocToLoc(prev, legList.get(i));
				prev = legList.get(i);
			}
		}
		
		journey.setDistanceKM(distanceKM);
		
		return journey;	
	}
	
	/**
	 * Parses a node within a route
	 * @param leg
	 * @param numLegs
	 * @return The details of one node within a route
	 */
	public Leg parseLeg(Node leg, int numLegs){		
		Node legBoard = null;
		Node legAlight = null;
		Node trip = null;
		Node mode = null;
		
		// Parse child nodes to determine the locations and mode
		NodeList children = leg.getChildNodes();
		for(int i = 0; i < children.getLength(); i++){
			Node child = children.item(i);
			switch(child.getNodeName()){
			case "LegBoard":
				legBoard = child;
				break;
			case "LegAlight":
				legAlight = child;
				break;
			case "Trip":
				trip = child;
				break;
			case "Mode":
				mode = child;
				break;
			}
		}
		
		// Get board loc
		LatLng boardLoc = null;
		if(legBoard != null){			
			NodeList geocodes = ((Element)legBoard).getElementsByTagName("Geocode");
			if(geocodes.getLength() > 0){
				Node geocode = geocodes.item(0);
				boardLoc = readGeocode(geocode);
			}
		}

		// Get alight loc
		LatLng alightLoc = null;
		if(legAlight != null){			
			NodeList geocodes = ((Element)legAlight).getElementsByTagName("Geocode");
			if(geocodes.getLength() > 0){
				Node geocode = geocodes.item(0);
				alightLoc = readGeocode(geocode);
			}
		}
		
		// Determine the travel mode
		TravelMode travelMode = null;		
		if(trip != null){
			// If there is a trip node then this uses bus/rail
			travelMode = parseTrip(trip);
			
		}else if(mode != null){
			travelMode = new TravelMode()
							.mode(mode.getTextContent());
		}
		
		
		Leg returnVal = new Leg(boardLoc.getLat(), boardLoc.getLng(), 0.0d);
		returnVal.travelMode = travelMode;
		returnVal.setIdentifier("");
		returnVal.to = new Location(alightLoc.getLat(), alightLoc.getLng(), 0.0d);
		returnVal.setDescription("Leg of a journey loaded using "+ this.getClass().getName());
		
		Element legElem = (Element) leg;
		NodeList pathSegs = legElem.getElementsByTagName("PathSegment");
		if(pathSegs.getLength() > 0){
			Node pathSeg = pathSegs.item(0);
			String segId = pathSeg.getAttributes().getNamedItem("SegmentId").getTextContent();
			if(segId.equals("segment_0")){
				// Parse postcode of first leg
				String postcode = legElem.getElementsByTagName("SegmentName").item(0).getTextContent();
				returnVal.setIdentifier(postcode.substring(0, postcode.indexOf(" - ")));
				
			}else if(segId.equals("segment_"+ (numLegs-1))){
				// Parse postcode of final leg
				String postcode = legElem.getElementsByTagName("SegmentName").item(0).getTextContent();
				returnVal.setIdentifier(postcode.substring(postcode.lastIndexOf(" - ") + 3));
			}
		}
		
		return returnVal;
	}
	
	/**
	 * Converts a precision string to a float scalar (0.0 <= x <= 1.0)
	 * 
	 * TODO This needs to handle more precision options (only seen 1cm so far)
	 * @param precision
	 * @return
	 */
	public float stringToFloatPrecision(String precision){
		if(precision.equals("1cm")){
			return 0.01f;
		}
		return 1.0f;
	}
	
	/**
	 * Reads TLS GeoCode information and converts (Easting,Northing) to (Lat,Lng)
	 * @param geocode
	 * @return LatLng location of GeoCode
	 */
	public LatLng readGeocode(Node geocode){
		int easting = 0;
		int northing = 0;
		float precision = 0.0f;

		NodeList geoInfo = geocode.getChildNodes();
		for(int i = 0; i < geoInfo.getLength(); i++){
			Node info = geoInfo.item(i);
			switch(info.getNodeName()){
			case "Easting":
				easting = Integer.parseInt(info.getTextContent());
				break;
			case "Northing":
				northing = Integer.parseInt(info.getTextContent());
				break;
			case "Precision":
				precision = stringToFloatPrecision(info.getTextContent());
				break;
			}
		}
		LatLng latlng = new OSRef(easting * precision, northing * precision).toLatLng();
		latlng.toWGS84();
		return latlng;
	}
	
	/**
	 * Strip out the bus information from the Trip node
	 * @param busTrip
	 * @return The mode, operator, operator code, and service number for bus travel
	 */
	public TLSTravelMode parseTrip(Node busTrip){
		TLSTravelMode travelMode = new TLSTravelMode();
		
		Element busTripElem = (Element) busTrip;
		String mode = busTripElem.getElementsByTagName("Mode").item(0).getTextContent();
		switch(mode.toUpperCase()){
		case "BUS":
			travelMode.setMode("bus");//BusRouting.DF_NAME);
			break;
		case "RAIL":
			travelMode.setMode("rail");//RailRouting.DF_NAME);
			break;
		}
		travelMode.setService(busTripElem.getElementsByTagName("ServiceNumber").item(0).getTextContent());
		travelMode.setOperator(busTripElem.getElementsByTagName("PublicName").item(0).getTextContent());
		travelMode.setOperatorCode(busTripElem.getElementsByTagName("OperatorCode").item(0).getTextContent());
		
		// Get the departure and arrival times
		String departureString = busTripElem.getElementsByTagName("ScheduledDepartureTime").item(0).getTextContent();
		String arrivalString = busTripElem.getElementsByTagName("ScheduledArrivalTime").item(0).getTextContent();
		
		// Parse the date strings
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
		Date departure, arrival;
		try {
			departure = format.parse(departureString);
			travelMode.setDeparture(departure);
			
			arrival = format.parse(arrivalString);
			travelMode.setArrival(arrival);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return travelMode;
	}
	
	
}
