package com.tp.result.stg;

import java.io.Serializable;

public class ASNsDetailResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5119068412559787595L;
	/**
	 * sku
	 */
	private String SkuCode;
	/**
	 * 入库时间
	 */
	private String ReceivedTime;
	/**
	 * 入库预计数量
	 */
	private String ExpectedQty;
	/**
	 * 入库数量
	 */
	private String ReceivedQty;

	public String getSkuCode() {
		return SkuCode;
	}

	public void setSkuCode(String skuCode) {
		SkuCode = skuCode;
	}

	public String getReceivedTime() {
		return ReceivedTime;
	}

	public void setReceivedTime(String receivedTime) {
		ReceivedTime = receivedTime;
	}

	public String getExpectedQty() {
		return ExpectedQty;
	}

	public void setExpectedQty(String expectedQty) {
		ExpectedQty = expectedQty;
	}

	public String getReceivedQty() {
		return ReceivedQty;
	}

	public void setReceivedQty(String receivedQty) {
		ReceivedQty = receivedQty;
	}
}
