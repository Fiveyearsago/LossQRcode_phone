package com.jy.third.pjhs.entity.fit;

import java.util.Date;

/** �洢�ƶ�app��չʾ���ݵ����� */
public class RemnantGood4App {
	/** REMNANT_FITS_DETAIL id */
	private String id;
	/** ������ */
	private String bah;
	/** �������� */
	private String cxmc;
	/** ������� */
	private String ljmc;
	/** ���״̬ 00 �����빩���� 01�Ѵ�ӡ 02��ɨ�� 03���ύ */
	private String state;
	/** �������������� */
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
