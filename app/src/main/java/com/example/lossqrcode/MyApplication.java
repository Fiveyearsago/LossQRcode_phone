package com.example.lossqrcode;

import com.example.lossqrcode.entity.User;
import com.example.lossqrcode.utils.Constants;
import com.google.gson.Gson;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.widget.Toast;

public class MyApplication extends Application{
	
	private SharedPreferences sp;
    private Editor spEditor;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		sp = getSharedPreferences(Constants.SP, Context.MODE_PRIVATE);
	     spEditor = sp.edit();
		
	}
	
	public SharedPreferences getSp() {
        return sp;
    }

    public Editor getSpEditor() {
        return spEditor;
    }	
	
	/**
	 * 保存userId
	 * @param userId
	 */
	public void cacheUserId(String userId){
		getSharedPreferences(Constants.SP, MODE_PRIVATE).edit().putString(Constants.USER_ID, userId).commit();
	}
	
	/**
	 * 获取userId
	 * @return
	 */
	public String getUserId(){
		return getSharedPreferences(Constants.SP, MODE_PRIVATE).getString(Constants.USER_ID, "");
	}
	
	
	/**
	 * 线下登录
	 * @param user
	 * @return
	 */
	public boolean loginOffline(User user){
		boolean flag;
		String lastLoginUser=getUsername();
		if(TextUtils.isEmpty(lastLoginUser)){
			flag=false;
		}else{
			Gson gson = new Gson();
			String userJson=getSharedPreferences(Constants.SP, MODE_PRIVATE).getString(user.getUsername(), "");
			if(TextUtils.isEmpty(userJson)){
				flag=false;
			}else{
				User saveUser=gson.fromJson(userJson, User.class);
				if(user.getPassword().equals(saveUser.getPassword())){
					flag=true;
				}else{
					flag=false;
					Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
				}
			}
			
		}
		return flag;
	}
	
	public void saveUserInfo(User user){
		getSharedPreferences("sp", MODE_PRIVATE).edit()
		.putString("username", user.getUsername())
		.putString("password", user.getPassword()).commit();
		
		Gson gson = new Gson();
		String userJson=gson.toJson(user);
		getSharedPreferences("sp", MODE_PRIVATE).edit()
		.putString(user.getUsername(), userJson).commit();
	}
	
	
	public void saveUserInfo(){
		
	}
	
	public String getUsername(){
		return getSharedPreferences("sp", MODE_PRIVATE).getString("username", "");
	}
	
	public String getPassword(){
		return getSharedPreferences("sp", MODE_PRIVATE).getString("password", "");
	}

}
