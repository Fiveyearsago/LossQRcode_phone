package com.jy.third.pjhs.dto;

import java.io.Serializable;

import com.example.lossqrcode.utils.GsonUtil;
import com.google.gson.reflect.TypeToken;


public class NLBaseJson<T> implements Serializable{
	private static final long serialVersionUID = 1L;
	private String flag;
	private T data;
	
	public NLBaseJson<T> fromJson(String jsondata , TypeToken<NLBaseJson<T>> type){
		return GsonUtil.createGson().fromJson(jsondata, type.getType());
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	
	
}
