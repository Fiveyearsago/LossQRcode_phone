package com.jy.third.pjhs.dto;

import java.util.List;

public class UploadScanDataReq {

	private String username;
	
	private List<String> ids;
	private List<String> goodNos;
	

	public List<String> getGoodNos() {
		return goodNos;
	}

	public void setGoodNos(List<String> goodNos) {
		this.goodNos = goodNos;
	}

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
	
}
