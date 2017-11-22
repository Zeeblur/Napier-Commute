package edu.napier.commute;

import edu.napier.geo.common.Journey;

public abstract class TransportSimulator {
	abstract  void newDay();
	abstract void addJourney(Commuter i, CJourney j, SimParams.direction dir);
	abstract void setFeedback();
	

}
