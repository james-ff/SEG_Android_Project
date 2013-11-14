package com.worldly.graph.types;

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
	 * Data for the graph.
	 */
	GraphData data;
	
	/**
	 * Basic constructor with the data for the graph.
	 * 
	 * @param data Formatted data for the graph.
	 */
	public GeoChart(GraphData data)
	{
		this.data = data;
	}
	
	/**
	 * Constructor with one column of data.
	 * 
	 * @param column Data for the graph.
	 * @throws CannotBeNullException If any value in column or the column 
	 * itself is NULL exception is thrown.
	 */
	public GeoChart(GraphDataColumn column) throws CannotBeNullException
	{
		this.data = new GraphData(column);
	}
	
	/**
	 * Constructor with two rows.
	 * 
	 * @param names First row, specifying names of columns.
	 * @param values Values for specified columns.
	 * @throws GraphDataSizeMismatchException If names and values length does not match,
	 * exception is thrown.
	 */
	public GeoChart(GraphDataRow names, GraphDataRow values) throws GraphDataSizeMismatchException
	{
		this.data = new GraphData(names, values);
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
              +			  data.toString()
              + "		  );"
              + "		  var options = {};"
              + "		  var chart = new google.visualization.GeoChart(document.getElementById('chart_div'));"
              + "		  chart.draw(data, options);"
              + "	   }"
              + "	   google.setOnLoadCallback(drawVisualization);"
              + "    </script>"
              + "  </head>"
              + "  <body>"
              + "    <div id=\"chart_div\" style=\"width: 900px; height: 500px;\"></div>"
              + "  </body>"
              + "</html>";
	}

	/* (non-Javadoc)
	 * @see com.worldly.graph.Chart#setGraphData(com.worldly.graph.data.GraphData)
	 */
	@Override
	public void setGraphData(GraphData data) 
	{
		this.data = data;
	}

	/* (non-Javadoc)
	 * @see com.worldly.graph.Chart#addColumn(com.worldly.graph.data.GraphDataColumn)
	 */
	@Override
	public void addColumn(GraphDataColumn column) throws CannotBeNullException, GraphDataSizeMismatchException 
	{
		this.data.addColumn(column);
	}

	/* (non-Javadoc)
	 * @see com.worldly.graph.Chart#addRow(com.worldly.graph.data.GraphDataRow)
	 */
	@Override
	public void addRow(GraphDataRow row) throws CannotBeNullException, GraphDataSizeMismatchException 
	{
		this.data.addRow(row);
	}
	
}
