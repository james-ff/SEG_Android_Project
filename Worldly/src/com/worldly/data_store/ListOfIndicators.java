package com.worldly.data_store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.worldly.data_models.Indicator;
import com.worldly.network.QuerySystem;

/**
 * This class works as a data storage for the indicators that will be presented to user on
 * selecting a country. This should be properly cached and stored so to avoid increased
 * traffic usage.
 * 
 * @author Ferdinand Keller & Team
 * 
 */
public class ListOfIndicators {
	
	/**
	 * Constant representing the indicator categories.
	 */
	public static final String CATEGORY_BUSINESS = "Business"
	, CATEGORY_CITY_LIFE = "City Life"
	, CATEGORY_CLIMATE = "Climate"
	, CATEGORY_DEMOGRAPHICS = "Demographics"
	, CATEGORY_EDUCATION = "Education"
	, CATEGORY_EMPLOYMENT_PROSPECTS = "Employment Prospects"
	, CATEGORY_FINANCE = "Finance"
	, CATEGORY_QUALITY_OF_LIFE = "Quality of Life"
	, CATEGORY_RURAL_LIFE = "Rural Life";
	
	/**
	 * Static List holding the loaded indicators' data.
	 */
	private static List<Indicator> loadedIndicators = new ArrayList<Indicator>();
	
	/**
	 * Static Map containing the indicator categories, subcategories, indicator readable names and indicator codes.
	 */
	private static Map<String, Map<String, String>> categories = new HashMap<String, Map<String, String>>();
	
	public ListOfIndicators() {
		// Category Setup -- These categories will be the headings of the expandable groups (use droneStrikes.keySet())
		categories.put(CATEGORY_BUSINESS, new HashMap<String, String>());
		categories.put(CATEGORY_CITY_LIFE, new HashMap<String, String>());
		categories.put(CATEGORY_CLIMATE, new HashMap<String, String>());
		categories.put(CATEGORY_DEMOGRAPHICS, new HashMap<String, String>());
		categories.put(CATEGORY_EDUCATION, new HashMap<String, String>());
		categories.put(CATEGORY_EMPLOYMENT_PROSPECTS, new HashMap<String, String>());
		categories.put(CATEGORY_FINANCE, new HashMap<String, String>());
		categories.put(CATEGORY_QUALITY_OF_LIFE, new HashMap<String, String>());
		categories.put(CATEGORY_RURAL_LIFE, new HashMap<String, String>());
		
		/* -- Indicators Setup -- Here we add the known codes of indicators we are interested in. -- */ 
		Map<String, String> reference = categories.get(CATEGORY_BUSINESS);
		reference.put("NY.GDP.MKTP.KD.ZG", "GDP growth (Annual %)");
		reference.put("CM.MKT.LDOM.NO", "Listed domestic companies");
		reference.put("IC.BUS.EASE.XQ", "Ease of doing business index (1=easiest)");
		reference.put("IC.LGL.CRED.XQ", "Strength of legal rights index (0=weak to 10=strong)");
		reference.put("SL.TLF.TOTL.IN", "Labor Force");
		reference.put("IC.TAX.TOTL.CP.ZS", "Total tax rate (% of commercial profits)");
		reference = categories.get(CATEGORY_CITY_LIFE);
		reference.put("SH.XPD.PCAP", "Health expenditure per capita (current US$)");
		reference.put("SH.H2O.SAFE.UR.ZS", "% of urban population with access to improved water source");
		reference.put("SH.SDA.ACSN.UR", "% of urban population with access to improved Sanitation Facilities");
		reference.put("IS.VEH.ROAD.K1", "Vehicles per km of road");
		reference = categories.get(CATEGORY_CLIMATE);
		reference.put("EN.ATM.CO2E.KT", "CO2 emissions in kilotons");
		reference.put("EN.ATM.METH.KT.CE", "Methane emissions in kilotons of CO2 equivalent");
		reference.put("EN.ATM.NOXE.KT.CE", "Nitrous oxide emissions (thousand metric tons of CO2 equivalent)");
		reference.put("EN.ATM.GHGO.KT.CE", "Other greenhouse gas emissions (thousand metric tons of CO2 equivalent)");
		reference = categories.get(CATEGORY_DEMOGRAPHICS);
		reference.put("SP.RUR.TOTL.ZS", "Rural population (% of total population)");
		reference.put("SP.URB.TOTL.IN.ZS", "Urban population (% of total population)");
		reference.put("EN.URB.LCTY.UR.ZS", "Population in the largest city (% of urban population)");
		reference.put("SP.POP.GROW", "Population growth (annual %)");
		reference.put("SP.POP.TOTL", "Total Population");
		reference.put("SM.POP.NETM", "Net migration");
		reference.put("SE.ADT.LITR.ZS", "Literacy rate: adult total");
		reference.put("SP.DYN.LE00.IN", "Life expectancy at birth (total: years)");
		reference = categories.get(CATEGORY_EDUCATION);
		reference.put("SE.XPD.TOTL.GD.ZS", "Public spending on education, total (% of GDP)");
		reference.put("SE.TER.ENRR", "School enrollment, tertiary (% gross)");
		reference = categories.get(CATEGORY_EMPLOYMENT_PROSPECTS);
		reference.put("SL.AGR.EMPL.ZS", "Employment in agriculture (% of total employment)");
		reference.put("SL.SRV.EMPL.ZS", "Employment in services (% of total employment)");
		reference.put("SL.IND.EMPL.ZS", "Employment in industry (% of total employment)");
		reference.put("SL.UEM.TOTL.ZS", "Unemployment, total (% of total labor force)");
		reference.put("SL.UEM.LTRM.ZS", "Long-term unemployment (% of total unemployment)");
		reference = categories.get(CATEGORY_FINANCE);
		reference.put("FR.INR.RINR", "Real interest rate (%)");
		reference.put("FR.INR.LEND", "Lending interest rate (%)");
		reference = categories.get(CATEGORY_QUALITY_OF_LIFE);
		reference.put("IS.ROD.PAVE.ZS", "Roads, paved (% of total roads)");
		reference.put("IT.NET.USER.P2", "Internet users (per 100 people)");
		reference.put("IT.CEL.SETS.P2", "Mobile cellular subscriptions (per 100 people)");
		reference.put("IS.VEH.NVEH.P3", "Motor vehicles (per 1,000 people)");
		reference.put("IS.RRS.TOTL.KM", "Rail lines (total route-km)");
		reference = categories.get(CATEGORY_RURAL_LIFE);
		reference.put("SH.H2O.SAFE.RU.ZS", "Improved water source, rural (% of rural population with access)");
		reference.put("AG.LND.FRST.ZS", "Forest area (% of land area)");
		reference.put("AG.LND.AGRI.ZS", "Agricultural land (% of land area)");
		reference.put("AG.LND.ARBL.ZS", "Arable land (% of land area)");
		reference.put("AG.LND.CROP.ZS", "Permanent cropland (% of land area)");
	}
	
	private static boolean addIndicator(String category, String code) {
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
		final Map<String, String> subCategory = categories.get(category);
		if (subCategory == null || subCategory.size() == 0) {
			Log.w("IndicatorLoad", "No indicators are present for category name: " + category);
			return false;
		}
		else {
				final CountDownLatch latch = new CountDownLatch(1);
				Thread aThread = new Thread(new Runnable() {
				@Override
				public void run() { 
					for (String code : subCategory.keySet())
						addIndicator(category, code);
					
					latch.countDown();
				}
			});
			aThread.start();
			try {
				latch.await();
			} catch (InterruptedException e) {e.printStackTrace();}
			return true;
		}
	}
	
	public static List<Indicator> getAllLoadedIndicatorsFromCategory(String category) {
		List<Indicator> indicators = new ArrayList<Indicator>();
		for (int i = 0; i < loadedIndicators.size(); i++) {
			if (loadedIndicators.get(i).getCategory().equals(category)) {
				indicators.add(loadedIndicators.get(i));
			}
		}
		if (indicators.size() == 0) { Log.w("IndicatorGrab", "Found no indicators with category: " + category); }
		return indicators;
	}
	
	public static int getNumberOfLoadedIndicatorsFromCategory(String category)
	{
		int count = 0;
		for (int i = 0; i < loadedIndicators.size(); i++)
			if (loadedIndicators.get(i).getCategory().equals(category))
				count++;
		return count;
	}
	
	public static String[] getCategoriesAsArray() {
		return new String[]{CATEGORY_BUSINESS, CATEGORY_CITY_LIFE, 
							CATEGORY_CLIMATE, CATEGORY_DEMOGRAPHICS, 
							CATEGORY_EDUCATION, CATEGORY_EMPLOYMENT_PROSPECTS,
							CATEGORY_FINANCE, CATEGORY_QUALITY_OF_LIFE,
							CATEGORY_RURAL_LIFE};
	}
	
	public static List<String> getReadableNamesOfIndicatorsInCategory(String category) {
		List<String> indicatorNames = new ArrayList<String>();
		indicatorNames.addAll(categories.get(category).values());	
		return indicatorNames;
	}
	
	public static String getIndicatorCodeFromName(String name)
	{
		if (name.equals("GDP growth (Annual %)"))
			return "NY.GDP.MKTP.KD.ZG";
		else if (name.equals("Listed domestic companies"))
			return "CM.MKT.LDOM.NO";
		else if (name.equals("Health expenditure per capita (current US$)"))
			return "SH.XPD.PCAP";
		else if (name.equals("% of urban populating with access to improved water"))
			return "SH.H2O.SAFE.UR.ZS";
		else if (name.equals("CO2 emissions in kilotons"))
			return "EN.ATM.CO2E.KT";
		else if (name.equals("Methane emissions in kilotons of CO2 equivalent"))
			return "EN.ATM.METH.KT.CE";
		else if (name.equals("Total Population"))
			return "SP.POP.TOTL";
		else if (name.equals("Net migration"))
			return "SM.POP.NETM";
		else if (name.equals("Ease of doing business index (1=easiest)"))
			return "IC.BUS.EASE.XQ";
		else if (name.equals("Strength of legal rights index (0=weak to 10=strong)"))
			return "IC.LGL.CRED.XQ";
		else if (name.equals("Labor Force"))
			return "SL.TLF.TOTL.IN";
		else if (name.equals("Total tax rate (% of commercial profits)"))
			return "IC.TAX.TOTL.CP.ZS";
		else if (name.equals("Health expenditure per capita (current US$)"))
			return "SH.XPD.PCAP";
		else if (name.equals("% of urban population with access to improved water source"))
			return "SH.H2O.SAFE.UR.ZS";
		else if (name.equals("% of urban population with access to improved Sanitation Facilities"))
			return "SH.SDA.ACSN.UR";
		else if (name.equals("Vehicles per km of road"))
			return "IS.VEH.ROAD.K1";
		else if (name.equals("Nitrous oxide emissions (thousand metric tons of CO2 equivalent)"))
			return "EN.ATM.NOXE.KT.CE";
		else if (name.equals("Other greenhouse gas emissions (thousand metric tons of CO2 equivalent)"))
			return "EN.ATM.GHGO.KT.CE";
		else if (name.equals("Rural population (% of total population)"))
			return "SP.RUR.TOTL.ZS";
		else if (name.equals("Urban population (% of total population)"))
			return "SP.URB.TOTL.IN.ZS";
		else if (name.equals("Population in the largest city (% of urban population)"))
			return "EN.URB.LCTY.UR.ZS";
		else if (name.equals("Population growth (annual %)"))
			return "SP.POP.GROW";
		else if (name.equals("Literacy rate: adult total"))
			return "SE.ADT.LITR.ZS";
		else if (name.equals("Life expectancy at birth (total: years)"))
			return "SP.DYN.LE00.IN";
		else if (name.equals("Public spending on education, total (% of GDP)"))
			return "SE.XPD.TOTL.GD.ZS";
		else if (name.equals("School enrollment, tertiary (% gross)"))
			return "SE.TER.ENRR";
		else if (name.equals("Employment in agriculture (% of total employment)"))
			return "SL.AGR.EMPL.ZS";
		else if (name.equals("Employment in services (% of total employment)"))
			return "SL.SRV.EMPL.ZS";
		else if (name.equals("Employment in industry (% of total employment)"))
			return "SL.IND.EMPL.ZS";
		else if (name.equals("Unemployment, total (% of total labor force)"))
			return "SL.UEM.TOTL.ZS";
		else if (name.equals("Long-term unemployment (% of total unemployment)"))
			return "SL.UEM.LTRM.ZS";
		else if (name.equals("Real interest rate (%)"))
			return "FR.INR.RINR";
		else if (name.equals("Lending interest rate (%)"))
			return "FR.INR.LEND";
		else if (name.equals("Roads, paved (% of total roads)"))
			return "IS.ROD.PAVE.ZS";
		else if (name.equals("Internet users (per 100 people)"))
			return "IT.NET.USER.P2";
		else if (name.equals("Mobile cellular subscriptions (per 100 people)"))
			return "IT.CEL.SETS.P2";
		else if (name.equals("Motor vehicles (per 1,000 people)"))
			return "IS.VEH.NVEH.P3";
		else if (name.equals("Rail lines (total route-km)"))
			return "IS.RRS.TOTL.KM";
		else if (name.equals("Improved water source, rural (% of rural population with access)"))
			return "SH.H2O.SAFE.RU.ZS";
		else if (name.equals("Forest area (% of land area)"))
			return "AG.LND.FRST.ZS";
		else if (name.equals("Agricultural land (% of land area)"))
			return "AG.LND.AGRI.ZS";
		else if (name.equals("Arable land (% of land area)"))
			return "AG.LND.ARBL.ZS";
		else if (name.equals("Permanent cropland (% of land area)"))
			return "AG.LND.CROP.ZS";		
		else
			return null;
	}
}
