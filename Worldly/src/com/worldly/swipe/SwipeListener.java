package com.worldly.swipe;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/**
 * Abstract class that provides easy and straight forward implementation of swipe gestures in all four directions.
 * 
 * @author Marek Matejka
 */
public abstract class SwipeListener extends SimpleOnGestureListener
{
	private final int SWIPE_MAX_OFF_PATH = 200; //specifies by how many pixels the swipe can be off at max
	private final int SWIPE_MIN_DISTANCE = 120; //defines the min distance of the swipe
	private final int SWIPE_MAX_VELOCITY = 200; //defines the max (threshold) velocity of the swipe
	
	/* (non-Javadoc)
	 * @see android.view.GestureDetector.SimpleOnGestureListener#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 */
	@Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) //na tahu prstu po obrazovke
    {

		if (Math.abs(e1.getY() - e2.getY()) <= SWIPE_MAX_OFF_PATH) //if the swipe is not too far from Y axis
		{
			if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_MAX_VELOCITY) //if the swipe is long and fast enough
				return onRightToLeftSwipe();
		    else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_MAX_VELOCITY)  //if the swipe is long and fast enough
				return onLeftToRightSwipe(); 	
		}
		else if (Math.abs(e1.getX() - e2.getX()) <= SWIPE_MAX_OFF_PATH) //if the swipe is not too far from Y axis
		{
			if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_MAX_VELOCITY) //if the swipe is long and fast enough
				return onBottomToTopSwipe();
		    else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_MAX_VELOCITY) //if the swipe is long and fast enough
				return onTopToBottomSwipe();
		}
        return false; //else return FALSE
    }
	
	/**
	 * Method triggered when a swipe from left to right of the screen is identified.
	 * The swipe must meet basic parameters which are always adjusted to screen rotation. 
	 * 
	 * @return TRUE if action happened, FALSE if not.
	 */
	public abstract boolean onLeftToRightSwipe();
	
	/**
	 * Method triggered when a swipe from right to left of the screen is identified.
	 * The swipe must meet basic parameters which are always adjusted to screen rotation. 
	 * 
	 * @return TRUE if action happened, FALSE if not.
	 */
	public abstract boolean onRightToLeftSwipe();
	
	/**
	 * Method triggered when a swipe from bottom to top of the screen is identified.
	 * The swipe must meet basic parameters which are always adjusted to screen rotation. 
	 * 
	 * @return TRUE if action happened, FALSE if not.
	 */
	public abstract boolean onBottomToTopSwipe();
	
	/**
	 * Method triggered when a swipe from top to bottom of the screen is identified.
	 * The swipe must meet basic parameters which are always adjusted to screen rotation. 
	 * 
	 * @return TRUE if action happened, FALSE if not.
	 */
	public abstract boolean onTopToBottomSwipe();
}