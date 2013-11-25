package com.worldly.graph.data;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.worldly.controller.WorldlyController;
import com.worldly.data_models.Country;
import com.worldly.data_models.Indicator;
import com.worldly.data_models.IndicatorDataBlock;
import com.worldly.data_store.ListOfIndicators;
import com.worldly.graph.exception.GraphDataSizeMismatchException;
import com.worldly.network.QuerySystem;

public class GraphDataFactory 
{
	private static final int MIN_YEAR = 2000;
	private static final int MAX_YEAR = 2013;
	
	public static GraphData createDataFromIndicator(IndicatorDataBlock idb)
	{
		List<GraphDataRow> list = new LinkedList<GraphDataRow>();
		
		list.add(new GraphDataRow("years", idb.getIndicatorReferenced().getName(), true));
		
		String temp = "";
		for (int i = MIN_YEAR; i <= MAX_YEAR; i++)
			if (!(temp = idb.getDataByYear(i).toString()).equals("null"))
				list.add(new GraphDataRow(""+i, temp, false));
		try{
			return new GraphData(list);
		}catch(GraphDataSizeMismatchException e){e.printStackTrace(); return null;}
	}
	
	public static GraphData createDataFromCountry()
	{	
		String category = ListOfIndicators.CATEGORY_CLIMATE;
		
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
				temp.add(getLastValidData(country, list.get(i)));
			}
			rows.add(new GraphDataRow(temp, false));
		}
		
		try {
			return new GraphData(rows);
		} catch (GraphDataSizeMismatchException e) {e.printStackTrace(); return null;}
	}
	
	private static void loadIndicatorDataBlock(final IndicatorDataBlock idb, final Country country)
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

	private static Object getLastValidData(Country country, Indicator in)
	{
		int year = MAX_YEAR;
		while (country.getDataElementByYear(in, year).toString().equals("null") && year >= MIN_YEAR)
			year--;
		
		if (year < MIN_YEAR)
			return 0;
		
		return country.getDataElementByYear(in, year);
			
	}
	
	public static GraphData removeIndicator(GraphData data, Indicator indicator)
	{
		Log.e("DEBUG", "index = "+data.getIndexOfColumnByName(indicator.getName()));
		data.removeColumn(data.getIndexOfColumnByName(indicator.getName()));
		Log.e("DEBUG", data.getData());
		return data;
	}
}
