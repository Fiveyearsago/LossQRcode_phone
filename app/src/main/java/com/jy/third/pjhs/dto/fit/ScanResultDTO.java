package com.jy.third.pjhs.dto.fit;

import java.util.Date;

public class ScanResultDTO {
	private String[] ids;
	private Date scanDate;
	
	
	public String[] getIds() {
		return ids;
	}
	public void setIds(String[] ids) {
		this.ids = ids;
	}
	public Date getScanDate() {
		return scanDate;
	}
	public void setScanDate(Date scanDate) {
		this.scanDate = scanDate;
	}
	
	
}
