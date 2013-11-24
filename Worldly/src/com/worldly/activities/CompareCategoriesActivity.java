package com.worldly.activities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.worldly.R;
import com.worldly.controller.WorldlyController;
import com.worldly.custom_adapter.CompareExpandableListAdapter;
import com.worldly.data_store.ListOfIndicators;
import com.worldly.graph.Chart;
import com.worldly.graph.GraphTestActivity;
import com.worldly.graph.data.GraphDataRow;
import com.worldly.graph.exception.CannotBeNullException;
import com.worldly.graph.exception.GraphDataSizeMismatchException;
import com.worldly.graph.types.BarChart;
import com.worldly.graph.view.GraphView;
import com.worldly.swipe.SwipeDetector;
import com.worldly.swipe.SwipeListener;

public class CompareCategoriesActivity extends Activity implements
		OnChildClickListener, OnGroupExpandListener, OnGroupCollapseListener
{
	private Context context;
	private List<String> groups;
	private WorldlyController controller;
	private Map<String, List<String>> childs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compare_categories);
		context = this;
		controller = WorldlyController.getInstance();
		
		
		//if in landscape
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
			setupLandscape();
		else
		{
			prepareListData();
			initializeELV(); //ELV = Expandable List View
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

	/*
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
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id)
	{
		String msg = ListOfIndicators.getAllLoadedIndicatorsFromCategory(groups.get(groupPosition)).get(0).getId();
		displayMessage(msg);
		
		//String msg = groups.get(groupPosition) + " : ";
		//msg += childs.get(groups.get(groupPosition)).get(childPosition);
		//displayMessage(msg);
		return true;
	}

	@Override
	public void onGroupExpand(int groupPosition) {
		//String msg = groups.get(groupPosition) + " Expanded";
		ListOfIndicators.loadIndicatorsForCategory(groups.get(groupPosition));

	}

	@Override
	public void onGroupCollapse(int groupPosition)
	{
		String msg = groups.get(groupPosition) + " Collapsed";
		displayMessage(msg);
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
		
		
	}
	
	/**
	 * Initializes ExpandableListView.
	 */
	private void initializeELV()
	{
		// Initializing the ExpandableListView object
		ExpandableListView elvCategories = (ExpandableListView) findViewById(R.id.elvCategories);
		elvCategories.setAdapter(new CompareExpandableListAdapter(this, groups, childs));
		elvCategories.setOnChildClickListener(this);
		elvCategories.setOnGroupExpandListener(this);
		elvCategories.setOnGroupCollapseListener(this);
		elvCategories.setOnTouchListener(new SwipeDetector(this, new SwipeListener() 
		{	
			@Override
			public boolean onTopToBottomSwipe() 
			{	
				return false;
			}
				
			@Override
			public boolean onRightToLeftSwipe() 
			{
				Toast.makeText(context, "Next country", Toast.LENGTH_SHORT).show();
				return true;
			}
				
			@Override
			public boolean onLeftToRightSwipe() 
			{
				Toast.makeText(context, "Previous country", Toast.LENGTH_SHORT).show();
				return true;
			}
			
			@Override
			public boolean onBottomToTopSwipe() 
			{
				return false;
			}
		}));
	}
}