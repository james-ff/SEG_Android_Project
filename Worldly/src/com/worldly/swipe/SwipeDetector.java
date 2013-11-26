package com.worldly.swipe;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Class that provides implementation of SwipeListener.
 * 
 * @author Marek Matejka
 */
public class SwipeDetector implements View.OnTouchListener
{
	private GestureDetector gd;
	
	/**
	 * Constructor for SwipeDetector.
	 * 
	 * @param context Context of the Activity from which the swipe came.
	 * @param swipeListener Instance of a SwipeListener class with defined actions (abstract methods).
	 */
	public SwipeDetector(Context context, SwipeListener swipeListener)
	{
		gd = new GestureDetector(context, swipeListener);
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
		return gd.onTouchEvent(event);
	}
}
