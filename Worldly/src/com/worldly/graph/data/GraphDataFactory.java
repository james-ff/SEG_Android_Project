package com.worldly.graph.data;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.worldly.controller.WorldlyController;
import com.worldly.data_models.Country;
import com.worldly.data_models.Indicator;
import com.worldly.data_models.IndicatorDataBlock;
import com.worldly.data_store.ListOfIndicators;
import com.worldly.graph.exception.GraphDataSizeMismatchException;
import com.worldly.network.QuerySystem;

/**
 * Class that makes GraphData out of different types 
 * of objects used in the application.
 * 
 * @author Marek Matejka
 *
 */
public class GraphDataFactory 
{
	private static final int MIN_YEAR = 2000;
	private static final int MAX_YEAR = 2013;
	private static final String NO_VALUES_TEXT = "Sorry, no values!";
	
	/**
	 * Creates data for a graph from a single IndicatorDataBlock.
	 * 
	 * @param idb Data which will be included in the graph.
	 * @return Properly formatted data for the graph.
	 */
	public static GraphData createDataFromIndicator(IndicatorDataBlock idb)
	{
		List<GraphDataRow> list = new LinkedList<GraphDataRow>();
		
		list.add(new GraphDataRow("years", idb.getIndicatorReferenced().getName(), true));
		
		String temp = "";
		for (int i = MIN_YEAR; i <= MAX_YEAR; i++)
			if (!(temp = idb.getDataByYear(i).toString()).equals("null"))
				list.add(new GraphDataRow(""+i, temp, false));
		
		if (list.size() == 1)
			list.add(new GraphDataRow(NO_VALUES_TEXT, 0, false));
		
		try{
			return new GraphData(list);
		}catch(GraphDataSizeMismatchException e){e.printStackTrace(); return null;}
	}
	
	public static GraphData createDataFromCategory(String category)
	{			
		if (ListOfIndicators.getNumberOfLoadedIndicatorsFromCategory(category) == 0)
			loadIndicatorsForCategory(category);
		
		List<Indicator> list = ListOfIndicators.getAllLoadedIndicatorsFromCategory(category);
		
		List<GraphDataRow> rows = new LinkedList<GraphDataRow>();
		
		//create first row
		GraphDataRow names = new GraphDataRow("country", list.get(0).getName(), true);
		for (int i = 1; i < list.size(); i++)
			names.addRowData(list.get(i).getName());		
		rows.add(names);
		
		//create other rows
		for (Country country : WorldlyController.getInstance().getCurrentSelectedCountries())
		{
			List<Object> temp = new LinkedList<Object>();
			temp.add(country.getName());
			for (int i = 0; i < list.size(); i++)
			{
				IndicatorDataBlock idb = new IndicatorDataBlock(list.get(i));
				loadIndicatorDataBlock(idb, country);
				country.addDataElement(list.get(i), idb);
				temp.add(getNewestValidData(country, list.get(i)));
			}
			rows.add(new GraphDataRow(temp, false));
		}
		
		if (rows.size() == 1)
		{
			GraphDataRow empty = new GraphDataRow(NO_VALUES_TEXT, 0, false);
			for (int i = 0; i < list.size(); i++)
				empty.addRowData(0);
			rows.add(empty);
		}
			 
		
		try {
			return new GraphData(rows);
		} catch (GraphDataSizeMismatchException e) {e.printStackTrace(); return null;}
	}
													
	
	/**
	 * Loads data for a country into a given {@link IndicatorDataBlock}.
	 * 
	 * @param idb Load data to this object.
	 * @param country Load data for this country.
	 */
	public static void loadIndicatorDataBlock(final IndicatorDataBlock idb, final Country country)
	{
		final CountDownLatch latch = new CountDownLatch(1);
		new Thread(new Runnable() {
			@Override
			public void run(){
				try {
					JSONArray res = new JSONArray(QuerySystem.getIndicatorData(country.getIso2Code(), 
																			   idb.getIndicatorReferenced().getId())).getJSONArray(1);
					for (int a = 0; a < res.length(); a++) {
						JSONObject o = res.getJSONObject(a);
						idb.addDataByYear(o.getInt("date"), o.get("value"));
					}
					latch.countDown();
				} catch (JSONException e) {
					e.printStackTrace();
					latch.countDown();
				}
				
			}
		}).start();
		
		try {
			latch.await();
		} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	/**
	 * Loads indicators with data for a given category.
	 * 
	 * @param category Category for which data should be loaded.
	 */
	private static void loadIndicatorsForCategory(final String category)
	{
		final CountDownLatch latch = new CountDownLatch(1);
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				ListOfIndicators.loadIndicatorsForCategory(category);	
				latch.countDown();
			}
		});
		t.start();
		
		try {
			latch.await();
		} catch (InterruptedException e) {e.printStackTrace();}
	}

	/**
	 * Returns the most up-to-date data for a given country
	 * and for a given indicator.
	 * 
	 * @param country Data for this country.
	 * @param in Data for this indicator.
	 * @return Newest data for given country and indicator, if no value is available
	 * 0 will be returned.
	 */
	private static Object getNewestValidData(Country country, Indicator in)
	{
		int year = MAX_YEAR;
		while (country.getDataElementByYear(in, year).toString().equals("null") && year > MIN_YEAR)
			year--;
		
		if (year == MIN_YEAR)
			return 0;
		
		return country.getDataElementByYear(in, year);
			
	}
	
	/**
	 * Removes a given indicator from a given GraphData.
	 * 
	 * @param data Initial data.
	 * @param indicator Indicator to be removed.
	 * @return New data without the given Indicator.
	 */
	public static GraphData removeIndicator(GraphData data, Indicator indicator)
	{
		data.removeColumn(data.getIndexOfColumnByName(indicator.getName()));
		return data;
	}
	
	/**
	 * Creates GraphData with values from only subcategory 
	 * (indicator) for all currently selected countries.
	 * 
	 * @param category Category of the subcategory.
	 * @param subcategory Exact subcategory name.
	 * @return GraphData object ready to be used with graph.
	 */
	public static GraphData createGraphDataFromSubCategory(String category, String subcategory)
	{
		List<Indicator> list = ListOfIndicators.getAllLoadedIndicatorsFromCategory(category);
		Indicator sub = null;
		String subCategoryCode = ListOfIndicators.getIndicatorCodeFromName(subcategory);
		for (int i = 0; i < list.size(); i++)
			if (list.get(i).getId().equals(subCategoryCode))
			{
				sub = list.get(i);
				break;
			}
		
		List<GraphDataRow> rows = new LinkedList<GraphDataRow>();
		
		//create first row
		rows.add(new GraphDataRow("country", sub.getName(), true));
		
		//create other rows
		for (Country country : WorldlyController.getInstance().getCurrentSelectedCountries())
		{			
			IndicatorDataBlock idb = new IndicatorDataBlock(sub);
			loadIndicatorDataBlock(idb, country);
			country.addDataElement(sub, idb);
			rows.add(new GraphDataRow(country.getName(), getNewestValidData(country, sub), false));
		}
		
		if (rows.size() == 1)
		{
			GraphDataRow empty = new GraphDataRow(NO_VALUES_TEXT, 0, false);
			for (int i = 0; i < list.size(); i++)
				empty.addRowData(0);
			rows.add(empty);
		}
			 
		
		try {
			return new GraphData(rows);
		} catch (GraphDataSizeMismatchException e) {e.printStackTrace(); return null;}
	}
}
