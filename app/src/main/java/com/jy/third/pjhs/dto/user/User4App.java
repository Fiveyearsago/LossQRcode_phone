package com.jy.third.pjhs.dto.user;

public class User4App {
	/** 用户登录 aq_yhxxb yhbm*/
	private String id;
	/**用户登录名*/
	private String userName;
	/**密码 MD5（password+userName）*/
	private String password;
	public User4App(String userName, String password) {
		this.userName=userName;
		this.password=password;
	}
	public User4App() {
		super();
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
