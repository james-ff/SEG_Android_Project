package com.worldly.data_store;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.worldly.data_models.Country;

import android.os.Environment;
import android.util.Log;

public class CachingEngine {
	private static final File workingDirectory = new File(Environment.getExternalStorageDirectory() + "/Worldly/");
	
	public static boolean writeCountriesCache(ArrayList<Country> countriesData) {
		try {
			workingDirectory.mkdirs();
			File cacheFile = new File(workingDirectory + "/countries.dat");
			FileOutputStream os = new FileOutputStream(cacheFile);
			os.write(("" + (System.currentTimeMillis() + 86400000 + "\n")).getBytes());
			
			for (int i = 0; i < countriesData.size(); i++) {
				Country current = countriesData.get(i);
				String data = (current.getId() + ", " + current.getName() + ", " + current.getIso2Code() + ", " + current.getCapitalCity() + ", " + current.getLatitude() + ", " + current.getLongitude() + "\n");
				os.write(data.getBytes());
			}
			os.flush();
			os.close();
			return true;
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public static ArrayList<Country> checkCacheForCountries() {
		File cacheFile = new File(workingDirectory + "/countries.dat");
		if (cacheFile.exists()) {
			try {
				FileInputStream is = new FileInputStream(cacheFile);
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				String line = "";
				
				Long expiryMilliseconds = Long.parseLong(reader.readLine());		
				if (System.currentTimeMillis() > expiryMilliseconds) { 
					Log.w("CacheEngine", "Cached countries too old, getting again.");
					reader.close(); is.close();
					return new ArrayList<Country>(); 					
				}
				
				ArrayList<Country> cached = new ArrayList<Country>();
				while ((line = reader.readLine()) != null) {
					cached.add(new Country(line));
				}
				
				Log.i("CacheEngine", "Read " + cached.size() + " countries from cache");
				reader.close(); is.close();
				return cached;
			} 
			catch (Exception ex) {
				return new ArrayList<Country>();
			}
		}
		return new ArrayList<Country>();
	}
}
