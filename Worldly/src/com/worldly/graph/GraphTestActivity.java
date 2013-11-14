package com.worldly.graph;

import android.app.Activity;
import android.os.Bundle;

import com.example.worldly.R;
import com.worldly.graph.data.GraphData;
import com.worldly.graph.data.GraphDataColumn;
import com.worldly.graph.data.GraphDataRow;
import com.worldly.graph.exception.CannotBeNullException;
import com.worldly.graph.exception.GraphDataSizeMismatchException;
import com.worldly.graph.types.BarChart;
import com.worldly.graph.view.GraphView;

/**
 * Class to show how to use GraphView, how to prepare GraphData
 * and how to load the Chart into the GraphView.
 * 
 * @author Marek Matejka
 * @see {@link GraphData}, {@link GraphView}, {@link Chart}
 *
 */
public class GraphTestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph_test);
		
		//get the GraphView
		GraphView geoChart = (GraphView) findViewById(R.id.graphView1);
		
		try{ //create GraphData by adding Rows and Columns to the GraphData.
		GraphDataRow names = new GraphDataRow("Country", "Population", true);
		
		GraphDataRow values1 = new GraphDataRow("Slovakia", 1500, false);
		
		GraphDataRow values2 = new GraphDataRow("Russia", 1100, false);
		
		GraphDataRow values3 = new GraphDataRow("USA", 500, false);
		
		GraphDataRow values4 = new GraphDataRow("GB", 750, false);  
		
		//column must contain the same amount of rows as the current GraphData!
		GraphDataColumn column = new GraphDataColumn("Test", 500);
		column.addColumnData(400);
		column.addColumnData(300);
		column.addColumnData(500);
		
		GraphDataColumn column2 = new GraphDataColumn("TEST2", 500);
		column2.addColumnData(400);
		column2.addColumnData(300);
		column2.addColumnData(500);
		
		//add all columns and rows into the GraphData (table)
		GraphData data = new GraphData(names, values1);
		data.addRow(values2);
		data.addRow(values3);
		data.addRow(values4);
		data.addColumn(column);
		data.addColumn(column2);
		
		//load BarChart with the given data
		geoChart.loadGraph(new BarChart(data));
		
		}catch (CannotBeNullException e){e.printStackTrace();}
		catch(GraphDataSizeMismatchException e){e.printStackTrace();}
	}	
}
	