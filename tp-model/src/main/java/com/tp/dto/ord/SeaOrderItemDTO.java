/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.dto.ord;

import java.util.List;

/**
 * <pre>
 * 海淘订单商品信息
 * </pre>
 * 
 * @author szy
 * @time 2015-3-20 下午5:01:46
 */
public class SeaOrderItemDTO implements BaseDTO {
	private static final long serialVersionUID = -4653991539455174802L;

	/** 会员ID */
	private Long memberId;

	/** 海淘订单商品，根据供应商拆分 */
	private List<SeaOrderItemWithSupplierDTO> seaOrderItemWithSupplierList;

	/** 商品金额总价 */
	private Double totalPrice;

	/** 商品总优惠金额 */
	private Double totalDiscount;

	/** 商品支付价格 */
	private Double payPrice;
	
	/** 购买的商品总税费 */
	private Double totalTaxfee;

	/** 购买的商品总运费 */
	private Double totalFreight;
	
	/** 整单商品总数 */
	private Integer  quantity;
	
	/** 商品吊牌总价总价 */
	private Double totalOrignalSum;
	
	/** 活动未使用优惠券应付金额 */
	private Double realSum;

	/** 是否为首单减*/
	private Boolean firstMinus=Boolean.FALSE;
	
	/** 优惠券总金额*/
	private Integer totalCoupon;
	
	public Boolean getFirstMinus() {
		return firstMinus;
	}

	public void setFirstMinus(Boolean firstMinus) {
		this.firstMinus = firstMinus;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public List<SeaOrderItemWithSupplierDTO> getSeaOrderItemWithSupplierList() {
		return seaOrderItemWithSupplierList;
	}

	public void setSeaOrderItemWithSupplierList(
			List<SeaOrderItemWithSupplierDTO> seaOrderItemWithSupplierList) {
		this.seaOrderItemWithSupplierList = seaOrderItemWithSupplierList;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public Double getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(Double payPrice) {
		this.payPrice = payPrice;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Double getTotalTaxfee() {
		return totalTaxfee;
	}

	public void setTotalTaxfee(Double totalTaxfee) {
		this.totalTaxfee = totalTaxfee;
	}

	public Double getTotalFreight() {
		return totalFreight;
	}

	public void setTotalFreight(Double totalFreight) {
		this.totalFreight = totalFreight;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getTotalOrignalSum() {
		return totalOrignalSum;
	}

	public void setTotalOrignalSum(Double totalOrignalSum) {
		this.totalOrignalSum = totalOrignalSum;
	}

	public Double getRealSum() {
		return realSum;
	}

	public void setRealSum(Double realSum) {
		this.realSum = realSum;
	}

	public Integer getTotalCoupon() {
		return totalCoupon;
	}

	public void setTotalCoupon(Integer totalCoupon) {
		this.totalCoupon = totalCoupon;
	}
}
