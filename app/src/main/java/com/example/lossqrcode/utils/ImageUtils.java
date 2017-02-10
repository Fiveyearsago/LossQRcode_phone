package com.example.lossqrcode.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageUtils {

	private static final int UNCONSTRAINED = -1;
	public static final int MAX_PIXELS = 614400;
	public static final int MIN_PIXELS = 307200;

	// 得到屏幕的高度
	public static int getScreenH(Context context) {
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(mDisplayMetrics);
		int h = mDisplayMetrics.heightPixels;
		return h;
	}

	// 得到屏幕的宽度
	public static int getScreenW(Context context) {
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(mDisplayMetrics);
		int w = mDisplayMetrics.widthPixels;
		return w;
	}

	public static void makeDirs(String filePath) {
		File file = new File(filePath);
		// 如果文件夹不存在则创建
		if (!file.exists() && !file.isDirectory()) {
			Log.i("dir", "目录不存在");
			file.mkdir();
		} else {
			Log.i("dir", "目录存在");
		}
	}

	// 保存Bitmap到SDCard
	public static void saveMyBitmap(String bitName, Bitmap mBitmap)
			throws IOException {
		File f = new File(bitName);
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 压缩并保存
	 * @param flag
	 * @param outImgPath
	 * @param title
	 * @param addr
	 * @param partId
     * @param id
     * @param carNo
     */
	public static void compressBitmap(int flag, String outImgPath,
									  String title, String addr, String partId,String id, String carNo) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		Bitmap b = BitmapFactory.decodeFile(outImgPath, opts);
		opts.inJustDecodeBounds = false;
		// opts.inSampleSize=computeSampleSize(opts, UNCONSTRAINED, 640*480);
		int scal = (int) (opts.outHeight / (float) 600);
		opts.inSampleSize = scal;
		b = BitmapFactory.decodeFile(outImgPath, opts);
		int degree = readPictureDegree(outImgPath);
		b = ImageUtils.rotaingImageView(degree, b);
		try {
			OutputStream out = new BufferedOutputStream(new FileOutputStream(
					outImgPath));
			Bitmap makeTextBitMap = null;
			if (degree == 90 || degree == 270) {
				makeTextBitMap = ImageUtil
						.makeTextBitMap(
								flag,
								ClaimFlag.PIC_HEIGHT,
								(int) (ClaimFlag.PIC_HEIGHT * (b.getHeight() / (float) b
										.getWidth())), title, addr,
								TimestampTool.getCurrentDateTime(), b,
								"", carNo);
			} else {
				makeTextBitMap = ImageUtil.makeTextBitMap(flag, (int) (b
						.getWidth() * (ClaimFlag.PIC_HEIGHT / (float) b
						.getHeight())), ClaimFlag.PIC_HEIGHT, title, addr,
						TimestampTool.getCurrentDateTime(), b, "", carNo);
			}

			makeTextBitMap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			makeTextBitMap.recycle();
		} catch (Exception e) {
			Log.e("a", "a", e);
		}
		// try {
		// OutputStream out = new BufferedOutputStream(new FileOutputStream(
		// outImgPath));
		// Bitmap makeTextBitMap =
		// ImageUtil.makeTextBitMap(b.getWidth(),b.getHeight(),addr,TimestampTool.getCurrentDateTime(),b,SharedData.data().getEvalUid());//ClaimFlag.PIC_WIDTH,ClaimFlag.PIC_HEIGHT+70
		// makeTextBitMap.compress(Bitmap.CompressFormat.JPEG, 85, out);
		// makeTextBitMap.recycle();
		// } catch (Exception e) {
		// Log.e("a", "a", e);
		// }
	}

	/**
	 * 压缩并保存
	 * 
	 * @param bytes
	 * @param outImgPath
	 */
	public static void compressBitmap(byte[] bytes, String title, String addr,
									  String outImgPath, int pixels, String turnFlag, String carNo) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		Bitmap b = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
		opts.inJustDecodeBounds = false;
		int scal = (int) (opts.outHeight / (float) 600);
		opts.inSampleSize = scal;

		// calculateInSampleSize(opts,640,480); //computeSampleSize(opts,
		// UNCONSTRAINED, 640*480);
		b = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
		if ("1".equals(turnFlag)) {
			b = rotaingImageView(90, b);
		}

		// float r=((float)b.getHeight()/b.getWidth());
		// if(turnFlag){
		// Matrix matrix = new Matrix();
		// matrix.postScale(scaleWidth, scaleHeight);
		// matrix.setRotate(turnRoll);
		// b=Bitmap.createBitmap(b, 0, 0,b.getWidth(),b.getHeight(), matrix,
		// true);
		// }
		// int readPictureDegree = readPictureDegree(outImgPath);
		// b = rotaingImageView(readPictureDegree,b);
		try {
			OutputStream out = new BufferedOutputStream(new FileOutputStream(
					outImgPath));
			Bitmap makeTextBitMap = null;
			if ("1".equals(turnFlag)) {
				Log.e("s----------with,height",
						b.getWidth() + "   " + b.getHeight());
				// makeTextBitMap =
				// ImageUtil.makeTextBitMap(2,ClaimFlag.PIC_HEIGHT,(int)(b.getHeight()*(ClaimFlag.PIC_HEIGHT/(float)ClaimFlag.PIC_WIDTH)),addr,TimestampTool.getCurrentDateTime(),b,SharedData.data().getEvalUid());
				makeTextBitMap = ImageUtil
						.makeTextBitMap(
								2,
								ClaimFlag.PIC_HEIGHT,
								(int) (ClaimFlag.PIC_HEIGHT * (b.getHeight() / (float) b
										.getWidth())), title, addr,
								TimestampTool.getCurrentDateTime(), b,
								"", carNo);

			} else {
				Log.e("h----------with,height",
						b.getWidth() + "   " + b.getHeight());
				// makeTextBitMap =
				// ImageUtil.makeTextBitMap(2,(int)(b.getWidth()*(ClaimFlag.PIC_HEIGHT/(float)ClaimFlag.PIC_WIDTH)),ClaimFlag.PIC_HEIGHT,addr,TimestampTool.getCurrentDateTime(),b,SharedData.data().getEvalUid());
				makeTextBitMap = ImageUtil.makeTextBitMap(2, (int) (b
						.getWidth() * (ClaimFlag.PIC_HEIGHT / (float) b
						.getHeight())), ClaimFlag.PIC_HEIGHT, title, addr,
						TimestampTool.getCurrentDateTime(), b, "", carNo);

			}

			makeTextBitMap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			makeTextBitMap.recycle();
		} catch (Exception e) {
			Log.e("a", "a", e);
		}
	}
	public static void compressBitmap1(byte[] bytes, String title, String addr,
									   String outImgPath, int pixels, String turnFlag, String carNo) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		Bitmap b = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
		opts.inJustDecodeBounds = false;
		int scal = (int) (opts.outHeight / (float) 600);
		opts.inSampleSize = scal;

		// calculateInSampleSize(opts,640,480); //computeSampleSize(opts,
		// UNCONSTRAINED, 640*480);
		b = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
		if ("1".equals(turnFlag)) {
			b = rotaingImageView(90, b);
		}

		// float r=((float)b.getHeight()/b.getWidth());
		// if(turnFlag){
		// Matrix matrix = new Matrix();
		// matrix.postScale(scaleWidth, scaleHeight);
		// matrix.setRotate(turnRoll);
		// b=Bitmap.createBitmap(b, 0, 0,b.getWidth(),b.getHeight(), matrix,
		// true);
		// }
		// int readPictureDegree = readPictureDegree(outImgPath);
		// b = rotaingImageView(readPictureDegree,b);
		try {
			OutputStream out = new BufferedOutputStream(new FileOutputStream(
					outImgPath));
			Bitmap makeTextBitMap = null;
			if ("1".equals(turnFlag)) {
				Log.e("s----------with,height",
						b.getWidth() + "   " + b.getHeight());
				// makeTextBitMap =
				// ImageUtil.makeTextBitMap(2,ClaimFlag.PIC_HEIGHT,(int)(b.getHeight()*(ClaimFlag.PIC_HEIGHT/(float)ClaimFlag.PIC_WIDTH)),addr,TimestampTool.getCurrentDateTime(),b,SharedData.data().getEvalUid());
				makeTextBitMap = ImageUtil
						.makeTextBitMap1(
								2,
								ClaimFlag.PIC_HEIGHT,
								(int) (ClaimFlag.PIC_HEIGHT * (b.getHeight() / (float) b
										.getWidth())), title, addr,
								TimestampTool.getCurrentDateTime(), b,
								"", carNo);

			} else {
				Log.e("h----------with,height",
						b.getWidth() + "   " + b.getHeight());
				// makeTextBitMap =
				// ImageUtil.makeTextBitMap(2,(int)(b.getWidth()*(ClaimFlag.PIC_HEIGHT/(float)ClaimFlag.PIC_WIDTH)),ClaimFlag.PIC_HEIGHT,addr,TimestampTool.getCurrentDateTime(),b,SharedData.data().getEvalUid());
				makeTextBitMap = ImageUtil.makeTextBitMap1(2, (int) (b
						.getWidth() * (ClaimFlag.PIC_HEIGHT / (float) b
						.getHeight())), ClaimFlag.PIC_HEIGHT, title, addr,
						TimestampTool.getCurrentDateTime(), b, "", carNo);

			}

			makeTextBitMap.compress(Bitmap.CompressFormat.JPEG, 95, out);
			makeTextBitMap.recycle();
		} catch (Exception e) {
			Log.e("a", "a", e);
		}
	}
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == UNCONSTRAINED) ? 1 : (int) Math
				.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == UNCONSTRAINED) ? 128 : (int) Math
				.min(Math.floor(w / minSideLength),
						Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == UNCONSTRAINED)
				&& (minSideLength == UNCONSTRAINED)) {
			return 1;
		} else if (minSideLength == UNCONSTRAINED) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/*
	 * 旋转图片
	 * 
	 * @param angle
	 * 
	 * @param bitmap
	 * 
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		;
		matrix.postRotate(angle);
		System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = resizedBitmap = Bitmap.createBitmap(bitmap, 0,
				0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			Log.e("path", path + "");
			ExifInterface exifInterface = new ExifInterface(path);

			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

}
