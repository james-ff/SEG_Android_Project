package com.worldly.network;

public class QuerySystem {
	
	//private final String API_BASE_URL = "http://api.worldbank.org/";
	
	private final static String FORMAT_APPEND = "format=json";
	
	private final static String GET_ALL_COUNTRIES_URL = "http://api.worldbank.org/countries";
	private final static String INDICATOR_BASE_URL = "http://api.worldbank.org/indicators/";
	
	public static String getAllCountriesData() {		
		String url = GET_ALL_COUNTRIES_URL + "?" + "per_page=500&" + FORMAT_APPEND;
		return JSONCore.readData(url);
	}
	
	public static String getIndicatorDescription(String indicatorNeeded) {
		String url = INDICATOR_BASE_URL + indicatorNeeded + "?" + FORMAT_APPEND;
		return JSONCore.readData(url);
	}
	
	public static String getIndicatorData(String countryCode, String indicatorCode)
	{
		String url = GET_ALL_COUNTRIES_URL+"/"+countryCode+"/indicators/"+indicatorCode+"?per_page=100&date=2000:2013&format=json";
		return JSONCore.readData(url);
	}
	
	//http://api.worldbank.org/countries/CZ/indicators/BG.GSR.NFSV.GD.ZS?per_page=100&date=2009:2013

}
