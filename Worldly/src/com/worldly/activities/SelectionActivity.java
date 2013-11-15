package com.worldly.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.worldly.R;
import com.worldly.controller.WorldlyController;

/**
 * This activity lets the user pick the desired mode of search within the
 * application structure, it then saves the user's option in a global shared
 * preferences for later use by other activities.
 * 
 * @author Rafael da Silva Costa & Team
 * 
 */
public class SelectionActivity extends Activity {
	/**
	 * Constant representing the mode selected by the user.
	 */
	public static final int FAMILY = 0, NEW_LIFE = 1, BUSINESS = 2;

	/**
	 * Constant representing the key used to store the selected mode in the
	 * shared preferences file.
	 */
	public static final String MODE = "MODE";

	/**
	 * Constant representing the name of the shared preferences file to be used
	 * across this application's activities.
	 */
	public static final String PREFS_NAME = "GLOBAL_PREFS";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selection);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.selection, menu);
		return true;
	}

	/**
	 * This method is called whenever the user presses one of the three buttons
	 * on the screen, it then detects the option selected and updates the global
	 * shared preferences file with the mode selected.
	 * 
	 * @param v
	 *            : The View object which the user interacted with.
	 */
	public void chooseMode(View v) {
		int option = 0;

		// Creates and instantiates a shared preferences object
		SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, 0);

		// Creates an editor object to update the shared preferences
		SharedPreferences.Editor editor = sharedPref.edit();

		WorldlyController appController = WorldlyController.getInstance();
		
		// Detects which button the user has pressed
		switch (v.getId()) {
		case R.id.btnFamily:
			option = FAMILY;
			appController.setCurrentMoveStatus(WorldlyController.FAMILY_MOVE);
			break;
		case R.id.btnNewLife:
			option = NEW_LIFE;
			appController.setCurrentMoveStatus(WorldlyController.PERSONAL_MOVE);
			break;
		case R.id.btnBusiness:
			option = BUSINESS;
			appController.setCurrentMoveStatus(WorldlyController.BUSINESS_MOVE);
			break;
		default:
			appController.setCurrentMoveStatus(WorldlyController.CUSTOM_MOVE);
			break;
		}

		// Saves the mode selected by the user onto the shared preferences file
		editor.putInt(MODE, option);
		editor.commit();

		// Creates a new Intent object for the transition to another activity
		startActivity(new Intent(this, MainActivity.class));
	}
}