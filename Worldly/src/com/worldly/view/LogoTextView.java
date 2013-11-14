package com.worldly.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.worldly.R;

/**
 * Class that extends TextView and uses specifically Font Awesome (http://fontawesome.io/icons/) to
 * show signs which can be used to annotate texts in the application.
 * 
 * @author Marek Matejka
 * @see {@link http://fontawesome.io/icons/} for more icons.
 */
public class LogoTextView extends TextView
{		
	
	/**
	 * General constructor for the LogoTextView view.
	 * 
	 * @param context Context of the application.
	 * @param attrs AttributeSet defined through XML (commonly).
	 */
	public LogoTextView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		
		//get the logo type code
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LogoTextView);
		int code = a.getInteger(R.styleable.LogoTextView_logo_type, -1);
		a.recycle();
				
		//sets the logo
		this.setText(Html.fromHtml(getLogo(code))); 
		
		//set the font to be Font Awesome
		this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/font_awesome.otf"));
		
		this.setClickable(true);
		this.setFocusable(true);
	}
	
	/**
	 * Returns Unicode of the logo type.
	 * 
	 * If you want to add another logo, go to <i>attrs.xml</i> create a new
	 * <i>enum</i> for it with a unique integer value, and add another <i>case</i>
	 * statement to this code with Unicode value as the return string.
	 * 
	 * @param code Logo type code.
	 * @return Unicode for the logo. 
	 */
	private String getLogo(int code)
	{
		switch(code)
		{
			case 0: return "&#xf015;"; //home
			case 1: return "&#xf0b1;"; //briefcase
			case 2: return "&#xf018;"; //road
			default: return null;
		}
	}
}