package com.worldly.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.worldly.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.worldly.data_models.Country;


@SuppressLint("NewApi")
public class MainActivity extends Activity {

	private Activity self;

	private ArrayList<Country> availableCountries = new ArrayList<Country>();
	private ArrayList<Country> selectedCountries = new ArrayList<Country>();
	private HashMap<Marker, Country> markerToCountry;

	private EditText countrySearchField;
	private Button goCompareButton;
	private Spinner allSelectedCountrySpinner;
	private GoogleMap map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		self = this;

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		countrySearchField = (EditText) findViewById(R.id.country_search_field);
		goCompareButton = (Button) findViewById(R.id.compare_button);
		allSelectedCountrySpinner = (Spinner) findViewById(R.id.countries_selected_spinner);
		
		ArrayAdapter<Country> arrayAdapter = new ArrayAdapter<Country>(self, android.R.layout.simple_list_item_1, selectedCountries);
		allSelectedCountrySpinner.setAdapter(arrayAdapter);
		allSelectedCountrySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Log.v(getClass().getName(), "SELECTED");
			}
			
			@Override public void onNothingSelected(AdapterView<?> arg0) {
				Log.v(getClass().getName(), "NOT SELECTED");
			}
			
		});
		
		countrySearchField.addTextChangedListener(new TextWatcher() {
			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override public void afterTextChanged(Editable s) {
				String inputString = countrySearchField.getText().toString();
				Log.d(getClass().getName(), "ATC :" + inputString);
				if (inputString.length() >= 0) {
					plotCountriesOnMap();
				}
			}
		});
		
		goCompareButton.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				if (selectedCountries.size() > 0) {
					Log.d(getClass().getName(), "Go Compare These Countries!");
					// TODO: Save to singleton class or move via an intent
				}
			}
		});
		
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			@Override public void onInfoWindowClick(Marker marker) {
				Country aCountry = markerToCountry.get(marker);
				Log.d(getClass().getName(), marker.getTitle() + " code: " + aCountry.getIso2Code());
				if (selectedCountries.contains(aCountry)) {
					selectedCountries.remove(aCountry);
				} else {
					selectedCountries.add(aCountry);
					allSelectedCountrySpinner.setSelection(selectedCountries.indexOf(aCountry), true);
				}
				marker.hideInfoWindow();
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

		hasGLES20();
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
				//map.animateCamera(CameraUpdateFactory.zoomTo(2000), 0, null);
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
