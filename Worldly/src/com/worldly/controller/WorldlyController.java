package com.worldly.controller;

import java.util.ArrayList;

import com.worldly.data_models.Country;

/**
 * This class acts as a global data controller, that stores the decisions made 
 * at each decision making activity. It supports saving and restoring it's 
 * state to/from file.
 * 
 * Current Decisions supported:
 * - Choosing Move Type
 * - Choosing Countries to compare
 * 
 * @author James Bellamy & Team
 * 
 */
public class WorldlyController {

	private static WorldlyController instance = null;
	
	public static final String PERSONAL_MOVE = "PERSONAL_MOVE_KEY";
	public static final String FAMILY_MOVE = "FAMILY_MOVE_KEY";
	public static final String BUSINESS_MOVE = "BUSINESS_MOVE_KEY";
	public static final String CUSTOM_MOVE = "CUSTOM_MOVE_KEY";
	
	private String currentMoveStatus;
	private ArrayList<Country> currentSelectedCountries;
	
	protected WorldlyController() {
		super();
		
	}
	
	public static WorldlyController getInstance() {
	      if(instance == null) {
	         instance = new WorldlyController();
	      }
	      return instance;
	   }
	
	// TODO: Create method for ranking all / selected countries based on current move status
	
	// TODO: Create method to store Controller state on file
	// TODO: Create method to retrieve Controller state from file
	
	public String getCurrentMoveStatus() {
		return currentMoveStatus;
	}

	public void setCurrentMoveStatus(String currentMoveStatus) {
		this.currentMoveStatus = currentMoveStatus;
	}

	public ArrayList<Country> getCurrentSelectedCountries() {
		return currentSelectedCountries;
	}

	public void setCurrentSelectedCountries(
			ArrayList<Country> currentSelectedCountries) {
		this.currentSelectedCountries = currentSelectedCountries;
	}
	
}
