package com.jy.third.pjhs.dto.user;

public class User4App {
	/** �û���¼ aq_yhxxb yhbm*/
	private String id;
	/**�û���¼��*/
	private String userName;
	/**���� MD5��password+userName��*/
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
