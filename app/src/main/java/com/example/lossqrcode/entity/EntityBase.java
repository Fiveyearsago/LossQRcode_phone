package com.example.lossqrcode.entity;

import com.lidroid.xutils.db.annotation.Id;

public abstract class EntityBase {
	@Id
    private int _id;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

   
}