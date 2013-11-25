package com.worldly.graph.data;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

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
	
	public static void createDataFromCountry(Country country)
	{	
		ListOfIndicators.loadIndicatorsForCategory(ListOfIndicators.CATEGORY_CLIMATE);
		List<Indicator> list = ListOfIndicators.getAllLoadedIndicatorsFromCategory(ListOfIndicators.CATEGORY_CLIMATE);
		
		for (int i = 0; i < list.size(); i++)
		{
			Log.e("DEBUG", ""+list.get(i));
			Log.e("DEBUG", ""+country);
			IndicatorDataBlock idb = new IndicatorDataBlock(list.get(i));
			loadIndicatorDataBlock(idb, country);
			country.addDataElement(list.get(i), idb);
			Log.e("DEBUG", ""+country.getDataElementByYear(list.get(i), 2000));
		}
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

}
