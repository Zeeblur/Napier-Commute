package edu.napier.commute.tls;

import edu.napier.geo.common.Location;


public class GeneralHelper {



	public static double distKMLocToLoc(Location locA, Location locB){
		double R = 6371.0d; // metres
		
		double degToRad = (Math.PI / 180.0f);
		
		double aLatRad = locA.getLat() * degToRad;
		double aLonRad = locA.getLon() * degToRad;
		
		double bLatRad = locB.getLat() * degToRad;
		double bLonRad = locB.getLon() * degToRad;
		double deltaLat = bLatRad - aLatRad;
		double deltaLon = bLonRad - aLonRad;

		double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2)
	          + Math.cos(aLatRad) * Math.cos(bLatRad)
	          * Math.sin(deltaLon/2) * Math.sin(deltaLon/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    
	    return R * c;
	}
	
}
