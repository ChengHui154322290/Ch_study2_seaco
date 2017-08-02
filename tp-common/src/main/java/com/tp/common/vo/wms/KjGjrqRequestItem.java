package com.tp.common.vo.wms;

import java.util.Date;
import javax.validation.constraints.NotNull;


public class KjGjrqRequestItem implements java.io.Serializable {

	/** 商品编号 */
//	@NotEmpty
	private String skuKey;

	/** 货物名称 */
//	@NotEmpty
	private String sku;

	/** 数量 */
	@NotNull
	private Double qty;

	/** 报检单号 */
//	@NotEmpty
	private String declNo;

	/** 报关单号 */
//	@NotEmpty
	private String entryNo;

	/** 数量 */
	@NotNull
	private Double weight;

	/** 单价 */
	@NotNull
	private Double unitPrice;

	/** 货值 */
	@NotNull
	private Double currencyValue;

	/** 币制（固定值：RMB） */
//	@NotEmpty
	private String currency;

	/** 电子账册号 */
//	@NotEmpty
	private String extraNo1;

	/** 项号 */
	@NotNull
	private Double extraNo2;

	/** 安全保质截止日(yyyy-MM-dd) */
//	@NotEmpty
	private String expirationTime;
	
	/**
	* 设置 商品编号
	* @param skuKey
	*/
		public void setSkuKey(String skuKey) {
		this.skuKey = skuKey;
	}
	/**
	* 设置 货物名称
	* @param sku
	*/
		public void setSku(String sku) {
		this.sku = sku;
	}
	/**
	* 设置 数量
	* @param qty
	*/
		public void setQty(Double qty) {
		this.qty = qty;
	}
	/**
	* 设置 报检单号
	* @param declNo
	*/
		public void setDeclNo(String declNo) {
		this.declNo = declNo;
	}
	/**
	* 设置 报关单号
	* @param entryNo
	*/
		public void setEntryNo(String entryNo) {
		this.entryNo = entryNo;
	}
	/**
	* 设置 数量
	* @param weight
	*/
		public void setWeight(Double weight) {
		this.weight = weight;
	}
	/**
	* 设置 单价
	* @param unitPrice
	*/
		public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	/**
	* 设置 货值
	* @param currencyValue
	*/
		public void setCurrencyValue(Double currencyValue) {
		this.currencyValue = currencyValue;
	}
	/**
	* 设置 币制（固定值：RMB）
	* @param currency
	*/
		public void setCurrency(String currency) {
		this.currency = currency;
	}
	/**
	* 设置 电子账册号
	* @param extraNo1
	*/
		public void setExtraNo1(String extraNo1) {
		this.extraNo1 = extraNo1;
	}
	/**
	* 设置 项号
	* @param extraNo2
	*/
		public void setExtraNo2(Double extraNo2) {
		this.extraNo2 = extraNo2;
	}
	/**
	* 设置 安全保质截止日(yyyy-MM-dd)
	* @param expirationTime
	*/
		public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}
	/**
	* 获取 商品编号
	* @return skuKey
	*/
		public String getSkuKey() {
		return skuKey;
	}
	/**
	* 获取 货物名称
	* @return sku
	*/
		public String getSku() {
		return sku;
	}
	/**
	* 获取 数量
	* @return qty
	*/
		public Double getQty() {
		return qty;
	}
	/**
	* 获取 报检单号
	* @return declNo
	*/
		public String getDeclNo() {
		return declNo;
	}
	/**
	* 获取 报关单号
	* @return entryNo
	*/
		public String getEntryNo() {
		return entryNo;
	}
	/**
	* 获取 数量
	* @return weight
	*/
		public Double getWeight() {
		return weight;
	}
	/**
	* 获取 单价
	* @return unitPrice
	*/
		public Double getUnitPrice() {
		return unitPrice;
	}
	/**
	* 获取 货值
	* @return currencyValue
	*/
		public Double getCurrencyValue() {
		return currencyValue;
	}
	/**
	* 获取 币制（固定值：RMB）
	* @return currency
	*/
		public String getCurrency() {
		return currency;
	}
	/**
	* 获取 电子账册号
	* @return extraNo1
	*/
		public String getExtraNo1() {
		return extraNo1;
	}
	/**
	* 获取 项号
	* @return extraNo2
	*/
		public Double getExtraNo2() {
		return extraNo2;
	}
	/**
	* 获取 安全保质截止日(yyyy-MM-dd)
	* @return expirationTime
	*/
		public String getExpirationTime() {
		return expirationTime;
	}

}
