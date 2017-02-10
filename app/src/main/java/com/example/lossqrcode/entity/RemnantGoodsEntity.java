package com.example.lossqrcode.entity;

import java.util.Date;
import java.util.List;

public class RemnantGoodsEntity {
	/** REMNANT_FITS_DETAIL id */
	private String id;
	/** 报案号 */
	private String bah;
	/** 车型名称 */
	private String cxmc;
	/** 零件名称 */
	private String ljmc;
	/** 零件状态 01已打印 02已扫描 03已提交 04已丢失 */
	private String state;
	/** 供货单创建日期 */
	private Date createDate;
	/** 零件扫码日期 */
	private Date scanDate;
	/** 零件打印生成的序列号 */
	private String appNo;
	/** 供货单号 */
	private String goodNo;
	/** 二维码本地存放路径 */
	private String qrcodePath;

	/** 保险公司名称 */
	private String bxgsmc;
	/** 仓库名称 */
	private String storeName;

	private String username;

	private List<RemnantGoodsEntity> searchResultList;

	public Date getScanDate() {
		return scanDate;
	}

	public void setScanDate(Date scanDate) {
		this.scanDate = scanDate;
	}

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

	public List<RemnantGoodsEntity> getSearchResultList() {
		return searchResultList;
	}

	public void setSearchResultList(List<RemnantGoodsEntity> searchResultList) {
		this.searchResultList = searchResultList;
	}

	public String getGoodNo() {
		return goodNo;
	}

	public void setGoodNo(String goodNo) {
		this.goodNo = goodNo;
	}

	public String getAppNo() {
		return appNo;
	}

	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}

	public String getQrcodePath() {
		return qrcodePath;
	}

	public void setQrcodePath(String qrcodePath) {
		this.qrcodePath = qrcodePath;
	}

	public String getBxgsmc() {
		return bxgsmc;
	}

	public void setBxgsmc(String bxgsmc) {
		this.bxgsmc = bxgsmc;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
