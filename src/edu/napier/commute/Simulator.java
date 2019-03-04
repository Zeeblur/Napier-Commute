package edu.napier.commute;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import edu.napier.geo.common.Journey;

public class Simulator {

	/*
	 * 
	 * This class runs the simulation
	 */
	private static ArrayList<Commuter> commuters = new ArrayList<Commuter>();
	
	public static void init() {
		TransportManager.init();
	}
	
	public static void addCommuter(Commuter newCommuter) {
	  commuters.add(newCommuter);
	}
	
	
	public static void run() {
		//Main simulation loop
		System.out.println("Running simulator");
		
		for (int day=0; day < SimParams.getInstance().getDays(); day++) {
			System.out.println("Simulating day " + day);
			//simulators.reset
			TransportManager.newDay();
			
			for(Commuter commute : commuters) {
				commute.selectTravelOption(day);
			}
			//Generate feedback on the days activities
			for(Commuter commute : commuters) 
				commute.clearFeedback();
			TransportManager.generateFeedback();
			writeResults(day);
		}	
	}
	
	private static int logValue = 0;
	
	public static void writeResults(int day) {
		BufferedWriter bw = null;
		FileWriter fw = null;
/*<<<<<<< HEAD
		
		try {

			if(day==0)
			fw = new FileWriter(SimParams.getInstance().getOutFile(),false);
			else
				fw = new FileWriter(SimParams.getInstance().getOutFile(),true);
=======*/

		try
		{
			String fileName = SimParams.getInstance().getOutFile();
			if (fileName.equals(""))
				fileName = "SimLog";
			
			String ext = ".csv";
            
			
			if(day==0)
			{

				// does the file already exist? if so, increment value
				File temp = new File(fileName + logValue + ext);
					
				while (temp.isFile())
				{
					logValue++;
					temp = new File(fileName + logValue + ext);
				}
				
				fw = new FileWriter(fileName+logValue+ext,false);
			}
			else
			{
				fw = new FileWriter(fileName+logValue+ext,true);
			}
//>>>>>>> a21e2ffc5aadd6c3a4c6b3da043494e3450ad748
			
			bw = new BufferedWriter(fw);
			if (day == 0) {//Write header
				bw.write("id,descriptor,Home,DepTime,mode,cost,dist,emissions,time(mins),Work,DepTime,mode,cost,dist,emissions,time(mins)\n") ;
					
			
			}
			bw.write("Day,"+ day +"\n");
			int total=0;
			HashMap<TransportMode,Integer> modeCount = new HashMap<TransportMode, Integer>();
			
			for (Commuter c : commuters) {
				TransportMode mode = c.getModeIn();
				if(modeCount.containsKey(mode)) {
					Integer count = modeCount.get(mode);
					count++;
					modeCount.put(mode, count);
					total++;
				}else {
					modeCount.put(mode, new Integer(1));
				}
				
				bw.write(c.getResultCSV());
			}

			bw.write("Summary\n");
			for(TransportMode m : modeCount.keySet()) {
				int c = modeCount.get(m);
				double avg = (double) c/total;
				bw.write(m+","+c +","+avg+"\n");
			}
				
			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}
}
