package com.example.lossqrcode.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class MyTestView extends TextView {
	public MyTestView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyTestView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyTestView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public boolean dispatchTouchEvent(MotionEvent event) {  
		switch (event.getAction()) {
	    case MotionEvent.ACTION_DOWN:
	      performClick();
	      break;

	    default:
	      break;
	    }
	    return false;
    }
}
