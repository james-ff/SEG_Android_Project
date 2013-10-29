package com.worldly.network;


public class QuerySystem {
	
	//private final String API_BASE_URL = "http://api.worldbank.org/";
	private final static String GET_ALL_COUNTRIES_URL = "http://api.worldbank.org/countries?per_page=500&format=json";
	
	public static String fetchAllCountriesData() {
		
		return JSONCore.readData(GET_ALL_COUNTRIES_URL);
	}

}
