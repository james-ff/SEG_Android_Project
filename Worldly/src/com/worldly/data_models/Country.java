package com.worldly.data_models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.worldly.network.QuerySystem;

public class Country {
	
	private final String NAME_KEY = "name";
	private final String ID_KEY = "id";
	private final String LONGITUDE_KEY = "longitude";
	private final String LATITUDE_KEY = "latitude";
	private final String CAPITAL_CITY_KEY = "capitalCity";
	private final String ISO_2_CODE_KEY = "iso2Code";
	
	private String name;
	private String id;
	private String iso2Code;
	private String capitalCity;
	private String longitude;
	private String latitude;
	
	
	public Country(JSONObject rawData) {
		super();
		
		try {
			this.name = rawData.getString(NAME_KEY);
			this.id = rawData.getString(ID_KEY);
			this.iso2Code = rawData.getString(ISO_2_CODE_KEY);
			this.capitalCity = rawData.getString(CAPITAL_CITY_KEY);
			this.longitude = rawData.getString(LONGITUDE_KEY);
			this.latitude = rawData.getString(LATITUDE_KEY);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Country> getAllCountries() throws JSONException {
		
		String queryData = QuerySystem.fetchAllCountriesData();
		ArrayList<Country> countries = new ArrayList<Country>();
		
		JSONArray rootArray = new JSONArray(queryData);
		JSONArray allCrountriesRaw = rootArray.getJSONArray(1);
		
		for (int n = 0; n < allCrountriesRaw.length(); n++) {
			countries.add(new Country(allCrountriesRaw.getJSONObject(n)));
		}
		
		return countries;
	}
	
	public void print() {
		Log.v(this.getClass().getName(), "Name:" + this.name + "  ID:" + this.id + "  iso 2 code:" + this.iso2Code + "  Capital City:" + this.capitalCity + "  Longitude + Latitutde:" + this.longitude + " | " + this.latitude);
	}

	/**
	 * Member Getters and Setters
	 */
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIso2Code() {
		return iso2Code;
	}

	public void setIso2Code(String iso2Code) {
		this.iso2Code = iso2Code;
	}

	public String getCapitalCity() {
		return capitalCity;
	}

	public void setCapitalCity(String capitalCity) {
		this.capitalCity = capitalCity;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
}
