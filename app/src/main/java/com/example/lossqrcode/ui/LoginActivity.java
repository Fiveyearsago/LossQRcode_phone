package com.example.lossqrcode.ui;

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
import com.example.lossqrcode.ui.widget.LoadingDialog;
import com.jy.aicloud.util.MD5;
import com.jy.third.pjhs.dto.Response;
import com.jy.third.pjhs.dto.user.User4App;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{
	private EditText editUsername;
	private EditText editPassword;
	private Button buttonLogin;
	private LoadingDialog dialog;
	
	private String username;
	private String password;
	private User user;
	
	private RequestQueue requestQueue;
	private MyApplication app;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loginold);
		app=(MyApplication)getApplication();
		initView();
	}
	
	private void initView(){
		editUsername=(EditText)findViewById(R.id.edit_username);
		editPassword=(EditText)findViewById(R.id.edit_password);
		
		editPassword.setText(app.getPassword());
		editUsername.setText(app.getUsername());
		
		buttonLogin=(Button)findViewById(R.id.button_login);
		
		buttonLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		username=editUsername.getText().toString().trim();
		password=editPassword.getText().toString().trim();
		if(TextUtils.isEmpty(username)){
			Toast.makeText(this, R.string.toast_username_is_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(password)){
			Toast.makeText(this, R.string.toast_password_is_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		
		user=new User();
		user.setUsername(username);
		user.setPassword(password);
		
		if(app.loginOffline(user)==true){
			app.saveUserInfo(user);
			gotoMain();
		}else{
			login();
		}
	}
	
//	private void saveUserInfo(){
//		getSharedPreferences("sp", MODE_PRIVATE).edit().putString("username", username).putString("password", password).commit();
//	}
//	
//	private String getUsername(){
//		return getSharedPreferences("sp", MODE_PRIVATE).getString("username", "");
//	}
//	
//	private String getPassword(){
//		return getSharedPreferences("sp", MODE_PRIVATE).getString("password", "");
//	}
	
	private void login(){
		User4App loginDTO=new User4App();
		loginDTO.setUserName(username);
		loginDTO.setPassword(MD5.encrypt(password+username));
		
		dialog=new LoadingDialog(this,"µÇÂ¼ÖÐ...");
		dialog.show();
		
		requestQueue=Volley.newRequestQueue(this);
		requestQueue.add(ApiService.login(loginDTO, new Listener<Response<User4App>>() {

			@Override
			public void onResponse(Response<User4App> response) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if("0".equals(response.getResponseType())){
					app.saveUserInfo(user);
					String userId=response.getData().getId();
					((MyApplication)getApplicationContext()).cacheUserId(userId);
					gotoMain();
				}else{
					Toast.makeText(LoginActivity.this, response.getErrMsg(), Toast.LENGTH_SHORT).show();
				}
				
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				Toast.makeText(LoginActivity.this, "µÇÂ¼Ê§°Ü", Toast.LENGTH_SHORT).show();
			}
		}));
	}
	
	private void gotoMain(){
		Intent intent=new Intent(LoginActivity.this,MainActivity.class);
		startActivity(intent);
	}

}
