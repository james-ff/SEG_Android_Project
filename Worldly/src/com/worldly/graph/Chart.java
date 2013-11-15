package com.worldly.graph;

import com.worldly.graph.data.GraphData;
import com.worldly.graph.data.GraphDataColumn;
import com.worldly.graph.data.GraphDataRow;
import com.worldly.graph.exception.CannotBeNullException;
import com.worldly.graph.exception.GraphDataSizeMismatchException;
import com.worldly.graph.listener.GraphDataChangeListener;

/**
 * Abstract class which all types of Graphs/Charts
 * have to extend.
 * 
 * @author Marek Matejka
 *
 */
public abstract class Chart
{	
	/**
	 * Listener which automatically updates the graph with new data,
	 * each time the data is changed.
	 */
	protected GraphDataChangeListener listener;
	
	/**
	 * Data for the graph.
	 */
	protected GraphData data;
	
	/**
	 * Returns the HTML and Javascript content used
	 * to load and display the graph in GraphView.
	 * 
	 * @return HTML and Javascript content in a String
	 * that can be read by Google Graphs.
	 */
	public abstract String getContent();
	
	/**
	 * Overwrites the current data in the graph.
	 * 
	 * @param data Fully formatted data for the graph.
	 */
	public void setGraphData(GraphData data)
	{
		this.data = data;
	}
	
	/**
	 * Returns the current data for the graph.
	 * 
	 * @return Current graph's data.
	 */
	public GraphData getGraphData()
	{
		return this.data;
	}
	
	/**
	 * Adds column to graph's current data.
	 * 
	 * @param column Column of data which will be added.
	 * @throws CannotBeNullException If column or any of its content is NULL exception is thrown.
	 * @throws GraphDataSizeMismatchException If column does not have the correct length exception is thrown.
	 */
	public abstract void addColumn(GraphDataColumn column) throws CannotBeNullException, GraphDataSizeMismatchException;
	
	/**
	 * Adds row to graph's current data.
	 * 
	 * @param row Row of data which will be added.
	 * @throws CannotBeNullException If row of any of its content is NULL exception is thrown.
	 * @throws GraphDataSizeMismatchException If the row's length is different to current length of row 
	 * in the GraphData exception is thrown.
	 */
	public abstract void addRow(GraphDataRow row) throws CannotBeNullException, GraphDataSizeMismatchException;
	
	/**
	 * Removes a row from data on a given index.
	 * Index starts with 0.
	 * 
	 * @param index Index of row to be removed.
	 */
	public abstract void removeRow(int index);
	
	/**
	 * Removes a column from data on a given index.
	 * 
	 * @param index Index of a column to be removed.
	 */
	public abstract void removeColumn(int index);
	
	/**
	 * Sets {@link GraphDataChangeListener} for this Chart.
	 * 
	 * @param listener Concrete instance of the listener.
	 */
	public void setGraphDataChangeListener(GraphDataChangeListener listener)
	{
		this.listener = listener;
	}
	
	/**
	 * Returns the number of columns in the data for the graph.
	 * 
	 * @return Number of columns in graph's data.
	 */
	public int getNumberOfColumns()
	{
		return data.getNumberOfColumns();
	}
	
	/**
	 * Returns the number of rows in the data for the graph.
	 * 
	 * @return Number of rows in graph's data.
	 */
	public int getNumberOfRows()
	{
		return data.getNumberOfRows();
	}
}
