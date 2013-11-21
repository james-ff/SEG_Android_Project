package com.worldly.graph.types;

import android.content.Context;

import com.worldly.graph.Chart;
import com.worldly.graph.data.GraphData;
import com.worldly.graph.data.GraphDataColumn;
import com.worldly.graph.data.GraphDataRow;
import com.worldly.graph.exception.CannotBeNullException;
import com.worldly.graph.exception.GraphDataSizeMismatchException;

/**
 * Class that represents the Bar Chart which could 
 * be displayed in GraphView.
 * 
 * @author Marek Matejka
 *
 */
public class BarChart extends Chart
{	
    /** Basic constructor which uses the fully prepared data (table)
	 * to create the graph.
	 * 
	 * @param data Already formatted data for the graph.
	 * @param context Context of the current Activity.
	 */
	public BarChart(GraphData data, Context context) 
	{
		super(data, context);
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
	public BarChart(GraphDataColumn names, GraphDataColumn values, Context context) throws CannotBeNullException
	{
		super(names, values, context);
	}
	
	/**
	 * Constructor with two rows - names and values.
	 * 
	 * @param names Row with names.
	 * @param values Row with values.
	 * @param context Context of the current Activity.
	 * @throws GraphDataSizeMismatchException If length of names and values does not match.
	 */
	public BarChart(GraphDataRow names, GraphDataRow values, Context context) throws GraphDataSizeMismatchException
	{
		super(names, values, context);
	}
	
	/* (non-Javadoc)
	 * @see com.worldly.graph.Chart#getContent()
	 */
	@Override
	public String getContent()
	{
		 return   	  "<html>"
	                + "  <head>"
	                + "    <script type=\"text/javascript\" src=\"jsapi.js\"></script>"
	                + "    <script type=\"text/javascript\">"
	                + "      google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
	                + "      google.setOnLoadCallback(drawChart);"
	                + "      function drawChart() {"
	                + "        var data = google.visualization.arrayToDataTable("
	                + 			getGraphData().toString()+");"
	                + "        var options = {"
	                + "          title: '"+getChartTitle()+"',"
	                + " 	     width: "+getChartWidth()+", height: "+getChartHeight()+","
	                + "          hAxis: {title: '"+getHorizontalAxisTitle()+"'},"
	                + "			 vAxis: {title: '"+getVerticalAxisTitle()+"'}};"
	               // + "			 +"};"
	                + "        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));"
	                + "        chart.draw(data, options);}"
	                + "    </script>"
	                + "  </head>"
	                + "  <body>"
	                + "    <div id=\"chart_div\" style=\"width: "+getChartWidth()+"px; height: "+getChartHeight()+"px;\"></div>"
	                + "  </body>"
	                + "</html>";
	}
}
