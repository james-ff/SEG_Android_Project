package com.worldly.controller;

import java.util.ArrayList;
import java.util.List;

import com.worldly.data_models.Country;
import com.worldly.data_store.ListOfIndicators;

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
	private List<String> compareCategories;
	
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

	public void setCurrentMoveStatus(int currentMoveStatus, List<String> customList) {
		this.currentMoveStatus = currentMoveStatus;
		this.compareCategories = new ArrayList<String>();
		
		if (currentMoveStatus == BUSINESS_MOVE) {
			compareCategories.add(ListOfIndicators.CATEGORY_BUSINESS);
			compareCategories.add(ListOfIndicators.CATEGORY_CITY_LIFE);
			compareCategories.add(ListOfIndicators.CATEGORY_DEMOGRAPHICS);
			compareCategories.add(ListOfIndicators.CATEGORY_EMPLOYMENT_PROSPECTS);
			compareCategories.add(ListOfIndicators.CATEGORY_FINANCE);
			compareCategories.add(ListOfIndicators.CATEGORY_QUALITY_OF_LIFE);
		} else if (currentMoveStatus == FAMILY_MOVE) {
			compareCategories.add(ListOfIndicators.CATEGORY_CITY_LIFE);
			compareCategories.add(ListOfIndicators.CATEGORY_CLIMATE);
			compareCategories.add(ListOfIndicators.CATEGORY_DEMOGRAPHICS);
			compareCategories.add(ListOfIndicators.CATEGORY_EDUCATION);
			compareCategories.add(ListOfIndicators.CATEGORY_QUALITY_OF_LIFE);
			compareCategories.add(ListOfIndicators.CATEGORY_RURAL_LIFE);
		} else if (currentMoveStatus == PERSONAL_MOVE) {
			compareCategories.add(ListOfIndicators.CATEGORY_CITY_LIFE);
			compareCategories.add(ListOfIndicators.CATEGORY_CLIMATE);
			compareCategories.add(ListOfIndicators.CATEGORY_DEMOGRAPHICS);
			compareCategories.add(ListOfIndicators.CATEGORY_EMPLOYMENT_PROSPECTS);
			compareCategories.add(ListOfIndicators.CATEGORY_QUALITY_OF_LIFE);
			compareCategories.add(ListOfIndicators.CATEGORY_RURAL_LIFE);
		} else if (currentMoveStatus == CUSTOM_MOVE) {
			compareCategories.addAll(customList);
		}
	}

	public List<Country> getCurrentSelectedCountries() {
		return currentSelectedCountries;
	}

	public void setCurrentSelectedCountries(List<Country> currentSelectedCountries) {
		this.currentSelectedCountries = currentSelectedCountries;
	}
	
}
