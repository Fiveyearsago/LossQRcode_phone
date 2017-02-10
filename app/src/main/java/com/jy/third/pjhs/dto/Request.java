package com.jy.third.pjhs.dto;

import com.example.lossqrcode.utils.GsonUtil;
import com.google.gson.reflect.TypeToken;


/**
 * 锟接口碉拷锟斤拷锟斤拷锟斤拷锟�
 * @author xinrongVy
 * 锟斤拷锟斤拷07:23:49
 * @param <T>
 */
public class Request<T> extends BaseJsonDTO<T>{
	private static final long serialVersionUID = 1L;
	private String requestType;
	private String userId;
	
	public Request<T> fromJson(String jsondata , TypeToken<Request<T>> type){
		return GsonUtil.createGson().fromJson(jsondata, type.getType());
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
}
