package com.example.lossqrcode.ui;

import java.lang.reflect.Field;

import com.example.lossqrcode.MyApplication;
import com.example.lossqrcode.R;
import com.example.lossqrcode.utils.SystemMethod;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BaseActivity extends FragmentActivity {

    private Button btnLeft;
    private Button btnTitle;
    private Button btnRight;
    private Button btnRightLeft;
    private MyApplication mApplication;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getActionBar();
        setActionBarLayout(actionBar, R.layout.action_bar);
        mApplication = (MyApplication) getApplication();
        findView();

    }
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
    private void findView() {
        btnLeft = (Button) findViewById(R.id.btn_bar_left);
        btnTitle = (Button) findViewById(R.id.btn_bar_title);
        btnRight = (Button) findViewById(R.id.btn_bar_right);
        btnRightLeft = (Button) findViewById(R.id.btn_bar_rightleft);
    }

    public void setBarViewVisible(int leftVisible, int titleVisible,
                                  int rightVisible) {
        btnLeft.setVisibility(leftVisible);
        btnTitle.setVisibility(titleVisible);
        btnRight.setVisibility(rightVisible);

    }

    public void setBarTitle(String title) {
        btnTitle.setText(title);
    }

    public void setBarLeftOnclickListener(OnClickListener listener) {
        btnLeft.setOnClickListener(listener);
    }

    public void setBarRightOnclickListener(OnClickListener listener) {
        btnRight.setOnClickListener(listener);
    }
    
    public void setBarRightLeftOnclickListener(OnClickListener listener) {
        btnRightLeft.setOnClickListener(listener);
    }

    public Button getBtnLeft() {
        return btnLeft;
    }

    public Button getBtnTitle() {
        return btnTitle;
    }

    public Button getBtnRight() {
        return btnRight;
    }
    
    public Button getBtnRightLeft() {
        return btnRightLeft;
    }

    public void setBarLeft(int icon, String string) {
        Drawable img = this.getResources().getDrawable(icon);
        int height = SystemMethod.dip2px(this, 25);
        int width = img.getIntrinsicWidth() * height / img.getIntrinsicHeight();
        img.setBounds(0, 0, width , height);
        btnLeft.setText(string);
        btnLeft.setCompoundDrawables(img, null, null, null);
    }

    public void setBarRightIcon(int icon) {
        Drawable img = this.getResources().getDrawable(icon);
        int height = SystemMethod.dip2px(this, 35);
        int width = img.getIntrinsicWidth() * height / img.getIntrinsicHeight();
        img.setBounds(0, 0, width, height);
        btnRight.setCompoundDrawables(null, null, img, null);
    }
    
    public void setBarRightLeftIcon(int icon) {
        Drawable img = this.getResources().getDrawable(icon);
        int height = SystemMethod.dip2px(this, 35);
        int width = img.getIntrinsicWidth() * height / img.getIntrinsicHeight();
        img.setBounds(0, 0, width, height);
        btnRightLeft.setCompoundDrawables(null, null, img, null);
    }

    public void setBarRightString(String string) {

        btnRight.setText(string);
    }
    
    public void setBarRightLeftString(String string) {

        btnRightLeft.setText(string);
    }

    public void setActionBarLayout(ActionBar actionBar, int layoutId) {

        if (null != actionBar) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            LayoutInflater inflator = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflator.inflate(layoutId, null);
            ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            actionBar.setCustomView(v, layout);

        }

    }

    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        overridePendingTransition(R.anim.move_right_in_activity,
                R.anim.move_left_out_activity);
    }

    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.move_right_in_activity,
                R.anim.move_left_out_activity);
    }

    public MyApplication getmApplication() {
        return mApplication;
    }

    public SharedPreferences getSp() {
        return mApplication.getSp();
    }

    public Editor getSpEditor() {
        return mApplication.getSpEditor();
    }

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.move_left_in_activity,
				R.anim.move_right_out_activity);
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
