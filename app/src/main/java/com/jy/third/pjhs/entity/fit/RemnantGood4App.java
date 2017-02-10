package com.jy.third.pjhs.entity.fit;

import java.util.Date;

/** 存储移动app端展示数据的载体 */
public class RemnantGood4App {
	/** REMNANT_FITS_DETAIL id */
	private String id;
	/** 报案号 */
	private String bah;
	/** 车型名称 */
	private String cxmc;
	/** 零件名称 */
	private String ljmc;
	/** 零件状态 00 已纳入供货单 01已打印 02已扫描 03已提交 */
	private String state;
	/** 供货单创建日期 */
	private Date createDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBah() {
		return bah;
	}

	public void setBah(String bah) {
		this.bah = bah;
	}

	public String getCxmc() {
		return cxmc;
	}

	public void setCxmc(String cxmc) {
		this.cxmc = cxmc;
	}

	public String getLjmc() {
		return ljmc;
	}

	public void setLjmc(String ljmc) {
		this.ljmc = ljmc;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
