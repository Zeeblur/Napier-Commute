package edu.napier.commute;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
			TransportManager.generateFeedback();
			
			writeResults(day);
		}	
	}
	
	private static int logValue = 0;
	
	public static void writeResults(int day) {
		BufferedWriter bw = null;
		FileWriter fw = null;

		try
		{
			String fileName = "SimLog";
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
			
			bw = new BufferedWriter(fw);
			if (day == 0) {//Write header
				bw.write("id,descriptor,Home,DepTime,mode,cost,dist,emissions,time(mins),Work,DepTime,mode,cost,dist,emissions,time(mins)\n") ;
					
			
			}
			bw.write("Day,"+ day +"\n");
			for (Commuter c : commuters) {
				bw.write(c.getResultCSV());
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
