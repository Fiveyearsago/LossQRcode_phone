package com.example.lossqrcode.ui.widget;


import com.example.lossqrcode.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class CustomDialog extends Dialog {


	public CustomDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomDialog(Context context, int layoutId) {
		super(context, R.style.custom_dialog2);
		int outsideMenuWidth = 0;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout dialogView = (LinearLayout) inflater.inflate(layoutId,
				null);

		WindowManager.LayoutParams localLayoutParams = getWindow()
				.getAttributes();
		localLayoutParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
		localLayoutParams.x = outsideMenuWidth;
		localLayoutParams.y = 0;

		int screenWidth = ((Activity) context).getWindowManager()
				.getDefaultDisplay().getWidth();
		dialogView.setMinimumWidth(screenWidth - outsideMenuWidth);
		// dialogView.setMinimumHeight(10);
		
		

		onWindowAttributesChanged(localLayoutParams);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setCanceledOnTouchOutside(false);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
		setContentView(dialogView);

		if (context instanceof Activity) {
			Activity activity = (Activity) context;
			if (!activity.isFinishing()) {
				show();
			}
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
	}

}
