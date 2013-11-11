package com.worldly.data_store;

import java.util.ArrayList;

import com.worldly.data_models.Indicator;

/**
 * This class works as a data storage for the indicators that will be presented to user on
 * selecting a country. This should be properly cached and stored so to avoid increased
 * traffic usage.
 * 
 * @author Annie the Eagle & Team
 * 
 */
public class ListOfIndicators {
	ArrayList<Indicator> indicators = new ArrayList<Indicator>();
	
	public ListOfIndicators() { // TODO: Determine which indicators we are interested in.
		
	}
}
