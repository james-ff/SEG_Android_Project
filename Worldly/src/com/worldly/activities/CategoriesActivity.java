package com.worldly.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import com.example.worldly.R;
import com.worldly.view.CategoriesListAdapter;

public class CategoriesActivity extends Activity implements
		OnChildClickListener, OnGroupExpandListener, OnGroupCollapseListener
{
	ExpandableListView elvCategories;
	List<String> groups;
	Map<String, List<String>> childs;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_categories);

		prepareListData();

		// Initialising the ExpandableListView object
		elvCategories = (ExpandableListView) findViewById(R.id.elvCategories);
		elvCategories
				.setAdapter(new CategoriesListAdapter(this, groups, childs));
		elvCategories.setOnChildClickListener(this);
		elvCategories.setOnGroupExpandListener(this);
		elvCategories.setOnGroupCollapseListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.categories, menu);
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
		groups.add("Top 250");
		groups.add("Now Showing");
		groups.add("Coming Soon..");

		// Adding child data
		List<String> top250 = new ArrayList<String>();
		top250.add("The Shawshank Redemption");
		top250.add("The Godfather");
		top250.add("The Godfather: Part II");
		top250.add("Pulp Fiction");
		top250.add("The Good, the Bad and the Ugly");
		top250.add("The Dark Knight");
		top250.add("12 Angry Men");

		List<String> nowShowing = new ArrayList<String>();
		nowShowing.add("The Conjuring");
		nowShowing.add("Despicable Me 2");
		nowShowing.add("Turbo");
		nowShowing.add("Grown Ups 2");
		nowShowing.add("Red 2");
		nowShowing.add("The Wolverine");

		List<String> comingSoon = new ArrayList<String>();
		comingSoon.add("2 Guns");
		comingSoon.add("The Smurfs 2");
		comingSoon.add("The Spectacular Now");
		comingSoon.add("The Canyons");
		comingSoon.add("Europa Report");

		childs.put(groups.get(0), top250); // Header, Child data
		childs.put(groups.get(1), nowShowing);
		childs.put(groups.get(2), comingSoon);
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
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
}