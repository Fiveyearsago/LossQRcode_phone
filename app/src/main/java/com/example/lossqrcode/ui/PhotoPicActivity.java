package com.example.lossqrcode.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lossqrcode.R;
import com.example.lossqrcode.utils.GetLocation;
import com.example.lossqrcode.utils.ImageUtils;
import com.example.lossqrcode.utils.TimestampTool;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * �Զ��屣����Ƭ
 * 
 * @author Administrator ���ݰ�������������� �����ı�ҳ��,����-->ȷ��,����,����,ȡ��,���� ȷ��������Ƭ������
 *         ���ĸ���ԭ��������Ƭ ���ı�����Ƭ���ٴ����� ,ȷ������ ȡ����������Ƭ���� ������Ƭ��С������
 */
public class PhotoPicActivity extends BaseActivity implements
		SensorEventListener {
	private Context context;
	private String falw1 = "����";

	private ImageView phReGo;
	private ImageView phGoOn;

	private TextView phLocal;
	// ����������������
	private SurfaceView surfaceView;
	// ��������������
	private SurfaceHolder surfaceHolder;
	// ���������
	private Camera camera;

	private ProgressDialog dialog;

	private SensorManager mSensorManager;
	private static String PATH;
	String turnFlag = "0";// ����
	private ImageView phReGoS;
	private ImageView phGoOnS;
	private String address;
	
	private ImageView imageViewFlash,imageViewFlash1;
	private String flashString="auto";
	private boolean isTakingPicture=false;
	private String id,ljmc,barghdh,barcph,partId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// ȫ��

		setContentView(R.layout.ds_photolay);
		GetLocation.getLoc(PhotoPicActivity.this, new GetLocation.LocationCallBack() {
			@Override
			public void locationResponse(String addr, String province, String city) {
				address=addr;
				Log.i("address",address);
			}

		});

		init();
		checkStage();// ����豸
		findView();
		bindView();
	}


	@SuppressLint("InlinedApi")
	private void init() {
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mOrientationSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ORIENTATION);

		context = this;
		PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator + "/jyds/img/LossQRcode";
		id = getIntent().getExtras().getString("id");
		ljmc = getIntent().getExtras().getString("ljmc");
		barghdh = getIntent().getExtras().getString("barghdh");
		barcph = getIntent().getExtras().getString("barcph");
		partId = getIntent().getExtras().getString("partId");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(surfaceCallback);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	private void findView() {
		phLocal = (TextView) findViewById(R.id.ds_phLocal);
		phLocal.setText(address == null ? "���ڻ�ȡ��ǰλ��..." : address);

		phReGo = (ImageView) findViewById(R.id.ds_phReGo);// ��������
		phReGo.setOnClickListener(this.onClickListener);

		phGoOn = (ImageView) findViewById(R.id.ds_phGoOn);// ��������
		phGoOn.setOnClickListener(this.onClickListener);

		phReGoS = (ImageView) findViewById(R.id.ds_phReGo_s);// ��������
		phReGoS.setOnClickListener(this.onClickListener);

		phGoOnS = (ImageView) findViewById(R.id.ds_phGoOn_s);// ��������
		phGoOnS.setOnClickListener(this.onClickListener);
		
		imageViewFlash=(ImageView) findViewById(R.id.flash_image);
		imageViewFlash1=(ImageView) findViewById(R.id.flash_image1);
		imageViewFlash.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		// ʵ�������ս������
		surfaceView = (SurfaceView) findViewById(R.id.ds_mSurView);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			findViewById(R.id.contral_h).setVisibility(View.VISIBLE);
			findViewById(R.id.contral_s).setVisibility(View.GONE);
//			imageViewFlash.setVisibility(View.VISIBLE);
			turnFlag = "0";
		} else {
			findViewById(R.id.contral_h).setVisibility(View.GONE);
			findViewById(R.id.contral_s).setVisibility(View.VISIBLE);
//			imageViewFlash.setVisibility(View.VISIBLE);
			turnFlag = "1";
		}

	}

	private void bindView() {

	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			// case R.id.ds_phOK:
			// case R.id.ds_phOK_s:
			// photoOK();
			// break;
			case R.id.ds_phReGo:
			case R.id.ds_phReGo_s:
				photoReGO();
				break;
			case R.id.ds_phGoOn:
			case R.id.ds_phGoOn_s:
				photoGoOn();
				break;
			// case R.id.ds_phCancle:
			// case R.id.ds_phCancle_s:
			// photoCancel();
			// break;
			default:
			}
		}

	};

	private void photoOK() {
		
		if ("����".equals(falw1)) {
			takePic();
			falw1 = "ȷ��";
			// phOK.setImageResource(R.drawable.ds_phsure_before);
		} else if ("ȷ��".equals(falw1)) {
			// showDialog(0);
			// finish();
		} else if ("����".equals(falw1)) {
			// showDialog(0);
			// finish();
		}
	}

	private void photoReGO() {
		// ���ջ�Ԥ��
		camera.startPreview();
		// phOK.setImageResource(R.drawable.ds_phok_before);
	}

	
	private void photoGoOn() {
		// phGoOn.setEnabled(false);
		// phReGo.setEnabled(false);
		// // phOK.setEnabled(false);
		// phGoOnS.setEnabled(false);
		// phReGoS.setEnabled(false);
		// phOKS.setEnabled(false);
		falw1 = "����";
		// ���ջ�Ԥ��
		takePic();
		// phOK.setImageResource(R.drawable.ds_phback_before);
		// phOKS.setImageResource(R.drawable.ds_phback_before);
	}

	private void photoCancel() {
		// ����
		finish();
	}

	SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			try {
				camera = Camera.open();
				if (camera != null) {
					setCameraDisplayOrientation(PhotoPicActivity.this, 0,
							camera);
					Camera.Parameters parameters = camera.getParameters();
					parameters.setJpegQuality(99);
					parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
					// ������Ƭ��ʽ
					parameters.setPictureFormat(ImageFormat.JPEG);
					// �����������
					camera.setParameters(parameters);
				}
				camera.setPreviewDisplay(surfaceHolder);
				camera.startPreview();
			} catch (IOException e) {
				camera.stopPreview();
				camera.release();
				camera = null;
				Toast.makeText(PhotoPicActivity.this,"������ͷʧ�ܣ�", Toast.LENGTH_SHORT).show();
				finish();
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
								   int height) {
			try {
				// ����������
				if (camera != null) {
					Camera.Parameters parameters = camera.getParameters();
					parameters.setJpegQuality(99);
					parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
					// ������Ƭ��ʽ
					parameters.setPictureFormat(PixelFormat.JPEG);
					// �����������
					camera.setParameters(parameters);
					// ��ʼ���
					camera.startPreview();
				}
			} catch (Exception e) {
				eshowDialog("������ϣ��޷����ӵ����");
				Log.e("PhotoPicActivity", "findView:", e);
				camera.release();
				camera = null;
				finish();
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			if (camera != null) {
				camera.setPreviewCallback(null);
				camera.stopPreview();
				camera.release();
				camera = null;
			}
		}
	};

	public static int getCameraDisplayOrientation(Activity activity,
			int cameraId, Camera camera) {
		Camera.CameraInfo info = new Camera.CameraInfo();
		Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}
		return degrees;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// ����
			findViewById(R.id.contral_h).setVisibility(View.VISIBLE);
			findViewById(R.id.contral_s).setVisibility(View.GONE);
			Log.e("-----------------h", "-----------------h");
			turnFlag = "0";// ����
			// camera.setDisplayOrientation(0);

		} else {
			turnFlag = "1";// ����
			// ����
			findViewById(R.id.contral_h).setVisibility(View.GONE);
			findViewById(R.id.contral_s).setVisibility(View.VISIBLE);
			Log.e("-----------------s", "-----------------s");
			// camera.setDisplayOrientation(90);
		}
		setCameraDisplayOrientation(PhotoPicActivity.this, 0, camera);
	}

	public static void setCameraDisplayOrientation(Activity activity,
			int cameraId, Camera camera) {
		Camera.CameraInfo info = new Camera.CameraInfo();
		Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}
		Log.e("---------------1", degrees + "");
		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { //
			result = (info.orientation - degrees + 360) % 360;
		}
		camera.setDisplayOrientation(result);
		// camera.stopPreview();
		// camera.setDisplayOrientation(result);
		// camera.startPreview();
		// return degrees;
	}

	protected static void setDisplayOrientation(Camera camera, int angle) {
		Method downPolymorphic;
		try {
			downPolymorphic = camera.getClass().getMethod(
					"setDisplayOrientation", new Class[] { int.class });
			if (downPolymorphic != null)
				downPolymorphic.invoke(camera, new Object[] { angle });
		} catch (Exception e1) {
		}
	}

	// ��Ƭ�ص� ֻ��ʵ������ص����������н��롢���漴�ɣ�ǰ2����������ֱ����Ϊnull������ϵͳһ����Զ��������Щ��д������
	Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

		@Override
		public synchronized void onPictureTaken(byte[] data, Camera camera) {

			String picName = TimestampTool.getStrDateTime().concat("_")
					.concat(partId).concat("_")
					.concat(barghdh).concat(".jpg");
			String picPath = PATH.concat("/").concat(id).concat("/").concat(picName);
			int imgType = ImageUtils.MAX_PIXELS;// takePhotoTypeCb.isChecked() ?
												// ImageUtils.MAX_PIXELS:
												// ImageUtils.MIN_PIXELS;
			ImageUtils.compressBitmap(data, ljmc,address , picPath,
					imgType, turnFlag,barcph);

			// �������ݿ�
//			QuestionAnswerDetial details = new QuestionAnswerDetial();
//			details.setEval_id(eval_id);
//			details.setQuestion_id(question_id);
//			details.setPic_name(picName);
//			details.setPic_path(picPath);
//			action.insertQuestionAnswerDetial(details);

			camera.startPreview();
			// ������ɣ��ظ����չ��ܿ���
			phGoOn.setEnabled(true);
			// phOK.setEnabled(true);
			phReGo.setEnabled(true);

			phGoOnS.setEnabled(true);
			// phOKS.setEnabled(true);
			phReGoS.setEnabled(true);
			isTakingPicture = false;
		}
	};

	// ���շ���
	private void takePic() {
		if (!isTakingPicture) {
			isTakingPicture = true;
			camera.autoFocus(new AutoFocus());
			

		}
	}

	private final class AutoFocus implements AutoFocusCallback {
		@Override
		public void onAutoFocus(boolean success, Camera arg1) {
			if (camera != null) {
				dialog = ProgressDialog.show(context, null, "�����У����Ժ�...", true);
				camera.takePicture(new ShutterCallback() {

					@Override
					public void onShutter() {

					}

				}, null, pictureCallback);
				handler.sendEmptyMessage(1);
			} else {
				isTakingPicture = false;
			}
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message message) {
			switch (message.what) {
			case 1:
				dialog.dismiss(); // �رս�����
				break;
			}
		}
	};
	private Sensor mOrientationSensor;

	// private int configurationDegrees;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_CAMERA
				|| keyCode == KeyEvent.KEYCODE_SEARCH) {
			// ���ջ�Ԥ��
			phGoOn.setEnabled(false);
			phReGo.setEnabled(false);
			// phOK.setEnabled(false);

			phGoOnS.setEnabled(false);
			phReGoS.setEnabled(false);
			// phOKS.setEnabled(false);
			// ���ջ�Ԥ��
			takePic();
			// phOK.setBackgroundResource(R.drawable.selector_button_bg);
			// phOKS.setBackgroundResource(R.drawable.selector_button_bg);
			phGoOn.setEnabled(true);
			phGoOnS.setEnabled(true);
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) { //
			Intent intent=new Intent(PhotoPicActivity.this,PhotoActivity.class);
//			intent.putExtra("partId", partId);
			this.setResult(RESULT_OK,intent);
			finish();
			
		}
		return true;
	}

	/**
	 * ���һ���ֻ��豸����Ӳ���Ŀ���״̬ �ж��ֻ�SD���Ƿ����
	 */
	public void checkStage() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ File.separator
					+ "/jyds/img/LossQRcode";
			File dir = new File(path + "/" + id);
			if (!dir.isDirectory()) {
				dir.mkdirs();
			}
		} else {
			new AlertDialog.Builder(this)
					.setMessage("��鵽û�д洢��,������ֻ��洢���ٿ�����Ӧ��")
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									finish();
								}
							}).show();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("�����У����Ժ�...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		}
		return null;
	}

	private void eshowDialog(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("�������").setMessage(msg).setCancelable(false)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						finish();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
}
