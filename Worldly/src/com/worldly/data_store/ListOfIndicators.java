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
import com.worldly.data_models.UnloadedIndicatorDescription;
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
	
	public static final String CATEGORY_BUSINESS = "Business";
	public static final String CATEGORY_CITY_LIFE = "City Life";
	public static final String CATEGORY_CLIMATE = "Climate";
	public static final String CATEGORY_DEMOGRAPHICS = "Demographics";
	public static final String CATEGORY_EDUCATION = "Education";
	public static final String CATEGORY_EMPLOYMENT_PROSPECTS = "Employment Prospects";
	public static final String CATEGORY_FINANCE = "Finance";
	public static final String CATEGORY_QUALITY_OF_LIFE = "Quality of Life";
	public static final String CATEGORY_RURAL_LIFE = "Rural Life";
	
	private static List<Indicator> loadedIndicators = new ArrayList<Indicator>();
	private static Map<String, ArrayList<UnloadedIndicatorDescription>> categories = new HashMap<String, ArrayList<UnloadedIndicatorDescription>>();
	
	public ListOfIndicators() {
		// Category Setup -- These categories will be the headings of the expandable groups (use droneStrikes.keySet())
		categories.put(CATEGORY_BUSINESS, new ArrayList<UnloadedIndicatorDescription>());
		categories.put(CATEGORY_CITY_LIFE, new ArrayList<UnloadedIndicatorDescription>());
		categories.put(CATEGORY_CLIMATE, new ArrayList<UnloadedIndicatorDescription>());
		categories.put(CATEGORY_DEMOGRAPHICS, new ArrayList<UnloadedIndicatorDescription>());
		categories.put(CATEGORY_EDUCATION, new ArrayList<UnloadedIndicatorDescription>());
		categories.put(CATEGORY_EMPLOYMENT_PROSPECTS, new ArrayList<UnloadedIndicatorDescription>());
		categories.put(CATEGORY_FINANCE, new ArrayList<UnloadedIndicatorDescription>());
		categories.put(CATEGORY_QUALITY_OF_LIFE, new ArrayList<UnloadedIndicatorDescription>());
		categories.put(CATEGORY_RURAL_LIFE, new ArrayList<UnloadedIndicatorDescription>());
		
		/* -- Indicators Setup -- Here we add the known codes of indicators we are interested in. -- */ 
		List<UnloadedIndicatorDescription> reference = categories.get(CATEGORY_BUSINESS);
		reference.add(new UnloadedIndicatorDescription("GDP growth (Annual %)", "NY.GDP.MKTP.KD.ZG"));
		reference.add(new UnloadedIndicatorDescription("Listed domestic companies", "CM.MKT.LDOM.NO"));
		reference.add(new UnloadedIndicatorDescription("Ease of doing business index (1=easiest)", "IC.BUS.EASE.XQ"));
		reference.add(new UnloadedIndicatorDescription("Strength of legal rights index (0=weak to 10=strong)", "IC.LGL.CRED.XQ"));
		reference.add(new UnloadedIndicatorDescription("Labor Force", "SL.TLF.TOTL.IN"));
		reference.add(new UnloadedIndicatorDescription("Total tax rate (% of commercial profits)", "IC.TAX.TOTL.CP.ZS"));
		reference = categories.get(CATEGORY_CITY_LIFE);
		reference.add(new UnloadedIndicatorDescription("Health expenditure per capita (current US$)", "SH.XPD.PCAP"));
		reference.add(new UnloadedIndicatorDescription("% of urban population with access to improved water source", "SH.H2O.SAFE.UR.ZS"));
		reference.add(new UnloadedIndicatorDescription("% of urban population with access to improved Sanitation Facilities", "SH.SDA.ACSN.UR"));
		reference.add(new UnloadedIndicatorDescription("Vehicles per km of road", "IS.VEH.ROAD.K1"));
		reference = categories.get(CATEGORY_CLIMATE);
		reference.add(new UnloadedIndicatorDescription("CO2 emissions in kilotons", "EN.ATM.CO2E.KT"));
		reference.add(new UnloadedIndicatorDescription("Methane emissions in kilotons of CO2 equivalent", "EN.ATM.METH.KT.CE"));
		reference.add(new UnloadedIndicatorDescription("Nitrous oxide emissions (thousand metric tons of CO2 equivalent)", "EN.ATM.NOXE.KT.CE"));
		reference.add(new UnloadedIndicatorDescription("Other greenhouse gas emissions (thousand metric tons of CO2 equivalent)", "EN.ATM.GHGO.KT.CE"));
		reference = categories.get(CATEGORY_DEMOGRAPHICS);
		reference.add(new UnloadedIndicatorDescription("Rural population (% of total population)", "SP.RUR.TOTL.ZS"));
		reference.add(new UnloadedIndicatorDescription("Urban population (% of total population)", "SP.URB.TOTL.IN.ZS"));
		reference.add(new UnloadedIndicatorDescription("Population in the largest city (% of urban population)", "EN.URB.LCTY.UR.ZS"));
		reference.add(new UnloadedIndicatorDescription("Population growth (annual %)", "SP.POP.GROW"));
		reference.add(new UnloadedIndicatorDescription("Total Population", "SP.POP.TOTL"));
		reference.add(new UnloadedIndicatorDescription("Net migration", "SM.POP.NETM"));
		reference.add(new UnloadedIndicatorDescription("Literacy rate: adult total", "SE.ADT.LITR.ZS"));
		reference.add(new UnloadedIndicatorDescription("Life expectancy at birth (total: years)", "SP.DYN.LE00.IN"));
		reference = categories.get(CATEGORY_EDUCATION);
		reference.add(new UnloadedIndicatorDescription("Public spending on education, total (% of GDP)", "SE.XPD.TOTL.GD.ZS"));
		reference.add(new UnloadedIndicatorDescription("School enrollment, tertiary (% gross)", "SE.TER.ENRR"));
		reference = categories.get(CATEGORY_EMPLOYMENT_PROSPECTS);
		reference.add(new UnloadedIndicatorDescription("Employment in agriculture (% of total employment)", "SL.AGR.EMPL.ZS"));
		reference.add(new UnloadedIndicatorDescription("Employment in services (% of total employment)", "SL.SRV.EMPL.ZS"));
		reference.add(new UnloadedIndicatorDescription("Employment in industry (% of total employment)", "SL.IND.EMPL.ZS"));
		reference.add(new UnloadedIndicatorDescription("Unemployment, total (% of total labor force)", "SL.UEM.TOTL.ZS"));
		reference.add(new UnloadedIndicatorDescription("Long-term unemployment (% of total unemployment)", "SL.UEM.LTRM.ZS"));
		reference = categories.get(CATEGORY_FINANCE);
		reference.add(new UnloadedIndicatorDescription("Real interest rate (%)", "FR.INR.RINR"));
		reference.add(new UnloadedIndicatorDescription("Lending interest rate (%)", "FR.INR.LEND"));
		reference = categories.get(CATEGORY_QUALITY_OF_LIFE);
		reference.add(new UnloadedIndicatorDescription("Roads, paved (% of total roads)", "IS.ROD.PAVE.ZS"));
		reference.add(new UnloadedIndicatorDescription("Internet users (per 100 people)", "IT.NET.USER.P2"));
		reference.add(new UnloadedIndicatorDescription("Mobile cellular subscriptions (per 100 people)", "IT.CEL.SETS.P2"));
		reference.add(new UnloadedIndicatorDescription("Motor vehicles (per 1,000 people)", "IS.VEH.NVEH.P3"));
		reference.add(new UnloadedIndicatorDescription("Rail lines (total route-km)", "IS.RRS.TOTL.KM"));
		reference = categories.get(CATEGORY_RURAL_LIFE);
		reference.add(new UnloadedIndicatorDescription("Improved water source, rural (% of rural population with access)", "SH.H2O.SAFE.RU.ZS"));
		reference.add(new UnloadedIndicatorDescription("Forest area (% of land area)", "AG.LND.FRST.ZS"));
		reference.add(new UnloadedIndicatorDescription("Agricultural land (% of land area)", "AG.LND.AGRI.ZS"));
		reference.add(new UnloadedIndicatorDescription("Arable land (% of land area)", "AG.LND.ARBL.ZS"));
		reference.add(new UnloadedIndicatorDescription("Permanent cropland (% of land area)", "AG.LND.CROP.ZS"));
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
		final ArrayList<UnloadedIndicatorDescription> codes = categories.get(category);
		if (codes == null || codes.size() == 0) {
			Log.w("IndicatorLoad", "No indicators are present for category name: " + category);
			return false;
		}
		else {
				final CountDownLatch latch = new CountDownLatch(1);
				Thread aThread = new Thread(new Runnable() {
				@Override
				public void run() { 
					for (int i = 0; i < codes.size(); i++) { addIndicator(category, codes.get(i).getCode()); }
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
		List<Indicator> returns = new ArrayList<Indicator>();
		for (int i = 0; i < loadedIndicators.size(); i++) {
			if (loadedIndicators.get(i).getCategory().equals(category)) {
				returns.add(loadedIndicators.get(i));
			}
		}
		if (returns.size() == 0) { Log.w("IndicatorGrab", "Found no indicators with category: " + category); }
		return returns;
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
		List<String> returner = new ArrayList<String>();
		for (int i = 0; i < categories.get(category).size(); i++) {
			returner.add(categories.get(category).get(i).getName());
		}
		return returner;
	}
	
	public static String getIndicatorCodeFromName(String name)
	{
		if (name.equals("GDP growth (Annual %"))
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
		else
			return null;
	}
}
