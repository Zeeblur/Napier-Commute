package edu.napier.commute;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import edu.napier.geo.common.Location;

/*
 * This class converts postcodes into lat/lon coordinates
 */

public class GeoCoder {
	private static HashMap <String, Location> cache;
	
	private static void init() {
		// Create Cache
		
		try {
			FileInputStream fis = new FileInputStream("./postCodeCache.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			cache = (HashMap<String, Location>) ois.readObject();

			System.out.println("GeoCoder: Successfully read cache file.");

			ois.close();

		} catch (Exception e) {
			System.out.println("GeoCoder: No valid cache file, starting with empty cache.");
			cache = new HashMap<String,Location>();
		}
	}

	public static Location find(String postCode) {
		if (cache == null)
			init();

		Location res = cache.get(postCode);

		if (res != null)
			return res;


		res = get(postCode);
		cache.put(postCode, res);

		FileOutputStream fos;
		try {
			fos = new FileOutputStream("./postCodeCache.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			Map<String, Location> mapToSave = new HashMap<String, Location>();

			oos.writeObject(mapToSave);
			oos.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}

		return res;
	}

	private static Location get(String postCode) {
		postCode = postCode.trim();
		postCode = postCode.toUpperCase();
		String area = "";// postCode.substring(0,2);
		for (int x = 0; x < postCode.length(); x++) {
			if (Character.isLetter(postCode.charAt(x)))
				area = area + postCode.charAt(x);
			else
				x = postCode.length();
		}
		// Fix for London codes
		// if
		// (area.equals("HA")||area.equals("RM")||area.equals("CR")||area.equals("EN")||area.equals("IG")||area.equals("BR")||area.equals("SM")||area.equals("SE")||area.equals("SW")||area.equals("W")||area.equals("WC")||area.equals("EC")||area.equals("E")||area.equals("N")||area.equals("NW"))
		// {area = "London";}
		try {
			BufferedReader reader = new BufferedReader(new FileReader("./postcodes/" + area + ".csv"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.contains(postCode)) {
					// Loc res = new Loc();
					String[] items = line.split(",");

					Location res = new Location(Double.parseDouble(items[1]), Double.parseDouble(items[2]));
					
					res.setDescription(postCode);
					res.setSource("Loaded from file using 'GeoCoder.java'. File: ./postcodes/" + area + ".csv");

					reader.close();
					return res;
				}
			}
			reader.close();
			System.err.println("GeoCoder: Postcode not found " + postCode);
			System.exit(-1);
			return null;
		} catch (Exception e) {
			System.err.println("GeoCoder: Postcode not found " + postCode);
			System.exit(-1);
			return null;
		}
	}

}

