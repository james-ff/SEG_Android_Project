package com.worldly.activities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.worldly.R;
import com.worldly.controller.WorldlyController;
import com.worldly.custom_adapter.CompareExpandableListAdapter;
import com.worldly.data_models.Country;
import com.worldly.data_models.Indicator;
import com.worldly.data_models.IndicatorDataBlock;
import com.worldly.data_store.ListOfIndicators;
import com.worldly.graph.Chart;
import com.worldly.graph.GraphTestActivity;
import com.worldly.graph.data.GraphData;
import com.worldly.graph.data.GraphDataFactory;
import com.worldly.graph.data.GraphDataRow;
import com.worldly.graph.exception.CannotBeNullException;
import com.worldly.graph.exception.GraphDataSizeMismatchException;
import com.worldly.graph.types.BarChart;
import com.worldly.graph.view.GraphView;
import com.worldly.network.QuerySystem;
import com.worldly.swipe.SwipeDetector;
import com.worldly.swipe.SwipeListener;
import com.worldly.view.LogoTextView;

public class CompareCategoriesActivity extends Activity implements
		OnChildClickListener, OnGroupExpandListener, OnGroupCollapseListener, OnGroupClickListener {
	
	private List<String> groups;
	private WorldlyController controller;
	private Map<String, List<String>> childs;
	private Country currentCountry;
	private ExpandableListView elvCategories;
	private CompareExpandableListAdapter adapter;
	
	private LogoTextView prevButton;
	private LogoTextView nextButton;
	private TextView countryTitleView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compare_categories);
		
		controller = WorldlyController.getInstance();
		controller.setCurrentlySelectedCountryIndex(0);
		currentCountry = controller.getCurrentlySelectedCountry();

		elvCategories = (ExpandableListView) findViewById(R.id.elvCategories);
			
		//if in landscape
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			setupLandscape();
			//GraphDataFactory.createDataFromCountry(currentCountry);
		}
		else
		{
			prevButton = (LogoTextView) findViewById(R.id.ltvPrevCountry);
			nextButton = (LogoTextView) findViewById(R.id.ltvNextCountry);
			countryTitleView = (TextView) findViewById(R.id.country_title_view);
			
			nextButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					selectNextCountry();
				}
			});
			
			prevButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					selectPreviousCountry();
				}
			});
			
			prepareListData();
			updateUIAfterSelection();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.selection, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		    case R.id.action_about: // Take user to secret classified About Page     
		        startActivity(new Intent(this, AboutActivity.class));
		        break; 	
			case android.R.id.home: // Respond to the action bar's Up/Home button
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Preparing the list data
	 */
	private void prepareListData() {
		groups = controller.getCategories();
		childs = new HashMap<String, List<String>>();

		// Adding child data
		for (int i = 0; i < groups.size(); i++) {
			childs.put(groups.get(i), ListOfIndicators.getReadableNamesOfIndicatorsInCategory(groups.get(i)));
		}
	}


	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) 
	{
		Log.e(getClass().getName(), "Child View Clicked");
		
		Indicator i = ListOfIndicators.getAllLoadedIndicatorsFromCategory(groups.get(groupPosition)).get(childPosition);
		
		IndicatorDataBlock idb = new IndicatorDataBlock(i);
		this.loadIndicatorDataBlock(idb);
		
		GraphData data = GraphDataFactory.createDataFromIndicator(idb);
		
		Log.e(getClass().getName(), data.getData().toString());
		
		adapter.addGraphData(adapter.getChildId(groupPosition, childPosition), data);	

		return true;
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
		return false;
	}
	
	@Override
	public void onGroupExpand(int groupPosition) {
		ListOfIndicators.loadIndicatorsForCategory(groups.get(groupPosition));
	}

	@Override
	public void onGroupCollapse(int groupPosition) {
		displayMessage(groups.get(groupPosition) + " Collapsed");
	}

	private void displayMessage(String msg)
	{
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
	
	public void showGraph(View v)
	{
		Intent i = new Intent(CompareCategoriesActivity.this, GraphTestActivity.class);
		startActivity(i);
		onStop();
	}
//	public String[] getBusinessIndicatorTitles() {
//		if(this.categoriesBusiness == null) {
//			this.categoriesBusiness = new String[]{"GDP", "Investment", "Import/Export", "Labor Force", "Tax"};
//		}
//		return this.categoriesBusiness;
//	}
//	
//	public String[] getCityLifeIndicatorTitles() {
//		if(this.categoriesCityLife == null) {
//			this.categoriesCityLife = new String[]{"Population", "Age Distribution", "Life Expectancy"};
//		}
//		return this.categoriesCityLife;
//	}
//	
//	public String[] getClimateIndicatorTitles() {
//		if(this.categoriesClimate == null) {
//			this.categoriesClimate = new String[]{"CO2 Emissions", "Methane Emissions"};
//		}
//		return this.categoriesClimate;
//	}
//	
//	public String[] getDemographicsIndicatorTitles() {
//		if(this.categoriesDemographics == null) {
//			this.categoriesDemographics = new String[]{};
//		}
//		return this.categoriesDemographics;
//	}
//	
//	public String[] getEducationIndicatorTitles() {
//		if(this.categoriesEducation == null) {
//			this.categoriesEducation = new String[]{};
//		}
//		return this.categoriesEducation;
//	}
//	
//	public String[] getEmploymentProspectsIndicatorTitles() {
//		if(this.categoriesEmploymentProspects == null) {
//			this.categoriesEmploymentProspects = new String[]{};
//		}
//		return this.categoriesEmploymentProspects;
//	}
//	
//	public String[] getFinanceIndicatorTitles() {
//		if(this.categoriesFinance == null) {
//			this.categoriesFinance = new String[]{};
//		}
//		return this.categoriesFinance;
//	}
//	
//	public String[] getQualityOfLifeIndicatorTitles() {
//		if(this.categoriesQualityOfLife == null) {
//			this.categoriesQualityOfLife = new String[]{};
//		}
//		return this.categoriesQualityOfLife;
//	}
//	
//	public String[] getRuralLifeIndicatorTitles() {
//		if(this.categoriesRuralLife == null) {
//			this.categoriesRuralLife = new String[]{};
//		}
//		return this.categoriesRuralLife;
//	}
	
	/**
	 * Loads the screen in landscape.
	 */
	private void setupLandscape()
	{
		Spinner categories = (Spinner)findViewById(R.id.categoriesSelectSpinner);
		categories.setAdapter(new com.worldly.custom_adapter.SpinnerAdapter(this, 
				android.R.layout.simple_spinner_item, 
				controller.getCategories()));
		
		try {
			Chart chart = new BarChart(new GraphDataRow("countries", "Population", true), new GraphDataRow("Slovakia", 12346, false), this);
			GraphView graph = (GraphView)findViewById(R.id.landscapeGraphView);
			graph.loadGraph(chart);
		} catch (CannotBeNullException e) {e.printStackTrace();} 
		catch (GraphDataSizeMismatchException e) {e.printStackTrace();}
		
		GraphDataFactory.createDataFromCountry(currentCountry);
	}
	
	/**
	 * Initializes ExpandableListView.
	 */
	private void initializeELV()
	{
		// Initializing the ExpandableListView object
		adapter = new CompareExpandableListAdapter(this, groups, childs);
		elvCategories.setAdapter(adapter);
		elvCategories.setOnChildClickListener(this);
		elvCategories.setOnGroupExpandListener(this);
		elvCategories.setOnGroupCollapseListener(this);
		elvCategories.setOnTouchListener(new SwipeDetector(this, new SwipeListener() 
		{	
			@Override public boolean onTopToBottomSwipe() {return false;}
			@Override public boolean onBottomToTopSwipe() {return false;}
				
			@Override
			public boolean onRightToLeftSwipe() 
			{
				selectNextCountry();
				return true;
			}
				
			@Override
			public boolean onLeftToRightSwipe() 
			{
				selectPreviousCountry();
				return true;
			}
		}));
	}
	
	public boolean selectNextCountry() {
		if (controller.getCurrentlySelectedCountryIndex() >= this.controller.getCurrentSelectedCountries().size() - 1) {
			Toast.makeText(this, "There is not a next country", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			//Toast.makeText(this, "Next country", Toast.LENGTH_SHORT).show();
			controller.incrementSelectedCountryIndex();
			updateUIAfterSelection();
			return true;
		}
	}
	
	public boolean selectPreviousCountry() {
		if (controller.getCurrentlySelectedCountryIndex() <= 0) {
			Toast.makeText(this, "There is not a previous country", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			//Toast.makeText(this, "Previous country", Toast.LENGTH_SHORT).show();
			controller.decrementSelectedCountryIndex();
			updateUIAfterSelection();
			return true;
		}
	}
	
	public void updateUIAfterSelection() {
		// Checking if previous button should be enabled
		if (controller.getCurrentlySelectedCountryIndex() <= 0) {
			// Grey out previous
			prevButton.setVisibility(View.INVISIBLE);
		} else {
			// Button Enabled
			prevButton.setVisibility(View.VISIBLE);
		}
		
		// Checking if next button should be enabled
		if (controller.getCurrentlySelectedCountryIndex() >= controller.getCurrentSelectedCountries().size() - 1) {
			// Grey out next
			nextButton.setVisibility(View.INVISIBLE);
		} else {
			// Button Enabled
			nextButton.setVisibility(View.VISIBLE);
		}

		currentCountry = controller.getCurrentlySelectedCountry();
		countryTitleView.setText(currentCountry.getName());
		initializeELV();
	}
	
	private void loadIndicatorDataBlock(final IndicatorDataBlock idb)
	{
		final CountDownLatch latch = new CountDownLatch(1);
		new Thread(new Runnable() {
			@Override
			public void run(){
				try {
					JSONArray res = new JSONArray(QuerySystem.getIndicatorData(currentCountry.getIso2Code(), 
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