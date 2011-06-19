package com.panaceasupples.android.grad;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

public class MyView extends View {

	public MyView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	

/*
	void OnKeyListener() {
//		super.OnKeyListener();
	}
*/
	boolean onKey(int i, KeyEvent k) {
//		Log.d("gradkey",i + " " + k.toString());
		int h = getHeight();
		int down = getNextFocusDownId();
		int right = getNextFocusRightId();
   //     int action = getAction();

		Log.d("gradkey",i + " " + h + " " + down + " " + right);
		
		if (i == KeyEvent.KEYCODE_DPAD_DOWN) {
			Log.d("gradkey", "dpad-down");
			boolean handled;
//			 handled = arrowScroll(FOCUS_RIGHT);
//			 handled = super.arrowScroll(v.FOCUS_RIGHT);
		}
		else if (i == KeyEvent.KEYCODE_DPAD_RIGHT) {
			Log.d("gradkey", "dpad-right");
		}
		
		
		
		
	//	super.onKey();
		return true;
	}

	
}
