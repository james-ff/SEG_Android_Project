package com.worldly.graph.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.worldly.graph.Chart;

/**
 * View that loads and displays the graph.
 * 
 * @author Marek Matejka
 *
 */
public class GraphView extends WebView
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
		WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        loadDataWithBaseURL( "file:///android_asset/", chart.getContent(), "text/html", "utf-8", null );
	}

}
