package com.worldly.graph;

import com.worldly.graph.data.GraphData;
import com.worldly.graph.data.GraphDataColumn;
import com.worldly.graph.data.GraphDataRow;
import com.worldly.graph.exception.CannotBeNullException;
import com.worldly.graph.exception.GraphDataSizeMismatchException;

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
	public abstract void setGraphData(GraphData data);
	
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
}
