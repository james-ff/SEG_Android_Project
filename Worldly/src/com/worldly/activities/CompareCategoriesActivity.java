package com.worldly.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.example.worldly.R;
import com.worldly.custom_adapter.CompareExpandableListAdapter;

@SuppressLint("NewApi")
public class CompareCategoriesActivity extends Activity implements
		OnChildClickListener, OnGroupExpandListener, OnGroupCollapseListener
{
	private ExpandableListView elvCategories;
	private List<String> groups;
	private Map<String, List<String>> childs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compare_categories);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		prepareListData();

		// Initializing the ExpandableListView object
		elvCategories = (ExpandableListView) findViewById(R.id.elvCategories);
		elvCategories.setAdapter(new CompareExpandableListAdapter(this, groups, childs));
		elvCategories.setOnChildClickListener(this);
		elvCategories.setOnGroupExpandListener(this);
		elvCategories.setOnGroupCollapseListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.selection, menu);
		return true;
	}

	/*
	 * Preparing the list data
	 */
	private void prepareListData()
	{
		groups = new ArrayList<String>();
		childs = new HashMap<String, List<String>>();

		// Adding child data
		groups.add("Business");
		groups.add("City Life");
		groups.add("Climate");

		// Adding child data
		List<String> business = createListFromResourceArray(getResources().getStringArray(R.array.subCategoriesBusiness));
		List<String> cityLife = createListFromResourceArray(getResources().getStringArray(R.array.subCategoriesCityLife));
		List<String> climate = createListFromResourceArray(getResources().getStringArray(R.array.subCategoriesClimate));
		
		childs.put(groups.get(0), business);
		childs.put(groups.get(1), cityLife);
		childs.put(groups.get(2), climate);
	}
	
	public List<String> createListFromResourceArray(String[] array) {
		List<String> finalList = new ArrayList<String>();
		for (String aString : array) {
			finalList.add(aString);
		}
		return finalList;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id)
	{
		String msg = groups.get(groupPosition) + " : ";
		msg += childs.get(groups.get(groupPosition)).get(childPosition);
		displayMessage(msg);
		return true;
	}

	@Override
	public void onGroupExpand(int groupPosition)
	{
		String msg = groups.get(groupPosition) + " Expanded";
		displayMessage(msg);
	}

	@Override
	public void onGroupCollapse(int groupPosition)
	{
		String msg = groups.get(groupPosition) + " Collapsed";
		displayMessage(msg);
	}

	private void displayMessage(String msg)
	{
		//Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
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
}
