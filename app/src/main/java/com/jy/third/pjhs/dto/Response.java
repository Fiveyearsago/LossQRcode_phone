package com.jy.third.pjhs.dto;

import com.example.lossqrcode.utils.GsonUtil;
import com.google.gson.reflect.TypeToken;

public class Response<T> extends BaseJsonDTO<T> {
	private static final long serialVersionUID = 1L;
	private String responseType;
	
	public Response<T> fromJson(String jsondata , TypeToken<Response<T>> type){
		return GsonUtil.createGson().fromJson(jsondata, type.getType());
	}
	
	public String getResponseType() {
		return responseType;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
}
