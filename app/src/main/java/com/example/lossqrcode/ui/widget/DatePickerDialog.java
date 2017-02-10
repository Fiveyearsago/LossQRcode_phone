package com.example.lossqrcode.ui.widget;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.lossqrcode.R;
import com.example.lossqrcode.ui.wheelview.NumericWheelAdapter;
import com.example.lossqrcode.ui.wheelview.OnWheelChangedListener;
import com.example.lossqrcode.ui.wheelview.WheelView;

public class DatePickerDialog extends CenterDialog implements android.view.View.OnClickListener{
	private static int START_YEAR = 2000,END_YEAR=2100;
	private final OnDateTimeSetListener mCallBack;
	private final Calendar mCalendar;
	private int day,month,year,year_num,month_num;
	private int curr_year, curr_month, curr_day,startYear,endYear;
	// ��Ӵ�С���·ݲ�����ת��Ϊlist,����֮����ж�
	String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
	String[] months_little = { "4", "6", "9", "11" };
	final WheelView wv_year, wv_month, wv_day;
	final List<String> list_big, list_little;
	
	


	public DatePickerDialog(Activity context,
			OnDateTimeSetListener callBack) {
		this(context, START_YEAR,END_YEAR,callBack);
	}

	public DatePickerDialog(Activity context, final int START_YEAR,
			 final int END_YEAR,OnDateTimeSetListener callBack) {
		super(context, R.layout.dialog_birthday_selection_layout);
		
		
		mCalendar = Calendar.getInstance();
		year = mCalendar.get(Calendar.YEAR);
		month = mCalendar.get(Calendar.MONTH);
		day = mCalendar.get(Calendar.DATE);
		
		endYear=year;
		startYear=endYear-99;
		
		mCallBack = callBack;

		list_big = Arrays.asList(months_big);
		list_little = Arrays.asList(months_little);
		// �ҵ�dialog�Ĳ����ļ�
		TextView tvCancel=(TextView)findViewById(R.id.tv_cancel);
		TextView tvSure=(TextView)findViewById(R.id.tv_sure);
		
		tvCancel.setOnClickListener(this);
		tvSure.setOnClickListener(this);
		
		int textSize = 0;
		textSize = adjustFontSize(getWindow().getWindowManager()); 
		// ��
		wv_year = (WheelView) findViewById(R.id.year);
		wv_year.setAdapter(new NumericWheelAdapter(startYear, endYear));// ����"��"����ʾ����
		wv_year.setCyclic(false);// ��ѭ������
		wv_year.setLabel("��");// �������
		wv_year.setCurrentItem(year - startYear);// ��ʼ��ʱ��ʾ������
		// ��
		wv_month = (WheelView) findViewById(R.id.month);
		wv_month.setAdapter(new NumericWheelAdapter(1, month+1));
		wv_month.setCyclic(false);
		wv_month.setLabel("��");
		wv_month.setCurrentItem(month);

		// ��
		wv_day = (WheelView)findViewById(R.id.day);
		wv_day.setCyclic(false);
		// �жϴ�С�¼��Ƿ�����,����ȷ��"��"������
		if (list_big.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, day));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, day));
		} else {
			// ����
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wv_day.setAdapter(new NumericWheelAdapter(1, day));
			else
				wv_day.setAdapter(new NumericWheelAdapter(1, day));
		}
		wv_day.setLabel("��");
		wv_day.setCurrentItem(day - 1);

		// ���"��"����
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				year_num = newValue + startYear;
				// �жϴ�С�¼��Ƿ�����,����ȷ��"��"������
				if(year_num!=year){//���ѡ�зǵ�ǰ�������
					wv_month.setAdapter(new NumericWheelAdapter(1, 12));
					if (list_big
							.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
						wv_day.setAdapter(new NumericWheelAdapter(1, 31));
					} else if (list_little.contains(String.valueOf(wv_month
							.getCurrentItem() + 1))) {
						wv_day.setAdapter(new NumericWheelAdapter(1, 30));
					} else {
						if ((year_num % 4 == 0 && year_num % 100 != 0)
								|| year_num % 400 == 0)
							wv_day.setAdapter(new NumericWheelAdapter(1, 29));
						else
							wv_day.setAdapter(new NumericWheelAdapter(1, 28));
					}
				}else{//���ѡ��Ϊ��ǰ�������
					wv_month.setAdapter(new NumericWheelAdapter(1, month+1));
					if(month_num==month+1){//���ѡ�е�ǰ�·�
						wv_day.setAdapter(new NumericWheelAdapter(1, day));
					}else{
						if (list_big
								.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
							wv_day.setAdapter(new NumericWheelAdapter(1, day));
						} else if (list_little.contains(String.valueOf(wv_month
								.getCurrentItem() + 1))) {
							wv_day.setAdapter(new NumericWheelAdapter(1, 30));
						} else {
							if ((year_num % 4 == 0 && year_num % 100 != 0)
									|| year_num % 400 == 0)
								wv_day.setAdapter(new NumericWheelAdapter(1, 29));
							else
								wv_day.setAdapter(new NumericWheelAdapter(1, 28));
						}
					}
				}
				
				
			}
		};
		// ���"��"����
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				month_num = newValue + 1;
				// �жϴ�С�¼��Ƿ�����,����ȷ��"��"������
				if(month_num==month+1&&year_num==year){
					wv_day.setAdapter(new NumericWheelAdapter(1, day));
				}else{
					if (list_big.contains(String.valueOf(month_num))) {
						wv_day.setAdapter(new NumericWheelAdapter(1, 31));
					} else if (list_little.contains(String.valueOf(month_num))) {
						wv_day.setAdapter(new NumericWheelAdapter(1, 30));
					} else {
						if (((wv_year.getCurrentItem() + startYear) % 4 == 0 && (wv_year
								.getCurrentItem() + startYear) % 100 != 0)
								|| (wv_year.getCurrentItem() + startYear) % 400 == 0)
							wv_day.setAdapter(new NumericWheelAdapter(1, 29));
						else
							wv_day.setAdapter(new NumericWheelAdapter(1, 28));
					}
				}
				
			}
		};
		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);
		wv_day.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;
	}
	 public void show() {
	        super.show();
	 }
	public  interface OnDateTimeSetListener {
			void onDateTimeSet(int year, int monthOfYear, int dayOfMonth);
		}
	public static int adjustFontSize(WindowManager windowmanager) {

		 int screenWidth = windowmanager.getDefaultDisplay().getWidth();
	     int screenHeight = windowmanager.getDefaultDisplay().getHeight();
	     /*  DisplayMetrics dm = new DisplayMetrics();
	      dm = windowmanager.getApplicationContext().getResources().getDisplayMetrics();
	     int widthPixels = dm.widthPixels;
	     int heightPixels = dm.heightPixels;
	     float density = dm.density;
	     fullScreenWidth = (int)(widthPixels * density);
	     fullScreenHeight = (int)(heightPixels * density);*/
		if (screenWidth <= 240) { // 240X320 ��Ļ      0.75
			return 12;
		} else if (screenWidth <= 320) { // 320X480 ��Ļ      1
			return 16;
		} else if (screenWidth <= 480) { // 480X800 �� 480X854 ��Ļ      1.5
			return 24;
		} else if (screenWidth <= 540) { // 540X960 ��Ļ     1.68
			return 26;
		} else if (screenWidth <= 800) { // 720X1280 ��Ļ     2.25
			return 36;
		} else { // ���� 800X1280   3.375
			return 54;
		}
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.tv_cancel:
			dismiss();
			break;
			
		case R.id.tv_sure:
			curr_year = wv_year.getCurrentItem() + startYear;
			curr_month = wv_month.getCurrentItem();
			curr_day = wv_day.getCurrentItem() + 1;
			if (mCallBack != null) {
				mCallBack.onDateTimeSet(curr_year, curr_month, curr_day);
			}
			dismiss();
			break;

		default:
			break;
		}
		// TODO Auto-generated method stub
		
	}
}
