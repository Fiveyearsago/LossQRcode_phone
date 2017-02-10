package com.example.lossqrcode.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.lossqrcode.MainActivity;
import com.example.lossqrcode.MyApplication;
import com.example.lossqrcode.R;
import com.example.lossqrcode.entity.User;
import com.example.lossqrcode.service.ApiService;
import com.example.lossqrcode.ui.widget.ClearEditText;
import com.example.lossqrcode.ui.widget.LoadingDialog;
import com.jy.aicloud.util.MD5;
import com.jy.third.pjhs.dto.NLBaseJson;
import com.jy.third.pjhs.dto.NLBaseResponse;
import com.jy.third.pjhs.dto.Response;
import com.jy.third.pjhs.dto.user.User4App;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NLLoginActivity extends Activity implements OnClickListener {

	private ClearEditText editUsername;
	private ClearEditText editPassword;
	private Button buttonLogin;
	private LoadingDialog dialog;

	private String username;
	private String password;
	private User user;

	private RequestQueue requestQueue;
	private MyApplication app;
	private ProgressDialog progressBar;
	private int iCount = 0;
	private long newVerSize;
	private Context context;
	private TextView versionText;//版本号
	private static String versionName = "";// 版本名
	private static int versioncode;// 版本号

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		context = this;
		// UmengUpdateAgent.setUpdateOnlyWifi(false);
		// UmengUpdateAgent.update(this);
		app = (MyApplication) getApplication();
		initView();
		FIR.init(this);
		checkNewVersion();
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

	private void checkNewVersion() {
		// TODO Auto-generated method stub
		versionName = getVerName();

		FIR.checkForUpdateInFIR("0425fa5fac3bc809a2de5ceab3e95ffe",
				new VersionCheckCallback() {
					@Override
					public void onSuccess(String versionJson) {
						Log.i("fir", "check from fir.im success! " + "\n"
								+ versionJson);
						JSONObject jsonObject;
						String newVersionName = "";
						String versionDeString = "";
						String installUrl = "";
						try {
							jsonObject = new JSONObject(versionJson);
							newVersionName = jsonObject
									.getString("versionShort");
							installUrl = jsonObject.getString("installUrl");
							versionDeString = jsonObject.getString("changelog");
							if (!versionName.equals(newVersionName)) {
								// 版本不一致
//								Toast.makeText(getApplicationContext(),
//										"版本不一致", Toast.LENGTH_SHORT).show();
								doNewVersionUpdate(newVersionName,
										versionDeString, installUrl);
							} else {
//								Toast.makeText(getApplicationContext(), "版本一致",
//										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFail(Exception exception) {
						Log.i("fir",
								"check fir.im fail! " + "\n"
										+ exception.getMessage());
					}

					@Override
					public void onStart() {
						// Toast.makeText(getApplicationContext(), "正在获取",
						// Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFinish() {
						// Toast.makeText(getApplicationContext(), "获取完成",
						// Toast.LENGTH_SHORT).show();
					}
				});

	}

	private void doNewVersionUpdate(String newVerName,
			String versionDescription, final String installUrl) {
		String verName = getVerName();
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本:");
		sb.append(verName);
		sb.append(", \n发现新版本:");
		sb.append(newVerName);
		sb.append("\n" + versionDescription);
		sb.append("\n\n是否更新?");
		Dialog dialog = new AlertDialog.Builder(context)
				.setTitle("软件更新")
				.setMessage(sb.toString())
				// 设置内容
				.setPositiveButton("更新",// 设置确定按钮
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								progressBar = new ProgressDialog(context);
								progressBar.setTitle("正在下载");
								progressBar.setMessage("请稍候...");
								progressBar
										.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
								progressBar.setMax(100);
								downloadAPK(installUrl);
							}
						})
				.setNegativeButton("暂不更新",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
							}
						}).create();
		dialog.setCancelable(false);
		dialog.show();
	}

	/**
	 * 下载APK
	 */
	private void downloadAPK(final String installUrl) {
		progressBar.setProgress(iCount);
		progressBar.show();
		Log.i("installUrl", installUrl);
		new AsyncTask<Void, Integer, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					URL url = new URL(installUrl);
					// 打开连接
					URLConnection con = url.openConnection();
					// 获得文件的长度
					// HttpEntity entity = HttpUtil.getEntity(installUrl);
					long length = con.getContentLength();
					newVerSize = length;
					int progress = 0;

					InputStream is = con.getInputStream();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File f = new File(getSDPath() + File.separator
								+ "saomiao.apk");
						if (!f.exists()) {
							f.createNewFile();
						}
						fileOutputStream = new FileOutputStream(f);

						byte[] buf = new byte[2 * 1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							iCount = count;

							// 发布进度
							progress = Math.round(count * 1.0f / length * 100);
							publishProgress(progress);
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				return null;
			}

			@Override
			public void onProgressUpdate(Integer... valuse) {
				int progress = valuse[0];
				progressBar.setProgress(progress);
			}

			@Override
			public void onPostExecute(Void result) {
				progressBar.cancel();
				// 如果下载失败，删除临时文件
				// if (newVerSize != iCount) {
				if (newVerSize != iCount) {
					showDialog("下载新版过程中出现意外，更新失败，请重试！");
					String fileName = getSDPath() + File.separator
							+ "saomiao.apk";
					File f = new File(fileName);
					if (f.exists()) {
						f.delete();
					}
				} else {
					installAPK();
				}
			}

		}.execute();
	}

	/*
	 * 安装新版本程序
	 */
	private void installAPK() {
		String fileName = getSDPath() + File.separator + "saomiao.apk";

		PackageInfo packageInfo = null;

		try {
			packageInfo = this.getPackageManager().getPackageInfo(
					this.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			PackageInfo packageArchiveInfo = this.getPackageManager()
					.getPackageArchiveInfo(fileName,
							PackageManager.GET_CONFIGURATIONS);
			// if (packageArchiveInfo.versionCode <= packageInfo.versionCode) {
			// File file = new File(fileName);
			// file.delete();
			// } else {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(fileName)),
					"application/vnd.android.package-archive");
			startActivity(intent);
			Toast.makeText(context, "安装成功", Toast.LENGTH_LONG);
			// invokeAPK();
			// }
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 弹出提示对话框
	 * 
	 * @param msg
	 */
	public void showDialog(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				}).show();
	}

	public String getVerName() {
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			versioncode = pi.versionCode;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
			Log.i("version", versionName + "   " + versioncode);
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}

	/**
	 * 获取SD卡路径
	 * 
	 * @return
	 */
	private String getSDPath() {
		File f = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // determine whether sd
														// card is exist
		if (sdCardExist) {
			f = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "/jyds" + "/app");
			if (!f.exists()) {
				f.mkdir();
			}
		}
		return f.toString();
	}

	private void initView() {
		versionText=(TextView)findViewById(R.id.versionName);
		versionText.setText("V"+getVerName());
		editUsername = (ClearEditText) findViewById(R.id.edt_user);
		editPassword = (ClearEditText) findViewById(R.id.edt_pwd);

		editUsername
				.setOnClearTouchEvent(new ClearEditText.DeleteClickListner() {

					@Override
					public void onDeleteClick() {
						editPassword.setText("");
					}

				});

		editPassword.setText(app.getPassword());
		editUsername.setText(app.getUsername());

		buttonLogin = (Button) findViewById(R.id.btn_login);

		buttonLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		username = editUsername.getText().toString().trim();
		password = editPassword.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			Toast.makeText(this, R.string.toast_username_is_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(password)) {
			Toast.makeText(this, R.string.toast_password_is_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}

		user = new User();
		user.setUsername(username);
		user.setPassword(password);

		if (app.loginOffline(user) == true) {
			app.saveUserInfo(user);
			gotoMain();
		} else {
			login();
		}
	}

	private void login() {
		User loginDTO = new User();
		loginDTO.setUsername(username);
		loginDTO.setPassword(MD5.encrypt(password + username));

		dialog = new LoadingDialog(this, "登录中...");
		dialog.show();

		requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(ApiService.nllogin(loginDTO,
				new Listener<NLBaseJson<NLBaseResponse>>() {

					@Override
					public void onResponse(NLBaseJson<NLBaseResponse> response) {
						// TODO Auto-generated method stub
						dialog.dismiss();

						String code = response.getData().getCode();
						if ("0101".equals(code)) {
							app.saveUserInfo(user);
							gotoMain();
						} else if ("0105".equals(code)) {
							Toast.makeText(NLLoginActivity.this, "用户名或密码错误",
									Toast.LENGTH_SHORT).show();
						} else if ("0106".equals(code)) {
							Toast.makeText(NLLoginActivity.this, "用户不具有登录权限",
									Toast.LENGTH_SHORT).show();
						} else if ("0199".equals(code)) {
							Toast.makeText(NLLoginActivity.this, "网络或XML格式异常",
									Toast.LENGTH_SHORT).show();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						dialog.dismiss();
						Toast.makeText(NLLoginActivity.this, "登录失败",
								Toast.LENGTH_SHORT).show();
					}
				}));
	}

	private void gotoMain() {
		Intent intent = new Intent(NLLoginActivity.this, NLMainActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.move_right_in_activity,
				R.anim.move_left_out_activity);
	}

}
