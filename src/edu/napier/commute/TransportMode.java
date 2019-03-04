package edu.napier.commute;

import java.util.HashMap;
import java.util.Map;

public enum TransportMode{CAR("car", 0),BUS("bus", 1),RAIL("rail", 2),BIKE("bike", 3),WALK("walk", 4);
	
	// Member to hold the name
	   private String name;
	   
	   // member to hold the value
	   private int value;
	   
	   
	   // map to hold mapping of numbers to enum
	   private static Map map = new HashMap<>();

	   // constructor to set the string
	   TransportMode(String nm, int val)
	   {
		   this.name = nm;
		   this.value = val;
	   }
	   
	   // maps values to modes
	   static
	   {
	        for (TransportMode mode : TransportMode.values())
	        {
	            map.put(mode.value, mode);
	        }
	    }
	   

	   // the toString just returns the given name
	   @Override
	   public String toString() {
	       return name;
	   }
	   
	   // returns enum var of a given value
	   public static TransportMode valueOf(int type)
	   {
		   return (TransportMode) map.get(type);
	   }
	   
	   public int getValue()
	   {
		   return value;
	   }
}; 