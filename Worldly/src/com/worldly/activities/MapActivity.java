package com.worldly.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.example.worldly.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.worldly.controller.WorldlyController;
import com.worldly.data_models.Country;


@SuppressLint("NewApi")
public class MapActivity extends Activity {

	private Activity self;

	private ArrayList<Country> availableCountries = new ArrayList<Country>();
	private List<Country> selectedCountries = new ArrayList<Country>();
	private HashMap<Marker, Country> markerToCountry;

	private EditText countrySearchField;
	private Button goCompareButton;
	private Button clearSelectionButton;
	private Button allSelectedCountriesButton;
	//private Spinner allSelectedCountrySpinner;
	private ArrayAdapter<Country> arrayAdapter;
	private GoogleMap map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		self = this;
		
		WorldlyController appController = WorldlyController.getInstance();
		if (appController.getCurrentSelectedCountries() != null) {
			this.selectedCountries = appController.getCurrentSelectedCountries();
		}

		if (hasGLES20()) {
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
				@Override public void onInfoWindowClick(Marker marker) {
					Country aCountry = markerToCountry.get(marker);
					if (selectedCountries.contains(aCountry)) {
						selectedCountries.remove(aCountry);
					} else {
						selectedCountries.add(aCountry);
					}
					arrayAdapter.notifyDataSetChanged();
					marker.hideInfoWindow();
				}
			});
		}
		
		//allSelectedCountrySpinner = (Spinner) findViewById(R.id.countries_selected_spinner);
		
		countrySearchField = (EditText) findViewById(R.id.country_search_field);
		goCompareButton = (Button) findViewById(R.id.compare_button);
		allSelectedCountriesButton = (Button) findViewById(R.id.countries_selected_button);
		clearSelectionButton = (Button) findViewById(R.id.clear_country_selection_button);
		
		this.arrayAdapter = new ArrayAdapter<Country>(self, android.R.layout.simple_spinner_dropdown_item, selectedCountries);
		allSelectedCountriesButton.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				new AlertDialog.Builder(self)
					.setTitle(selectedCountries.size() + " " + getResources().getString(R.string.countries_spinner_prompt))
					.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
						@Override public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
				}).create().show();
			}
		});
		//this.arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		allSelectedCountrySpinner.setAdapter(this.arrayAdapter);
//		allSelectedCountrySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//			
//			@Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//				//Log.v(getClass().getName(), "SELECTED");
//				parent.setSelection(0);
//			}
//			
//			@Override public void onNothingSelected(AdapterView<?> parent) {
//				//Log.v(getClass().getName(), "NOT SELECTED");
//			}
//			
//		});
		
		countrySearchField.addTextChangedListener(new TextWatcher() {
			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override public void afterTextChanged(Editable s) {
				String inputString = countrySearchField.getText().toString();
				if (inputString.length() >= 0) {
					plotCountriesOnMap();
				}
			}
		});
		
		goCompareButton.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				if (selectedCountries.size() > 0) {
					WorldlyController appController = WorldlyController.getInstance();
					appController.setCurrentSelectedCountries(selectedCountries);
					
					startActivity(new Intent(self, CompareActivity.class));
				}
			}
		});
		
		clearSelectionButton.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				selectedCountries.clear();
			}
		});

		Thread aThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					availableCountries = Country.getAllCountries();
					plotCountriesOnMap();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		aThread.start();
	}

	public boolean hasGLES20() {
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo info = am.getDeviceConfigurationInfo();
		Log.d(getClass().getName(), (info.reqGlEsVersion >= 0x20000) ? "Has Open GL 2.0" : "Has not got Open GL 2.0");
		return info.reqGlEsVersion >= 0x20000;
	}

	public void plotCountriesOnMap() {
		self.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				plotSpecificCountriesOnMap();
			}
		});
	}
	
	public void plotSpecificCountriesOnMap() {
		map.clear();
		markerToCountry = new HashMap<Marker, Country>();
		if (availableCountries.size() > 0) {
			int markerCount = 0;
			Marker lastMarker = null;
			for (Country aCountry : availableCountries) {
				Locale current = getResources().getConfiguration().locale;
				String inputString = countrySearchField.getText().toString().toLowerCase(current);
				String countryName = aCountry.getName().toLowerCase(current);
				String iso2Code = aCountry.getIso2Code().toLowerCase(current);
				if (aCountry.getLatitude() != null && aCountry.getLongitude() != null 
						&& (countryName.startsWith(inputString) || iso2Code.startsWith(inputString))) {
					LatLng aLocation = new LatLng(aCountry.getLatitude(), aCountry.getLongitude());
					if (map != null) {
						String title = aCountry.getName() + " - " + aCountry.getCapitalCity();
						MarkerOptions aMarkerOption = new MarkerOptions().title(title).snippet("Tap to Add / Remove").position(aLocation);
						Marker aMarker = map.addMarker(aMarkerOption);
						markerToCountry.put(aMarker, aCountry);
						lastMarker = aMarker;
						markerCount++;
					}
				}
			}
			if (markerCount == 1) {
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastMarker.getPosition(), 4));
				lastMarker = null;
			} else {
				map.moveCamera(CameraUpdateFactory.zoomTo(1));
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
