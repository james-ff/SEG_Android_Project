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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.worldly.R;
import com.worldly.controller.WorldlyController;
import com.worldly.custom_adapter.CompareExpandableListAdapter;
import com.worldly.custom_adapter.SpinnerAdapter;
import com.worldly.data_models.Indicator;
import com.worldly.data_models.IndicatorDataBlock;
import com.worldly.data_store.ListOfIndicators;
import com.worldly.graph.data.GraphData;
import com.worldly.graph.data.GraphDataFactory;
import com.worldly.graph.exception.CannotBeNullException;
import com.worldly.graph.types.BarChart;
import com.worldly.graph.view.GraphView;
import com.worldly.swipe.SwipeDetector;
import com.worldly.swipe.SwipeListener;
import com.worldly.view.LogoTextView;

/**
 * Activity used to compare the indicators and to display the graphs, it implements Listeners used by the ExpandableListView.
 * @author Rafael da Silva Costa & Team
 *
 */
public class CompareCategoriesActivity extends Activity implements
		OnChildClickListener, OnGroupExpandListener, OnGroupCollapseListener, OnGroupClickListener {
	
	private Context context = this;
	private WorldlyController appController = WorldlyController.getInstance();
	
	private List<String> groups;
	private Map<String, List<String>> children;

	private GraphData data = null;
	private GraphView graph = null;

	private ExpandableListView elvCategories;
	private CompareExpandableListAdapter adapter;

	private LogoTextView prevButton;
	private LogoTextView nextButton;
	private TextView countryTitleView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compare_categories);

		appController.setCurrentlySelectedCountryIndex(0);

		elvCategories = (ExpandableListView) findViewById(R.id.elvCategories);

		// if in landscape
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) 
			setupLandscape();
		else 
			setupPortrait();
	}

	@Override
	protected void onPause() {
		super.onPause();
		appController.saveState();
	}

	@Override
	protected void onResume() {
		super.onResume();
		appController = WorldlyController.getInstance();
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
		case R.id.action_about: // Take user to About Page
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
		groups = appController.getCategories();
		children = new HashMap<String, List<String>>();

		// Adding child data
		for (int i = 0; i < groups.size(); i++) {
			children.put(groups.get(i), ListOfIndicators.getReadableNamesOfIndicatorsInCategory(groups.get(i)));
		}
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		//Log.d(getClass().getName(), "Child View Clicked");

		Indicator i = ListOfIndicators.getAllLoadedIndicatorsFromCategory(groups.get(groupPosition)).get(childPosition);

		IndicatorDataBlock idb = new IndicatorDataBlock(i);
		GraphDataFactory.loadIndicatorDataBlock(idb, appController.getCurrentlySelectedCountry());

		GraphData data = GraphDataFactory.createDataFromIndicator(idb);

		//Log.d(getClass().getName(), data.getData().toString());

		adapter.addGraphData(adapter.getChildId(groupPosition, childPosition), data);

		return true;
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		return false;
	}

	@Override
	public void onGroupExpand(int groupPosition) {
		if (ListOfIndicators.getNumberOfLoadedIndicatorsFromCategory(groups.get(groupPosition)) == 0)
			ListOfIndicators.loadIndicatorsForCategory(groups.get(groupPosition));

	}

	@Override
	public void onGroupCollapse(int groupPosition) {}
	
	/**
	 * Loads the screen in landscape.
	 */
	private void setupLandscape() {
		final Spinner categories = (Spinner) findViewById(R.id.categoriesSelectSpinner);
		final Spinner subCategories = (Spinner) findViewById(R.id.subCategoriesSelectSpinner);

		// set categories
		categories.setAdapter(new SpinnerAdapter(this, android.R.layout.simple_spinner_item, appController.getCategories()));
		categories.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// set subcategories for currently selected category
				subCategories.setAdapter(new SpinnerAdapter(context, android.R.layout.simple_spinner_item, getSubcategories(categories.getSelectedItem().toString())));
				subCategories.setSelection(0); // set 1st subcategory selected
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		subCategories.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				graph = (GraphView) findViewById(R.id.landscapeGraphView);
				if (subCategories.getSelectedItemPosition() == 0)
					data = GraphDataFactory.createDataFromCategory(categories.getSelectedItem().toString());
				else
					data = GraphDataFactory.createGraphDataFromSubCategory(categories.getSelectedItem().toString(), subCategories.getSelectedItem().toString());

				try {
					graph.loadGraph(new BarChart(data, context));
				} catch (CannotBeNullException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	private void setupPortrait() {
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

	/**
	 * Initializes ExpandableListView.
	 */
	private void initializeELV() {
		// Initializing the ExpandableListView object
		adapter = new CompareExpandableListAdapter(this, groups, children);
		elvCategories.setAdapter(adapter);
		elvCategories.setOnChildClickListener(this);
		elvCategories.setOnGroupExpandListener(this);
		elvCategories.setOnGroupCollapseListener(this);
		elvCategories.setOnTouchListener(new SwipeDetector(this, new SwipeListener() {
			@Override
			public boolean onTopToBottomSwipe() {
				return false;
			}

			@Override
			public boolean onBottomToTopSwipe() {
				return false;
			}

			@Override
			public boolean onRightToLeftSwipe() {
				selectNextCountry();
				return true;
			}

			@Override
			public boolean onLeftToRightSwipe() {
				selectPreviousCountry();
				return true;
			}
		}));
	}

	public void selectNextCountry() {
		if (appController.hasNextCountrySelection()) {
			// Toast.makeText(this, "Next country", Toast.LENGTH_SHORT).show();
			appController.incrementSelectedCountryIndex();
		} else {
			// Toast.makeText(this, "There is not a next country", Toast.LENGTH_SHORT).show();
			appController.selectFirstCountry();
		}
		updateUIAfterSelection();
	}

	public void selectPreviousCountry() {
		if (appController.hasPreviousCountrySelection()) {
			// Toast.makeText(this, "Previous country", Toast.LENGTH_SHORT).show();
			appController.decrementSelectedCountryIndex();
		} else {
			// Toast.makeText(this, "There is not a previous country", Toast.LENGTH_SHORT).show();
			appController.selectLastCountry();
		}
		updateUIAfterSelection();
	}

	public void updateUIAfterSelection() {
		countryTitleView.setText(appController.getCurrentlySelectedCountry().getName());
		initializeELV();
	}

	private List<String> getSubcategories(String category) {
		List<String> indicatorNames = ListOfIndicators.getReadableNamesOfIndicatorsInCategory(category);
		indicatorNames.add(0, "All indicators");
		return indicatorNames;
	}
}
