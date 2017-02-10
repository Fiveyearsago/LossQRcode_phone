package com.jy.third.pjhs.dto.fit;

import java.util.Date;

import com.jy.third.pjhs.dto.BaseSearchDTO;

public class RemnantGood4AppDTO<RemnantGood4App> extends
		BaseSearchDTO<RemnantGood4App> {
	private String state;


	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
