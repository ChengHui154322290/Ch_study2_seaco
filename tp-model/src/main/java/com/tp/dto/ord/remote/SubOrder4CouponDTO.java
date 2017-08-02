package com.tp.dto.ord.remote;

import java.util.Map;

import com.tp.model.BaseDO;
import com.tp.model.ord.SubOrder;

/**
 * 优惠券订单DTO
 * 
 * @author szy
 * @version 0.0.1
 */
public class SubOrder4CouponDTO extends BaseDO {

	private static final long serialVersionUID = -2944227507275227869L;
	
	/** 子订单 */
	private Map<SubOrder, Double> subMap;
	/** 优惠券code */
	private String couponCode;
	/** 优惠券面值 */
	private Double couponAmount;
	
	
	public Map<SubOrder, Double> getSubMap() {
		return subMap;
	}
	public void setSubMap(Map<SubOrder, Double> subMap) {
		this.subMap = subMap;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public Double getCouponAmount() {
		return couponAmount;
	}
	public void setCouponAmount(Double couponAmount) {
		this.couponAmount = couponAmount;
	}
}
