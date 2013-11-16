package com.worldly.graph.listener;

import com.worldly.graph.Chart;

/**
 * Interface defining listener that is called each time
 * data (GraphData) is modified.
 * 
 * @author Marek Matejka
 *
 */
public interface GraphDataChangeListener 
{
	/**
	 * Called when graph data is changed and should
	 * specify what should happen in such situation.
	 * 
	 * @param chart Chart for which data was changed.
	 */
	public void onGraphDataChanged(Chart chart);
}
