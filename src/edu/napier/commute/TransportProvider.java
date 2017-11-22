package edu.napier.commute;

import java.util.ArrayList;
import java.util.List;

import edu.napier.geo.common.Journey;

/*
 * An abstract class for a transport provider
 * The transport provider takes a basic journey type as a request and return the option for travel by this provider
 */
public abstract class TransportProvider {
	
	abstract void setup();
	abstract ArrayList<CJourney> getOptions(CJourney request) ;
}
