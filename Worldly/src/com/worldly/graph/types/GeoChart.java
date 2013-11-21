package com.worldly.graph.types;

import android.content.Context;

import com.worldly.graph.Chart;
import com.worldly.graph.data.GraphData;
import com.worldly.graph.data.GraphDataColumn;
import com.worldly.graph.data.GraphDataRow;
import com.worldly.graph.exception.CannotBeNullException;
import com.worldly.graph.exception.GraphDataSizeMismatchException;

/**
 * Class that represents the Geo Chart which could 
 * be displayed in GraphView.
 * 
 * @author Marek Matejka
 *
 */
public class GeoChart extends Chart
{	
	/**
	 * Basic constructor with the data for the graph.
	 * 
	 * @param data Formatted data for the graph.
	 * @param context Context of the current Activity.
	 */
	public GeoChart(GraphData data, Context context)
	{
		super(data, context);
	}
	
	/**
	 * Constructor with two columns of data.
	 * 
	 * @param names Names of rows in the data.
	 * @param values Data for the graph.
	 * @param context Context of the current Activity.
	 * @throws CannotBeNullException If any value in column or the column 
	 * itself is NULL exception is thrown.
	 */
	public GeoChart(GraphDataColumn names, GraphDataColumn values, Context context) throws CannotBeNullException
	{
		super(names, values, context);
	}
	
	/**
	 * Constructor with two rows.
	 * 
	 * @param names First row, specifying names of columns.
	 * @param values Values for specified columns.
	 * @param context Context of the current Activity.
	 * @throws GraphDataSizeMismatchException If names and values length does not match,
	 * exception is thrown.
	 */
	public GeoChart(GraphDataRow names, GraphDataRow values, Context context) throws GraphDataSizeMismatchException
	{
		super(names, values, context);
	}
		
	/* (non-Javadoc)
	 * @see com.worldly.graph.Chart#getContent()
	 */
	@Override
	public String getContent() 
	{
		return  "<html>"
              + "  <head>"
              + "    <script type=\"text/javascript\" src=\"jsapi.js\"></script>"
              + "    <script type=\"text/javascript\">"
              + "      google.load(\"visualization\", \"1\", {packages: [\"geochart\"]});"
              + "      google.setOnLoadCallback(drawRegionsMap);"
              + "      function drawRegionsMap() {"
              + "		  var data = google.visualization.arrayToDataTable("
              +			  getGraphData().toString()
              + "		  );"
              + "		  var options = {};"
              + "		  var chart = new google.visualization.GeoChart(document.getElementById('chart_div'));"
              + "		  chart.draw(data, options);"
              + "	   }"
              + "	   google.setOnLoadCallback(drawVisualization);"
              + "    </script>"
              + "  </head>"
              + "  <body>"
              + "    <div id=\"chart_div\" style=\"width: "+getChartWidth()+"px; height: "+getChartHeight()+"px;\"></div>"
              + "  </body>"
              + "</html>";
	}
}
