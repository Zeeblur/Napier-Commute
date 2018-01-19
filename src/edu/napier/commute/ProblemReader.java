package edu.napier.commute;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;

public class ProblemReader {
	public ProblemReader(String filename) {
		 try {

	            File f = new File(filename);

	            BufferedReader b = new BufferedReader(new FileReader(f));

	            String readLine = "";
	            int id =0;
	            boolean employeesSection = false;

	            while ((readLine = b.readLine()) != null) {
	            
	            	if (employeesSection) {
	            		String data[] = readLine.split(",");
	            		id ++;
	            		
	            		// addcatch for ,,,
	            		if (data.length == 0)
	            			continue;
	            		
	            		String desc = data[0];
	            		String strHome = data[1];
	            		String strWork= data[2];
	            		LocalTime depHome = LocalTime.parse(data[3]);
	            		LocalTime depWork = LocalTime.parse(data[4]);
	            		Commuter c  = new SimpleBDICommuter(id, desc, strHome, strWork,  depHome, depWork);
	            		//Commuter c  = new RndCommuter(id, desc, strHome, strWork,  depHome, depWork);
	            		
	            		String travelModes = data[9];
	            		
	            		if (travelModes.contains("Car"))
	            			c.addTransportMode(TransportMode.CAR);
	            		if (travelModes.contains("Bus"))
	            			c.addTransportMode(TransportMode.BUS);
	            		if (travelModes.contains("Rail"))
	            			c.addTransportMode(TransportMode.RAIL);
	            		if (travelModes.contains("Cycle"))
	            			c.addTransportMode(TransportMode.BIKE);
	            		if (travelModes.contains("Walk"))
	            			c.addTransportMode(TransportMode.WALK);
	            		
	            		Simulator.addCommuter(c);
	            	}
	            	if (readLine.startsWith("Employee,Home,Work,Start,End,Days,Type,canShareRoute,canJoinRoute,RoutingOptions"))
	            		employeesSection = true;


	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		
	}

}
