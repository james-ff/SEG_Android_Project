package com.worldly.graph;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.worldly.R;
import com.worldly.graph.data.GraphData;
import com.worldly.graph.data.GraphDataColumn;
import com.worldly.graph.data.GraphDataRow;
import com.worldly.graph.exception.CannotBeNullException;
import com.worldly.graph.exception.GraphDataSizeMismatchException;
import com.worldly.graph.types.BarChart;
import com.worldly.graph.view.GraphView;
import com.worldly.network.QuerySystem;

public class GraphTestActivity extends Activity {

	Chart chart;
	GraphData data;
	final String key = "TEST";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph_test);
		
		GraphView chartView = (GraphView) findViewById(R.id.graphView1);
		
		if (savedInstanceState == null)
		{	
			try{
			GraphDataRow names = new GraphDataRow("Country", "Population", true);
			
			GraphDataRow values1 = new GraphDataRow("Slovakia", 1500, false);
			
			GraphDataRow values2 = new GraphDataRow("Russia", 1100, false);
			
			GraphDataRow values3 = new GraphDataRow("USA", 500, false);
			
			GraphDataRow values4 = new GraphDataRow("GB", 750, false);  
			
			GraphDataColumn column = new GraphDataColumn("Test", 500);
			column.addColumnData(400);
			column.addColumnData(300);
			column.addColumnData(500);
			
			/*GraphDataColumn column2 = new GraphDataColumn("TEST2", 500);
			column2.addColumnData(400);
			column2.addColumnData(300);
			column2.addColumnData(500);
			
			GraphDataColumn column3 = new GraphDataColumn("TEST2", 500);
			column2.addColumnData(400);
			column2.addColumnData(300);
			column2.addColumnData(500);*/
			
			data = new GraphData(names, values1);
			data.addRow(values2);
			data.addRow(values3);
			data.addRow(values4);
			//data.addColumn(column);
			//data.addColumn(column2);
			
			}catch (CannotBeNullException e){e.printStackTrace();}
			catch(GraphDataSizeMismatchException e){e.printStackTrace();}
			
		}
		else
		{
			data = (GraphData)savedInstanceState.getSerializable(key);
		}
		
		Button one = (Button) findViewById(R.id.button1);
		final Button two = (Button) findViewById(R.id.button2);
		Button three = (Button) findViewById(R.id.button3);
			
		one.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) {
				two.setVisibility(View.VISIBLE);
			
				GraphDataColumn column2 = new GraphDataColumn("KCL", 500);
				column2.addColumnData(400);
				column2.addColumnData(300);
				column2.addColumnData(500);
					
				try {
					chart.addColumn(column2);
				} catch (CannotBeNullException e) {
					e.printStackTrace();
				} catch (GraphDataSizeMismatchException e) {
					e.printStackTrace();
				}
			}
		});
			
		three.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) {
				two.setVisibility(View.GONE);
				chart.removeColumn(chart.getNumberOfColumns()-1);
			}
		});
		
		two.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) {
				//chart.removeRow(chart.getNumberOfRows()-1);
				chart.setChartTitle("Cauu Marek");
			}
		});
			
		chart = new BarChart(data, this);
		
		chart.setChartTitle("Ahoj Marek");
		chart.setVerticalAxisTitle("vertical");
		chart.setHorizontalAxisTitle("horizontal");
		
		chartView.loadGraph(chart);
		
		Thread aThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Log.e("DEBUG", QuerySystem.getIndicatorData("SK", "NY.GDP.MKTP.KD.ZG"));
			}
		});
		aThread.start();
	}
	
	@Override
	public void onSaveInstanceState(Bundle b)
	{
		super.onSaveInstanceState(b);
		if (data != null)
			b.putSerializable(key, data);
	}
}
	