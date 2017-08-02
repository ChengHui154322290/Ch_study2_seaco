package com.tp.dto.ord;

import java.util.Date;

import com.tp.model.ord.OrderRedeemItem;

/**
  * @author zhouguofeng
  * 商家线下购买兑换码信息
  */
public class OrderRedeemItemDTO extends OrderRedeemItem {

	private static final long serialVersionUID = 2690196440134328125L;
	
	/**店铺名称 数据类型varchar(255)*/
	private String shopName;
	/**兑换码金额 数据类型double(10,2)*/
	private Double discount;
	/**兑换码失效日期 数据类型datetime*/
	private Date expirationDate;
	/**团购券名称*/
	private String skuName;
	/**退款金额*/
	private Double refunedAmount;
	/**实际收入*/
	private Double amount=discount;
	/**更新人*/
	private String updateUser;
	
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public Double getRefunedAmount() {
		return refunedAmount;
	}
	public void setRefunedAmount(Double refunedAmount) {
		this.refunedAmount = refunedAmount;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
		
}