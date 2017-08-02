/**
 * NewHeight.com Inc.
 * Copyright (c) 2008-2010 All Rights Reserved.
 */
package com.tp.dto.ord;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 * 订单信息DTO
 * </pre>
 * 
 * @author szy
 * @version 0.0.1
 */
public class OrderInfoDTO implements Serializable {
	private static final long serialVersionUID = -8977132285150852190L;

	/** 收货地址ID */
	private Long consigneeId;

	/** 支付方式（1：在线支付。2：货到付款） */
	private Integer payType;

	/** 支付途径 */
	private Long payWay;

	/** 快递 */
	private Long express;

	/** 是否需要发票：0-不需要发票，1-需要发票 */
	private Integer isNeedInvoice;
	/** 发票载体：1.纸质发票，2.电子发票 */
	private Integer invoiceCarrier;
	/** 发票类型 ：1,个人 2,企业 */
	private Integer invoiceType;
	/** 发票抬头 **/
	private String invoiceTitle;
	/** 发票金额 */
	private Double invoiceAmount;

	/** 使用优惠券ID */
	private List<Long> couponIds;

	/** 订单备注 */
	private String orderRemark;
	
	/** 类型 - 普通 TYPE_COMMON = 1;类型 - 海淘 TYPE_SEA = 2*/
	private Integer cartType;
	
	/** 快速购买uuid */
	private String uuid;
	
	/** 店铺编码*/
	private Long shopPromoterId;
	
	/** 是否为首单减*/
	public transient Boolean firstMinus=Boolean.FALSE;

	/**团购参团Id*/
	private Long groupId;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getConsigneeId() {
		return consigneeId;
	}

	public void setConsigneeId(Long consigneeId) {
		this.consigneeId = consigneeId;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Long getExpress() {
		return express;
	}

	public void setExpress(Long express) {
		this.express = express;
	}

	public Integer getIsNeedInvoice() {
		return isNeedInvoice;
	}

	public void setIsNeedInvoice(Integer isNeedInvoice) {
		this.isNeedInvoice = isNeedInvoice;
	}

	public Integer getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public Double getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(Double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public List<Long> getCouponIds() {
		return couponIds;
	}

	public void setCouponIds(List<Long> couponIds) {
		this.couponIds = couponIds;
	}

	public Integer getInvoiceCarrier() {
		return invoiceCarrier;
	}

	public void setInvoiceCarrier(Integer invoiceCarrier) {
		this.invoiceCarrier = invoiceCarrier;
	}

	public Long getPayWay() {
		return payWay;
	}

	public void setPayWay(Long payWay) {
		this.payWay = payWay;
	}

	public String getOrderRemark() {
		return orderRemark;
	}

	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}

	public Integer getCartType() {
		return cartType;
	}

	public void setCartType(Integer cartType) {
		this.cartType = cartType;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getShopPromoterId() {
		return shopPromoterId;
	}

	public void setShopPromoterId(Long shopPromoterId) {
		this.shopPromoterId = shopPromoterId;
	}

}
