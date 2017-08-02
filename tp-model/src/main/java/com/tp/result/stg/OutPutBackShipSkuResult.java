package com.tp.result.stg;

import java.io.Serializable;

public class OutPutBackShipSkuResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 816473448171466190L;
	/**
     * sku code码
     */
	private String skuCode;
	/**
	 * sku的数量
	 */
	private String skuNum;

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getSkuNum() {
		return skuNum;
	}

	public void setSkuNum(String skuNum) {
		this.skuNum = skuNum;
	}
}
