package com.tp.dto.ord.directOrderNB;

import java.io.Serializable;

public class DirectOrderDetailNBDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String ProductNumberCode;	//货号
	private String SaleGoodsName;		//商品名
	private String SaleGoodsPrice;		//单价
	private String SaleNumber;			//数量
	private String SaleSubTotal;		//金额
	private String SkuCode;				//经销商商品编码
	private String HsCode;				//商品HSCode
	
	public String getSkuCode() {
		return SkuCode;
	}
	public void setSkuCode(String skuCode) {
		SkuCode = skuCode;
	}
	public String getHsCode() {
		return HsCode;
	}
	public void setHsCode(String hsCode) {
		HsCode = hsCode;
	}
	public String getProductNumberCode() {
		return ProductNumberCode;
	}
	public void setProductNumberCode(String productNumberCode) {
		ProductNumberCode = productNumberCode;
	}
	public String getSaleGoodsName() {
		return SaleGoodsName;
	}
	public void setSaleGoodsName(String saleGoodsName) {
		SaleGoodsName = saleGoodsName;
	}
	public String getSaleGoodsPrice() {
		return SaleGoodsPrice;
	}
	public void setSaleGoodsPrice(String saleGoodsPrice) {
		SaleGoodsPrice = saleGoodsPrice;
	}
	public String getSaleNumber() {
		return SaleNumber;
	}
	public void setSaleNumber(String saleNumber) {
		SaleNumber = saleNumber;
	}
	public String getSaleSubTotal() {
		return SaleSubTotal;
	}
	public void setSaleSubTotal(String saleSubTotal) {
		SaleSubTotal = saleSubTotal;
	}
	
	
	
}
