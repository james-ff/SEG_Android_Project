package com.worldly.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.worldly.R;
import com.worldly.data_models.Country;

public class MainActivity extends Activity {
	
	private Activity self;

	private ArrayList<Country> allCountries;
	
	private Spinner myCountrySpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		self = this;

		myCountrySpinner = (Spinner) findViewById(R.id.my_country_spinner);
		
		Thread aThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Log.v(getClass().getName(), "Starting Download");
					allCountries = Country.getAllCountries();
					printAllCountries();
					setSpinnerList();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		aThread.start();
	}

	public void printAllCountries() {
		for (Country aCountry : allCountries) {
			aCountry.print();
		}
	}
	
	public void setSpinnerList() {
		self.runOnUiThread(new Runnable() {
			@Override public void run() {
				List<String> countryNames = new ArrayList<String>();
				for (Country aCountry : allCountries) {
					if (aCountry.getLongitude() != null && aCountry.getLatitude() != null) {
						countryNames.add(aCountry.getName());
					}
				}
				ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(self, android.R.layout.simple_spinner_dropdown_item, countryNames);
				myCountrySpinner.setAdapter(spinnerArrayAdapter);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
