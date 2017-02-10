package com.example.lossqrcode.entity;

import java.util.Date;
import java.util.List;

public class RemnantGoodsEntity {
	/** REMNANT_FITS_DETAIL id */
	private String id;
	/** ������ */
	private String bah;
	/** �������� */
	private String cxmc;
	/** ������� */
	private String ljmc;
	/** ���״̬ 01�Ѵ�ӡ 02��ɨ�� 03���ύ 04�Ѷ�ʧ */
	private String state;
	/** �������������� */
	private Date createDate;
	/** ���ɨ������ */
	private Date scanDate;
	/** �����ӡ���ɵ����к� */
	private String appNo;
	/** �������� */
	private String goodNo;
	/** ��ά�뱾�ش��·�� */
	private String qrcodePath;

	/** ���չ�˾���� */
	private String bxgsmc;
	/** �ֿ����� */
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
