package com.example.lossqrcode.entity;

import java.util.ArrayList;
import java.util.Date;

public class DataDownloadEntity {

	/**
	 * ���id
	 */
	private String id;
	/**
	 * ������
	 */
	private String bah;
	/**
	 * ��������
	 */
	private String cxmc;
	/**
	 * �������
	 */
	private String ljmc;
	/**
	 * ������������
	 */
	private String sendDate;
	/**
	 * ��������
	 */
	private String barCode;

	/******** ���������� *********/

	/**
	 * ע���
	 */
	private String barzch;
	/**
	 * ������
	 */
	private String barbah;
	/**
	 * ��������
	 */
	private String barghdh;
	/**
	 * ���ƺ�
	 */
	private String barcph;
	
	//��������
	
	private String barysth;
	
	private String username;
	
	private String state;
	
	private Date createDate;
	
	
	private String careState;//��ע
	
	private String url;
	private String repairAddress;
	private String partId;//���Id
	private String repairPhone;//��ϵ�绰
	private String vehSeriName;//��������

	public String getVehSeriName() {
		return vehSeriName;
	}

	public void setVehSeriName(String vehSeriName) {
		this.vehSeriName = vehSeriName;
	}

	public String getRepairPhone() {
		return repairPhone;
	}

	public void setRepairPhone(String repairPhone) {
		this.repairPhone = repairPhone;
	}

	public String getPartId() {
		return partId;
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}

	public String getRepairAddress() {
		return repairAddress;
	}

	public void setRepairAddress(String repairAddress) {
		this.repairAddress = repairAddress;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCareState() {
		return careState;
	}

	public void setCareState(String careState) {
		this.careState = careState;
	}

	/** ���ɨ������ */
	private Date scanDate;
	
	
	public String getBarysth() {
		return barysth;
	}

	public void setBarysth(String barysth) {
		this.barysth = barysth;
	}

	public Date getScanDate() {
		return scanDate;
	}

	public void setScanDate(Date scanDate) {
		this.scanDate = scanDate;
	}

	public String getUsername() {
		return username;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getBarzch() {
		return barzch;
	}

	public void setBarzch(String barzch) {
		this.barzch = barzch;
	}

	public String getBarbah() {
		return barbah;
	}

	public void setBarbah(String barbah) {
		this.barbah = barbah;
	}

	public String getBarghdh() {
		return barghdh;
	}

	public void setBarghdh(String barghdh) {
		this.barghdh = barghdh;
	}

	public String getBarcph() {
		return barcph;
	}

	public void setBarcph(String barcph) {
		this.barcph = barcph;
	}
	

}
