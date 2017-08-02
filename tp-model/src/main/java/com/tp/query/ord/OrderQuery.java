package com.tp.query.ord;

import java.io.Serializable;

public class OrderQuery implements Serializable{

	private static final long serialVersionUID = 4025085663697678410L;
	
	/** */
	private String orderSn;
	
	private String skuCode;
	
	private String goodsCode;

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	
	
	
	
	

}
