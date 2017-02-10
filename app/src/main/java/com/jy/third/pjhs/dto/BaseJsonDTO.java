package com.jy.third.pjhs.dto;

import java.io.Serializable;

import com.example.lossqrcode.utils.GsonUtil;
import com.google.gson.Gson;

public class BaseJsonDTO<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	private T data;
	private String errMsg;
	
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public String getJson() {
		Gson gson = GsonUtil.createGson();
		return gson.toJson(this);
	}
	public String getContentType() {
		return "text/html;charset=GBK";
	}
}
