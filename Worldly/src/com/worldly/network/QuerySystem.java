package com.worldly.network;


public class QuerySystem {
	
	//private final String API_BASE_URL = "http://api.worldbank.org/";
	
	private final static String FORMAT_APPEND = "format=json";
	private final static String GET_ALL_COUNTRIES_URL = "http://api.worldbank.org/countries?per_page=500";
	private final static String INDICATOR_BASE_URL = "http://api.worldbank.org/indicators/";
	
	public static String getAllCountriesData() {		
		String url = GET_ALL_COUNTRIES_URL + "&" + FORMAT_APPEND;
		return JSONCore.readData(url);
	}
	
	public static String getIndicatorData(String indicatorNeeded) {
		String url = INDICATOR_BASE_URL + indicatorNeeded + "?" + FORMAT_APPEND;
		
		return JSONCore.readData(url);
	}

}
