package com.tp.dto.ord.directOrderNB;

import java.io.Serializable;

public class ProductNBDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ProductNumberCode;//商品货号（系统唯一编码）
	private String ProductBarCode;//商品条码，不同货号可能同条码
	private String ProductName;//商品名
	private Integer StoreNumber;//库存数量
	
	public String getProductNumberCode() {
		return ProductNumberCode;
	}
	public void setProductNumberCode(String productNumberCode) {
		ProductNumberCode = productNumberCode;
	}
	public String getProductBarCode() {
		return ProductBarCode;
	}
	public void setProductBarCode(String productBarCode) {
		ProductBarCode = productBarCode;
	}
	public String getProductName() {
		return ProductName;
	}
	public void setProductName(String productName) {
		ProductName = productName;
	}
	public Integer getStoreNumber() {
		return StoreNumber;
	}
	public void setStoreNumber(Integer storeNumber) {
		StoreNumber = storeNumber;
	}
	
}
