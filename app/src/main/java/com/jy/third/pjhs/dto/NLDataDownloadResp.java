package com.jy.third.pjhs.dto;

import java.util.List;

import com.example.lossqrcode.entity.DataDownloadEntity;

public class NLDataDownloadResp extends NLBaseResponse {

	private List<DataDownloadEntity> list;

	public List<DataDownloadEntity> getList() {
		return list;
	}

	public void setList(List<DataDownloadEntity> list) {
		this.list = list;
	}
	
	
	
}
