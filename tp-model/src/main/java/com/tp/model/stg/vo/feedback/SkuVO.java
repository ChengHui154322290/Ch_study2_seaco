package com.tp.model.stg.vo.feedback;

import java.io.Serializable;

public class SkuVO implements Serializable{

	private static final long serialVersionUID = 5611246905158712521L;
	/** 商品sku */
	private String skuCode;
	/** 条形码 */
	private String barcode;
	
	private int skuNum;
	
	private String vendor;

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public int getSkuNum() {
		return skuNum;
	}

	public void setSkuNum(int skuNum) {
		this.skuNum = skuNum;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
}
