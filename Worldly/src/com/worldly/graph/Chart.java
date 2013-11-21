package com.worldly.graph;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

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
	 * Used to get data about the current state of the application.
	 */
	private Context context;
	
	/**
	 * Data for the graph.
	 */
	private GraphData data;
	
	/**
	 * Listener which automatically updates the graph with new data,
	 * each time the data is changed.
	 */
	private GraphDataChangeListener listener;
	
	/**
	 * Title of the vertical axis.
	 */
	private String vAxisTitle;

	/**
	 * Title of the horizontal axis.
	 */
	private String hAxisTitle;
	
	/**
	 * Title of the chart.
	 */
	private String chartTitle;
	
	/**
	 * Basic constructor which uses the fully prepared data (table)
	 * to create the graph.
	 * 
	 * @param data Already formatted data for the graph.
	 * @param context Context of the current Activity.
	 */
	public Chart(GraphData data, Context context)
	{
		this.data = data;
		this.chartTitle = "";
		this.hAxisTitle = "";
		this.vAxisTitle = "";
		this.context = context;
	}
	
	/**
	 * Constructor which uses one Column as the initial data.
	 * 
	 * @param names Column of row names.
	 * @param values Column of data to be included in the data table.
	 * @param context Context of the current Activity.
	 * @throws CannotBeNullException Thrown if some value in the column is NULL
	 * or if the column itself is NULL.
	 */
	public Chart(GraphDataColumn names, GraphDataColumn values, Context context) throws CannotBeNullException
	{
		this.data = new GraphData(names, values);
		this.chartTitle = "";
		this.hAxisTitle = "";
		this.vAxisTitle = "";
		this.context = context;
	}
	
	/**
	 * Constructor with two rows - names and values.
	 * 
	 * @param names Row with names.
	 * @param values Row with values.
	 * @param context Context of the current Activity.
	 * @throws GraphDataSizeMismatchException If length of names and values does not match.
	 */
	public Chart(GraphDataRow names, GraphDataRow values, Context context) throws GraphDataSizeMismatchException
	{
		this.data = new GraphData(names, values);
		this.chartTitle = "";
		this.hAxisTitle = "";
		this.vAxisTitle = "";
		this.context = context;
	}
	
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
	public void addColumn(GraphDataColumn column) throws CannotBeNullException, GraphDataSizeMismatchException
	{
		this.data.addColumn(column);
		listener.onGraphDataChanged(this);
	}
	
	/**
	 * Adds row to graph's current data.
	 * 
	 * @param row Row of data which will be added.
	 * @throws CannotBeNullException If row of any of its content is NULL exception is thrown.
	 * @throws GraphDataSizeMismatchException If the row's length is different to current length of row 
	 * in the GraphData exception is thrown.
	 */
	public void addRow(GraphDataRow row) throws CannotBeNullException, GraphDataSizeMismatchException
	{
		this.data.addRow(row);
		listener.onGraphDataChanged(this);
	}
	
	/**
	 * Removes a row from data on a given index.
	 * Index starts with 0.
	 * 
	 * @param index Index of row to be removed.
	 */
	public void removeRow(int index)
	{
		this.data.removeRow(index);
		listener.onGraphDataChanged(this);
	}
	
	/**
	 * Removes a column from data on a given index.
	 * 
	 * @param index Index of a column to be removed.
	 */
	public void removeColumn(int index)
	{
		this.data.removeColumn(index);
		listener.onGraphDataChanged(this);
	}
	
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
	
	/**
	 * Returns the width of the chart adjusted to screen size.
	 * 
	 * @return Screen adjusted width of the graph.
	 */
	public int getChartWidth()
	{
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
		return (int)((metrics.widthPixels/metrics.density)*0.9);
	}
	
	/**
	 * Returns the height of the chart adjusted to screen size
	 * 
	 * @return Screen adjusted height of the graph.
	 */
	public int getChartHeight()
	{
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
		
		//based on the orientation
		if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
			return (int)((metrics.heightPixels/metrics.density)*0.75);
		else
			return (int)(metrics.heightPixels/metrics.density/4);
	}
	
	/**
	 * Returns the current title of vertical axis.
	 * 
	 * @return Current vertical axis title.
	 */
	public String getVerticalAxisTitle() 
	{
		return vAxisTitle;
	}

	/**
	 * Sets the title of the vertical axis.
	 * 
	 * @param vAxisTitle New title of the vertical axis.
	 */
	public void setVerticalAxisTitle(String vAxisTitle) 
	{
		this.vAxisTitle = vAxisTitle;
		if (listener != null)
			listener.onGraphDataChanged(this);
	}

	/**
	 * Returns the current title of horizontal axis.
	 * 
	 * @return Current horizontal axis title.
	 */
	public String getHorizontalAxisTitle() 
	{
		return hAxisTitle;
	}

	/**
	 * Sets the title of the horizontal axis.
	 * 
	 * @param hAxisTitle New title of the horizontal axis.
	 */
	public void setHorizontalAxisTitle(String hAxisTitle) 
	{
		this.hAxisTitle = hAxisTitle;
		if (listener != null)
			listener.onGraphDataChanged(this);
	}

	/**
	 * Returns the current title of the chart.
	 * 
	 * @return Chart's title.
	 */
	public String getChartTitle() 
	{
		return chartTitle;
	}

	/**
	 * Sets the title of the chart.
	 * 
	 * @param chartTitle New chart's title.
	 */
	public void setChartTitle(String chartTitle) 
	{
		this.chartTitle = chartTitle;
		if (listener != null)
			listener.onGraphDataChanged(this);
	}
}
