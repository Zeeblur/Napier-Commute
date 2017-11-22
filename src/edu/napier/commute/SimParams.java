package edu.napier.commute;

public class SimParams {
/*
 * A class of useful parameters that may be used throughout the simulation
 * Implemented as a Singleton
 */
	
	//Singleton bits
	private SimParams() {
	}
	
	private static SimParams me = null;
	
	public static SimParams getInstance() {
		if (me==null)
			me = new SimParams();
		
		return me;
	}
	
	
	/*
	 * 
	 * General Params
	 */
	private int days;//The number of days the simulation should run for
	private String osmFile; //OSM file for all OSM networks
	private String carDirectory;//Folder used to store OSM data for cars
	private String bikeDirectory;//Folder used to store OSM data for bikes
	private String walkDirectory; //Folder used to store OSM data for walk
	private int defaultCarParkSize;//Workplace parking capacity
	private int defaultBikeParkSize;//Workplace parking capacity
	
	public enum direction  {IN, OUT};//Used to designate journeys IN to work or OUT from work
	
	
	/*
	 * Emission costs per kilometre
	 * 
	 * 		Defaults values sourced from: 'http://www.aef.org.uk/downloads/Grams_CO2_transportmodesUK.pdf'
	 * 
	 */
	
	public int getDays() {
		return days;
	}




	public void setDays(int days) {
		this.days = days;
	}




	public String getOsmFile() {
		return osmFile;
	}




	public void setOsmFile(String osmFile) {
		this.osmFile = osmFile;
	}




	public String getCarDirectory() {
		return carDirectory;
	}




	public void setCarDirectory(String carDirectory) {
		this.carDirectory = carDirectory;
	}




	public String getBikeDirectory() {
		return bikeDirectory;
	}




	public void setBikeDirectory(String bikeDirectory) {
		this.bikeDirectory = bikeDirectory;
	}




	public String getWalkDirectory() {
		return walkDirectory;
	}




	public void setWalkDirectory(String walkDirectory) {
		this.walkDirectory = walkDirectory;
	}




	public int getDefaultCarParkSize() {
		return defaultCarParkSize;
	}




	public void setDefaultCarParkSize(int defaultCarParkSize) {
		this.defaultCarParkSize = defaultCarParkSize;
	}




	public int getDefaultBikeParkSize() {
		return defaultBikeParkSize;
	}




	public void setDefaultBikeParkSize(int defaultBikeParkSize) {
		this.defaultBikeParkSize = defaultBikeParkSize;
	}




	// Emissions Small Car
	private float emSmallCarOnePass = 0.1276f;
	private float emSmallCarTwoPass = 0.063f;
	
	// Emissions Large Car
	private float emLargeCarOnePass = 0.257f;
	private float emLargeCarTwoPass = 0.1288f;	
	private float emLargeCarFourPass = 0.064f;
	
	// Emissions Bus
	private float emBus = 0.089f;
	
	// Emissions Train
	private float emTrain = 0.06f;
	
	
	//Emissions Bike
	private float emBike = 0.00f;
	
	//Emissions Walk
	private float emWalk = 0.00f;
	
	/*
	 * Monetary cost pet kilometre
	 * TODO update this with more precise values for each mode of transport
	 *  Defaults based on Expenses per mile at: http://www.theaa.com/driving/mileage-calculator.jsp
	 */
	// Cost Small Car
	private float costSmallCarOnePass = 0.087f;
	private float costSmallCarTwoPass = 0.087f;
	
	// Cost Large Car
	private float costLargeCarOnePass = 0.087f;
	private float costLargeCarTwoPass = 0.087f;
	private float costLargeCarFourPass = 0.087f;
	
	// Cost Bus
	private float costBusDayTicket = 4.00f;
	
	// Cost Rail
	private float costTrainTicket = 0.00f;//Need data!!!
	
	//Cost Bike
	private float costBike = 0.00f;
	
	//Cost Walk
	private float costWalk = 0.00f;
	
	public float getCostTrainTicket() {
		return costTrainTicket;
	}




	public void setCostTrainTicket(float costTrainTicket) {
		this.costTrainTicket = costTrainTicket;
	}


	/*
	 * Emissions getters/setters
	 */

	public float getEmSmallCarOnePass() {
		return emSmallCarOnePass;
	}
	public void setEmSmallCarOnePass(float emSmallCarOnePass) {
		this.emSmallCarOnePass = emSmallCarOnePass;
	}

	public float getEmSmallCarTwoPass() {
		return emSmallCarTwoPass;
	}
	public void setEmSmallCarTwoPass(float emSmallCarTwoPass) {
		this.emSmallCarTwoPass = emSmallCarTwoPass;
	}

	public float getEmLargeCarOnePass() {
		return emLargeCarOnePass;
	}
	public void setEmLargeCarOnePass(float emLargeCarOnePass) {
		this.emLargeCarOnePass = emLargeCarOnePass;
	}

	public float getEmLargeCarTwoPass() {
		return emLargeCarTwoPass;
	}
	public void setEmLargeCarTwoPass(float emLargeCarTwoPass) {
		this.emLargeCarTwoPass = emLargeCarTwoPass;
	}

	public float getEmLargeCarFourPass() {
		return emLargeCarFourPass;
	}
	public void setEmLargeCarFourPass(float emLargeCarFourPass) {
		this.emLargeCarFourPass = emLargeCarFourPass;
	}

	public float getEmBus() {
		return emBus;
	}
	public void setEmBus(float emBus) {
		this.emBus = emBus;
	}

	public float getEmTrain(){
		return emTrain;
	}
	public void setEmTrain(float emTrain) {
		this.emTrain = emTrain;
	}



	public float getEmBike() {
		return emBike;
	}




	public void setEmBike(float emBike) {
		this.emBike = emBike;
	}




	public float getEmWalk() {
		return emWalk;
	}




	public void setEmWalk(float emWalk) {
		this.emWalk = emWalk;
	}




	/*
	 * Cost getters/setters
	 */
	
	public float getCostSmallCarOnePass() {
		return costSmallCarOnePass;
	}
	public void setCostSmallCarOnePass(float costSmallCarOnePass) {
		this.costSmallCarOnePass = costSmallCarOnePass;
	}
	
	public float getCostSmallCarTwoPass() {
		return costSmallCarTwoPass;
	}
	public void setCostSmallCarTwoPass(float costSmallCarTwoPass) {
		this.costSmallCarTwoPass = costSmallCarTwoPass;
	}
	
	public float getCostLargeCarOnePass() {
		return costLargeCarOnePass;
	}
	public void setCostLargeCarOnePass(float costLargeCarOnePass) {
		this.costLargeCarOnePass = costLargeCarOnePass;
	}
	
	public float getCostLargeCarTwoPass() {
		return costLargeCarTwoPass;
	}
	public void setCostLargeCarTwoPass(float costLargeCarTwoPass) {
		this.costLargeCarTwoPass = costLargeCarTwoPass;
	}
	
	public float getCostLargeCarFourPass() {
		return costLargeCarFourPass;
	}
	public void setCostLargeCarFourPass(float costLargeCarFourPass) {
		this.costLargeCarFourPass = costLargeCarFourPass;
	}
	
	public float getCostBusDayTicket() {
		return costBusDayTicket;
	}
	public void setCostBusDayTicket(float costBusDayTicket) {
		this.costBusDayTicket = costBusDayTicket;
	}	
	


	public float getCostBike() {
		return costBike;
	}




	public void setCostBike(float costBike) {
		this.costBike = costBike;
	}




	public float getCostWalk() {
		return costWalk;
	}




	public void setCostWalk(float costWalk) {
		this.costWalk = costWalk;
	}
}

