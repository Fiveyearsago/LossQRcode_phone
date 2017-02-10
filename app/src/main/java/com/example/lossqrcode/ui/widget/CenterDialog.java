package com.example.lossqrcode.ui.widget;

import com.example.lossqrcode.R;
import com.example.lossqrcode.utils.DensityUtil;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;


public class CenterDialog extends Dialog {

	public CenterDialog(Activity context, int layout) {
		this(context,layout,R.style.Dialog);
	}

	public CenterDialog(Activity context,int layout,
			int style) {
		super(context, style);
		setContentView(layout);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		
		int screenWidth=DensityUtil.getScreenW(context);
		params.width = (int) (screenWidth *4/5);
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);
	}
}
