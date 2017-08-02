package com.tp.dto.pay.query;

import java.io.Serializable;

public class TradeStatusQuery implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5663834225095089364L;
	private String bizCode;

	public String getBizCode() {
		return bizCode;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}
}
