package com.example.lossqrcode.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.util.Vector;

public class ImageUtil {

	public static Bitmap makeTextBitMap1(int flag, int w, int h, String title, String addr, String date,
										 Bitmap b, String userid, String carNo) {
		if(addr==null||"".equals(addr)){
			addr="未定位到地址";
		}
		
		String[] stringFormat = StringFormat(addr, w, 18);

		Bitmap icon = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888); // 建立一个空的BItMap
		Canvas canvas = new Canvas(icon);// 初始化画布 绘制的图像到icon上

		Paint photoPaint = new Paint(); // 建立画笔
		photoPaint.setDither(true); // 获取跟清晰的图像采样
		photoPaint.setFilterBitmap(true);// 过滤一些

		Rect src = new Rect(0, 0, b.getWidth(), b.getHeight());// 创建一个指定的新矩形的坐标
		Rect dst = new Rect(0, 0, w, h);// 创建一个指定的新矩形的坐标
		canvas.drawBitmap(b, src, dst, photoPaint);// 将photo 缩放或则扩大到
													// dst使用的填充区photoPaint

		Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.DEV_KERN_TEXT_FLAG);// 设置画笔
		textPaint.setTextSize(13.0f);// 字体大小
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);// 采用默认的宽度
		textPaint.setColor(Color.BLUE);// 采用的颜色
		
		int j = 20;
//		String title ="";
//		if(2==flag){
//        	title = "到达现场";
//        }else if(3==flag){
//        	title = "开始拖车";
//        }else if(4==flag){
//        	title = "到达目的地";
//        }else if(5==flag){
//        	title = "单据照";
//        }
		canvas.drawText(title, 0, j, textPaint);
		j = j + 20;
		for (int i = 0; i < stringFormat.length; i++) {
			canvas.drawText(stringFormat[i], 0, j, textPaint);
			j = j + 20;
		}
		canvas.drawText(date, 0, j + 20, textPaint);
		canvas.drawText(carNo, 0, j, textPaint);

		return icon;

	}

	public static Bitmap makeTextBitMap(int flag, int w, int h, String title, String addr, String date,
										Bitmap b, String userid, String carNo) {
		if(addr==null||"".equals(addr)){
			addr="未定位到地址";
		}
		
		String[] stringFormat = StringFormat(addr, w, 22);

		Bitmap icon = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888); // 建立一个空的BItMap
		Canvas canvas = new Canvas(icon);// 初始化画布 绘制的图像到icon上

		Paint photoPaint = new Paint(); // 建立画笔
		photoPaint.setDither(true); // 获取跟清晰的图像采样
		photoPaint.setFilterBitmap(true);// 过滤一些

		Rect src = new Rect(0, 0, b.getWidth(), b.getHeight());// 创建一个指定的新矩形的坐标
		Rect dst = new Rect(0, 0, w, h);// 创建一个指定的新矩形的坐标
		canvas.drawBitmap(b, src, dst, photoPaint);// 将photo 缩放或则扩大到
													// dst使用的填充区photoPaint

		Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.DEV_KERN_TEXT_FLAG);// 设置画笔
		textPaint.setTextSize(18.0f);// 字体大小
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);// 采用默认的宽度
		textPaint.setColor(Color.BLUE);// 采用的颜色
		
		int j = 20;
//		String title ="";
//		if(2==flag){
//        	title = "到达现场";
//        }else if(3==flag){
//        	title = "开始拖车";
//        }else if(4==flag){
//        	title = "到达目的地";
//        }else if(5==flag){
//        	title = "单据照";
//        }
		canvas.drawText(title, 0, j, textPaint);
		j = j + 20;
		for (int i = 0; i < stringFormat.length; i++) {
			canvas.drawText(stringFormat[i], 0, j, textPaint);
			j = j + 20;
		}
		canvas.drawText(date, 0, j + 20, textPaint);
		canvas.drawText(carNo, 0, j, textPaint);

		return icon;

	}

	public static String[] StringFormat(String text, int maxWidth, int fontSize) {

		String[] result = null;

		Vector<String> tempR = new Vector<String>();

		int lines = 0;

		int len = text.length();

		int index0 = 0;

		int index1 = 0;

		boolean wrap;

		while (true) {

			int widthes = 0;

			wrap = false;

			for (index0 = index1; index1 < len; index1++) {

				if (text.charAt(index1) == '\n') {

					index1++;

					wrap = true;

					break;

				}

				widthes = fontSize + widthes;

				if (widthes > maxWidth) {

					break;

				}

			}

			lines++;

			if (wrap) {

				tempR.addElement(text.substring(index0, index1 - 1));

			} else {

				tempR.addElement(text.substring(index0, index1));

			}

			if (index1 >= len) {

				break;

			}

		}

		result = new String[lines];

		tempR.copyInto(result);

		return result;

	}

}
