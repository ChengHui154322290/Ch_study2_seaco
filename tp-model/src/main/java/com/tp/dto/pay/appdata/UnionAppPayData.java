package com.tp.dto.pay.appdata;

import java.io.Serializable;

import com.tp.dto.pay.AppPayData;


public class UnionAppPayData implements AppPayData, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8451071972198266727L;
	private String tn;
	private String bizCode;

	public String getTn() {
		return tn;
	}

	public void setTn(String tn) {
		this.tn = tn;
	}

	public String getBizCode() {
		return bizCode;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	@Override
	public String getPaymentTradeNo() {
		return bizCode;
	}

	@Override
	public String toString() {
		return "UnionAppPayData [tn=" + tn + ", bizCode=" + bizCode + "]";
	}
}
