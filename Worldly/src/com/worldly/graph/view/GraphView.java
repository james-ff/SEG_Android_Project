package com.worldly.graph.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.worldly.graph.Chart;
import com.worldly.graph.listener.GraphDataChangeListener;

/**
 * View that loads and displays the graph.
 * 
 * @author Marek Matejka
 *
 */
public class GraphView extends WebView implements GraphDataChangeListener
{
	
	/**
	 * Default constructor for GraphView.
	 * 
	 * @param context Context of the application.
	 * @param attrs Attributes specified for this View (usually through XML).
	 */
	public GraphView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
	}

	/**
	 * Loads and displays the given graph into the view.
	 * 
	 * @param chart Graph to be loaded and displayed..
	 */
	@SuppressLint("SetJavaScriptEnabled")
	public void loadGraph(Chart chart)
	{
		chart.setGraphDataChangeListener(this);
		WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        loadDataWithBaseURL( "file:///android_asset/", chart.getContent(), "text/html", "utf-8", null );
	}

	/* (non-Javadoc)
	 * @see com.example.seg_graph.graph.listener.GraphDataChangeListener#onGraphDataChanged(com.example.seg_graph.graph.Chart)
	 */
	@Override
	public void onGraphDataChanged(Chart chart)
	{
		loadGraph(chart);
	}

}
