package com.worldly.data_store;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.worldly.data_models.Indicator;
import com.worldly.data_models.UnloadedIndicatorDescription;
import com.worldly.network.QuerySystem;

/**
 * This class works as a data storage for the indicators that will be presented to user on
 * selecting a country. This should be properly cached and stored so to avoid increased
 * traffic usage.
 * 
 * @author Annie the Eagle & Team
 * 
 */
public class ListOfIndicators {
	
	public static final String CATEGORY_BUSINESS = "Business";
	public static final String CATEGORY_CITY_LIFE = "City Life";
	public static final String CATEGORY_CLIMATE = "Climate";
	public static final String CATEGORY_DEMOGRAPHICS = "Demographics";
	public static final String CATEGORY_EDUCATION = "Education";
	public static final String CATEGORY_EMPLOYMENT_PROSPECTS = "Employment Prospects";
	public static final String CATEGORY_FINANCE = "Finance";
	public static final String CATEGORY_QUALITY_OF_LIFE = "Quality of Life";
	public static final String CATEGORY_RURAL_LIFE = "Rural Life";
	
	private static ArrayList<Indicator> loadedIndicators = new ArrayList<Indicator>();
	private static HashMap<String, ArrayList<UnloadedIndicatorDescription>> droneStrikes = new HashMap<String, ArrayList<UnloadedIndicatorDescription>>();
	
	public ListOfIndicators() {
		// Category Setup -- These categories will be the headings of the expandable groups (use droneStrikes.keySet())
		droneStrikes.put(CATEGORY_BUSINESS, new ArrayList<UnloadedIndicatorDescription>());
		droneStrikes.put(CATEGORY_CITY_LIFE, new ArrayList<UnloadedIndicatorDescription>());
		droneStrikes.put(CATEGORY_CLIMATE, new ArrayList<UnloadedIndicatorDescription>());
		droneStrikes.put(CATEGORY_DEMOGRAPHICS, new ArrayList<UnloadedIndicatorDescription>());
		droneStrikes.put(CATEGORY_EDUCATION, new ArrayList<UnloadedIndicatorDescription>());
		droneStrikes.put(CATEGORY_EMPLOYMENT_PROSPECTS, new ArrayList<UnloadedIndicatorDescription>());
		droneStrikes.put(CATEGORY_FINANCE, new ArrayList<UnloadedIndicatorDescription>());
		droneStrikes.put(CATEGORY_QUALITY_OF_LIFE, new ArrayList<UnloadedIndicatorDescription>());
		droneStrikes.put(CATEGORY_RURAL_LIFE, new ArrayList<UnloadedIndicatorDescription>());
		
		// TODO: Remove
		//droneStrikes.put("Military", new ArrayList<UnloadedIndicatorDescription>());
		
		/* -- Indicators Setup -- Here we add the known codes of indicators we are interested in. -- */ 
		ArrayList<UnloadedIndicatorDescription> reference = droneStrikes.get(CATEGORY_BUSINESS);
		reference.add(new UnloadedIndicatorDescription("GDP growth (Annual %)", "NY.GDP.MKTP.KD.ZG")); 
		reference.add(new UnloadedIndicatorDescription("Listed domestic companies", "CM.MKT.LDOM.NO"));
		reference = droneStrikes.get(CATEGORY_CITY_LIFE);
		reference.add(new UnloadedIndicatorDescription("Health expendiure per capita (US$)", "SH.XPD.PCAP"));
		reference.add(new UnloadedIndicatorDescription("% of urban populating with access to improved water", "SH.H2O.SAFE.UR.ZS"));
		reference = droneStrikes.get(CATEGORY_CLIMATE);
		reference.add(new UnloadedIndicatorDescription("CO2 emissions in kilotons", "EN.ATM.CO2E.KT"));
		reference.add(new UnloadedIndicatorDescription("Methane emissions in kilotons of CO2 equivalent", "EN.ATM.METH.KT.CE"));
		reference = droneStrikes.get(CATEGORY_DEMOGRAPHICS);
		reference.add(new UnloadedIndicatorDescription("Total Population", "SP.POP.TOTL"));
		reference.add(new UnloadedIndicatorDescription("Net migration", "SM.POP.NETM"));
		
		// TODO: Remove
		//reference = droneStrikes.get("Military");
		//reference.add(new UnloadedIndicatorDescription("Military expenditure (% of GDP)", "MS.MIL.XPND.GD.ZS"));
	}
	
	public static boolean addIndicator(String category, String code) {
		try { 
			String data = QuerySystem.getIndicatorDescription(code);
			loadedIndicators.add(new Indicator(category, new JSONArray(data)));
			return true; 
		}
		catch (JSONException e) {
			Log.e("IndicatorGrab", "An error occured adding indicator with code: " + code);
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean loadIndicatorsForCategory(final String category) {
		final ArrayList<UnloadedIndicatorDescription> codes = droneStrikes.get(category);
		if (codes == null || codes.size() == 0) {
			Log.w("IndicatorLoad", "No indicators are present for category name: " + category);
			return false;
		}
		else {
			Thread aThread = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < codes.size(); i++) { addIndicator(category, codes.get(i).getCode()); }
				}
			});
			aThread.start();
			return true;
		}
	}
	
	public static ArrayList<Indicator> getAllLoadedIndicatorsFromCategory(String category) {
		ArrayList<Indicator> returns = new ArrayList<Indicator>();
		for (int i = 0; i < loadedIndicators.size(); i++) {
			if (loadedIndicators.get(i).getCategory().equals(category)) {
				returns.add(loadedIndicators.get(i));
			}
		}
		if (returns.size() == 0) { Log.w("IndicatorGrab", "Found no indicators with category: " + category); }
		return returns;
	}
	
	public static ArrayList<String> getCategories() {
		return new ArrayList<String>(droneStrikes.keySet());
	}
	
	public static String[] getCategoriesAsArray() {
		return new String[]{CATEGORY_BUSINESS, CATEGORY_CITY_LIFE, 
							CATEGORY_CLIMATE, CATEGORY_DEMOGRAPHICS, 
							CATEGORY_EDUCATION, CATEGORY_EMPLOYMENT_PROSPECTS,
							CATEGORY_FINANCE, CATEGORY_QUALITY_OF_LIFE,
							CATEGORY_RURAL_LIFE};
	}
	
	public static ArrayList<String> getReadableNamesOfIndicatorsInCategory(String category) {
		ArrayList<String> returner = new ArrayList<String>();
		for (int i = 0; i < droneStrikes.get(category).size(); i++) {
			returner.add(droneStrikes.get(category).get(i).getName());
		}
		return returner;
	}
}
