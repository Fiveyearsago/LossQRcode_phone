package com.example.lossqrcode.ui.widget;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.ListView;

public class MyListView extends ListView {

	public MyListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	  public boolean dispatchTouchEvent(MotionEvent event) {
	    // TODO Auto-generated method stub
	    switch (event.getAction()) {
	    case MotionEvent.ACTION_DOWN:
	      performClick();
	      break;

	    default:
	      break;
	    }
	    return true;
	  }
}
