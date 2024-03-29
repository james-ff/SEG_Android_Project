package com.worldly.data_models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.worldly.data_store.CachingEngine;
import com.worldly.network.QuerySystem;

/**
 * This class represents a single country, it contains various fields which
 * represent the country's properties, there are also getter and setter methods
 * for all fields.
 * 
 * @author James Bellamy & Team
 * 
 */
public class Country {
	/**
	 * Constant representing the key to obtain the respective value from the
	 * JSON raw data.
	 */
	private final String NAME_KEY = "name", ID_KEY = "id",
			LONGITUDE_KEY = "longitude", LATITUDE_KEY = "latitude",
			CAPITAL_CITY_KEY = "capitalCity", ISO_2_CODE_KEY = "iso2Code";
	
	

	private String name;
	private String id;
	private String iso2Code;
	private String capitalCity;
	private Double longitude;
	private Double latitude;
	private Map<Indicator, IndicatorDataBlock> countryValues = new HashMap<Indicator, IndicatorDataBlock>();

	/**
	 * Constructs a new Country object with the specified JSON raw data.
	 * 
	 * @param rawData
	 *            : A JSONObject containing raw data about a country.
	 */
	public Country(JSONObject rawData) {
		super();

		try
		// Attempts to parse rawData
		{
			this.name = rawData.getString(NAME_KEY);
			this.id = rawData.getString(ID_KEY);
			this.iso2Code = rawData.getString(ISO_2_CODE_KEY);
			this.capitalCity = rawData.getString(CAPITAL_CITY_KEY);
			
			try {
				this.longitude = Double.parseDouble(rawData.getString(LONGITUDE_KEY));
			} catch (NumberFormatException e) {
				this.longitude = null;
			}
			
			try {
				this.latitude = Double.parseDouble(rawData.getString(LATITUDE_KEY));
			} catch (NumberFormatException e) {
				this.latitude = null;
			}
			
		} 
		catch (JSONException e) { // Encountered a problem whilst parsing rawData
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructs a new Country object with the specified line from the cache file.
	 * @param cacheLine
	 */
	public Country(String cacheLine) {
		super();
	
		// Places the country data into a String array
		String[] countryParts = cacheLine.split(", ");
		
		// Attempts to parse the country data
		try {
			this.name = countryParts[1];
			this.id = countryParts[0];
			this.iso2Code = countryParts[2];
			this.capitalCity = countryParts[3];
			
			try { this.longitude = Double.parseDouble(countryParts[5]); } 
			catch (NumberFormatException e) { this.longitude = null; }
			
			try { this.latitude = Double.parseDouble(countryParts[4]); } 
			catch (NumberFormatException e) { this.latitude = null; }
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Retrieves an ArrayList of Country Objects containing all countries from
	 * the World Bank feed.
	 * 
	 * @return An ArrayList of Country objects containing all countries from the
	 *         World Bank feed.
	 * @throws JSONException
	 *             If there was a problem whilst parsing queryData.
	 */
	public static List<Country> getAllCountries() throws JSONException {
		// Fetching the data from the World Bank feed
		List<Country> countries = CachingEngine.getCachedCountries();
		//ArrayList<Country> countries = new ArrayList<Country>();
		
		if (countries.size() == 0) {
			// Parsing the data onto JSONArray objects
			JSONArray rootArray = new JSONArray(QuerySystem.getAllCountriesData());
			JSONArray allCountriesRaw = rootArray.getJSONArray(1);
	
			// Iterates through allCountriesRaw
			for (int n = 0; n < allCountriesRaw.length(); n++) {
				// Creates and adds a Country object to the ArrayList of countries
				Country aCountry = new Country(allCountriesRaw.getJSONObject(n));
				if (aCountry.longitude != null && aCountry.latitude != null) {
					countries.add(aCountry);
				}
			}
			CachingEngine.writeCountriesCache(countries); // Cache response for 1 day.
		}
		return countries;
	}
	
	/**
	 * Prints a Log of current instantiated Country Object
	 */
	public void print() {
		 Log.v(this.getClass().getName(), "Name:" + this.name + "  ID:" +
		 this.id + "  iso 2 code:" + this.iso2Code + "  Capital City:" +
		 this.capitalCity + "  Longitude + Latitutde:" + this.longitude +
		 " | " + this.latitude);
	}
	
	
	
	@Override
	public String toString() {
		return this.getIso2Code() + " - " + this.getName();
	}

	/**
	 * Retrieves the name of the Country object.
	 * 
	 * @return A String containing the name of the Country object.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retrieves the ID of the Country object.
	 * 
	 * @return A String containing the ID of the Country object.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Retrieves the country code of the Country object.
	 * 
	 * @return A String containing the country code of the Country object.
	 */
	public String getIso2Code() {
		return iso2Code;
	}

	/**
	 * Retrieves the capital city of the Country object.
	 * 
	 * @return A String containing the capital city of the Country object.
	 */
	public String getCapitalCity() {
		return capitalCity;
	}

	/**
	 * Retrieves the longitude of the Country object.
	 * 
	 * @return A String containing the longitude of the Country object.
	 */
	public Double getLongitude() {
		return this.longitude;
	}

	/**
	 * Retrieves the latitude of the Country object.
	 * 
	 * @return A String containing the latitude of the Country object.
	 */
	public Double getLatitude() {
		return this.latitude;
	}

	public void addDataElement(Indicator regarding, IndicatorDataBlock data){ countryValues.put(regarding, data); }
	public void addDataElementByYear(Indicator regarding, int year, Object data) { countryValues.get(regarding).addDataByYear(year, data); }
	public void editDataElementByYear(Indicator regarding, int year, Object newValue) { countryValues.get(regarding).modifyDataInYear(year, newValue); }
	public void deleteDataElementByYear(Indicator regarding, int year) { countryValues.get(regarding).removeDataByYear(year); }
	public Object getDataElementByYear(Indicator regarding, int year) { return countryValues.get(regarding).getDataByYear(year); }
}
