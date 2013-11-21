package com.worldly.controller;

import java.util.List;

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
	
	/**
	 * Constant representing the mode selected by the user.
	 */
	public static final int FAMILY_MOVE = 0, PERSONAL_MOVE = 1, BUSINESS_MOVE = 2, CUSTOM_MOVE = 3;
	
	private int currentMoveStatus;
	private List<Country> currentSelectedCountries;
	
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
	
	public int getCurrentMoveStatus() {
		return currentMoveStatus;
	}

	public void setCurrentMoveStatus(int currentMoveStatus) {
		this.currentMoveStatus = currentMoveStatus;
	}

	public List<Country> getCurrentSelectedCountries() {
		return currentSelectedCountries;
	}

	public void setCurrentSelectedCountries(List<Country> currentSelectedCountries) {
		this.currentSelectedCountries = currentSelectedCountries;
	}
	
}
