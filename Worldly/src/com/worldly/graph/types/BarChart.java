package com.worldly.graph.types;

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
	 */
	public BarChart(GraphData data) 
	{
		super(data);
	}
	
	/**
	 * Constructor which uses one Column as the initial data.
	 * 
	 * @param names Column of row names.
	 * @param values Column of data to be included in the data table.
	 * @throws CannotBeNullException Thrown if some value in the column is NULL
	 * or if the column itself is NULL.
	 */
	public BarChart(GraphDataColumn names, GraphDataColumn values) throws CannotBeNullException
	{
		super(names,values);
	}
	
	/**
	 * Constructor with two rows - names and values.
	 * 
	 * @param names Row with names.
	 * @param values Row with values.
	 * @throws GraphDataSizeMismatchException If length of names and values does not match.
	 */
	public BarChart(GraphDataRow names, GraphDataRow values) throws GraphDataSizeMismatchException
	{
		super(names, values);
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
	                + 			data.toString()
	                + ");"
	                + "        var options = {"
	                + "          title: 'Performance',"
	                + "          hAxis: {title: 'Year', titleTextStyle: {color: 'red'}}"
	                + "        };"
	                + "        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));"
	                + "        chart.draw(data, options);"
	                + "      }"
	                + "    </script>"
	                + "  </head>"
	                + "  <body>"
	                + "    <div id=\"chart_div\" style=\"width: 500px; height: 300px;\"></div>"
	                + "  </body>"
	                + "</html>";
	}

	/* (non-Javadoc)
	 * @see com.worldly.graph.Chart#addColumn(com.worldly.graph.data.GraphDataColumn)
	 */
	@Override
	public void addColumn(GraphDataColumn column) throws CannotBeNullException, GraphDataSizeMismatchException 
	{
		this.data.addColumn(column);
		listener.onGraphDataChanged(this);
	}

	/* (non-Javadoc)
	 * @see com.worldly.graph.Chart#addRow(com.worldly.graph.data.GraphDataRow)
	 */
	@Override
	public void addRow(GraphDataRow row) throws CannotBeNullException, GraphDataSizeMismatchException 
	{
		this.data.addRow(row);
		listener.onGraphDataChanged(this);
	}

	/* (non-Javadoc)
	 * @see com.example.seg_graph.graph.Chart#removeRow(int)
	 */
	@Override
	public void removeRow(int index) 
	{
		this.data.removeRow(index);
		listener.onGraphDataChanged(this);
	}

	/* (non-Javadoc)
	 * @see com.example.seg_graph.graph.Chart#removeColumn(int)
	 */
	@Override
	public void removeColumn(int index)
	{
		this.data.removeColumn(index);
		listener.onGraphDataChanged(this);
	}
}
