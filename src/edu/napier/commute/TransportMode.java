package edu.napier.commute;

public enum TransportMode{CAR("car"),BUS("bus"),RAIL("rail"),BIKE("bike"),WALK("walk");
	
	// Member to hold the name
	   private String string;

	   // constructor to set the string
	   TransportMode(String name){string = name;}

	   // the toString just returns the given name
	   @Override
	   public String toString() {
	       return string;
	   }
}; 