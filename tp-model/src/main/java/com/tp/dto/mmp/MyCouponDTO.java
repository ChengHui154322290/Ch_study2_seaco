package com.tp.dto.mmp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyCouponDTO  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5653163107098806936L;
	
	private long couponId;
	
	/** 优惠券Id */
	private long couponUserId;
	
	private int faceValue;
	
	private String couponName;
	
	/** 使用状态   0 可用，1 已使用 2 已过期*/
	private int status;
	
	/** 优惠券类型  0 : 满减券  1：现金券*/
	private int couponType;
	
	private int needOverMon;
	
	/** 优惠券使用 类型 0：时间段有效   1：领取有效*/
	private Integer couponUseType;
	
	/** 当优惠券 使用类型为 0  时间段 需要赋值。 使用开始时间*/
	private Date couponUseStime;
	
	/**当优惠券 使用类型为 0  时间段 需要赋值。 使用结束时间*/
	private Date couponUseEtime;
	
	private String userCondition;
	
	/*0:不兑换  1：允许兑换*/
	private String exchangeXgMoney;

	/**
	 * 优惠券适用条件
	 */
	private List<String> rangeInclude = new ArrayList<>();

	/**
	 * 优惠券不适用条件
	 */
	private List<String> rangeNotInclude = new ArrayList<>();

	public List<String> getRangeInclude() {
		return rangeInclude;
	}

	public void setRangeInclude(List<String> rangeInclude) {
		this.rangeInclude = rangeInclude;
	}

	public List<String> getRangeNotInclude() {
		return rangeNotInclude;
	}

	public void setRangeNotInclude(List<String> rangeNotInclude) {
		this.rangeNotInclude = rangeNotInclude;
	}

	public String getUserCondition() {
		return userCondition;
	}

	public void setUserCondition(String userCondition) {
		this.userCondition = userCondition;
	}

	public long getCouponId() {
		return couponId;
	}

	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}

	public int getFaceValue() {
		return faceValue;
	}

	public void setFaceValue(int faceValue) {
		this.faceValue = faceValue;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getCouponType() {
		return couponType;
	}

	public void setCouponType(int couponType) {
		this.couponType = couponType;
	}

	public Integer getCouponUseType() {
		return couponUseType;
	}

	public void setCouponUseType(Integer couponUseType) {
		this.couponUseType = couponUseType;
	}

	public Date getCouponUseStime() {
		return couponUseStime;
	}

	public void setCouponUseStime(Date couponUseStime) {
		this.couponUseStime = couponUseStime;
	}

	public Date getCouponUseEtime() {
		return couponUseEtime;
	}

	public void setCouponUseEtime(Date couponUseEtime) {
		this.couponUseEtime = couponUseEtime;
	}

	public int getNeedOverMon() {
		return needOverMon;
	}

	public void setNeedOverMon(int needOverMon) {
		this.needOverMon = needOverMon;
	}

	/**
	 * @return the couponUserId
	 */
	public long getCouponUserId() {
		return couponUserId;
	}

	/**
	 * @param couponUserId the couponUserId to set
	 */
	public void setCouponUserId(long couponUserId) {
		this.couponUserId = couponUserId;
	}

	public String getExchangeXgMoney() {
		return exchangeXgMoney;
	}

	public void setExchangeXgMoney(String exchangeXgMoney) {
		this.exchangeXgMoney = exchangeXgMoney;
	}
	
	

}
