package com.tp.dto.mmp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tp.model.mmp.CouponRange;

public class OrderCouponDTO  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5653163107098806936L;
	
	/** 用户优惠券Id*/
	private Long couponUserId;
	
	/** 面值*/
	private Integer faceValue;
	
	/** 优惠券的名称*/
	private String couponName;
	
	/** 优惠券类型  0 : 满减券  1：现金券*/
	private Integer couponType;
	
	/** 适用范围0:全部 1：自营+代销 2：联营 string 字符串 以 “,” 分割*/
	private String useRange; 
	
	/** 最低消费*/
	private Integer needOverMon;
	
	/** 当优惠券  时间段 需要赋值。 使用开始时间 */
	private Date couponUseStime;

	/** 当优惠券  时间段 需要赋值。 使用结束时间 */
	private Date couponUseEtime;
	
	private List<CouponRange> couponRangeList;
	
	/**发券主体 **/
	private Integer sourceType;
	/**商户ID **/
	private Long sourceId;
	/** 商家名称 **/
	private String sourceName;
	/**优惠金额*/
	private Double couponDiscount;
	/**关联批次*/
	private Long couponId;
	/**推广员ID*/
	private Long promoterId;
	/**使用状态*/
	private Integer couponStatus;
	
	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public List<CouponRange> getCouponRangeList() {
		return couponRangeList;
	}

	public void setCouponRangeList(List<CouponRange> couponRangeList) {
		this.couponRangeList = couponRangeList;
	}

	public Long getCouponUserId() {
		return couponUserId;
	}

	public void setCouponUserId(Long couponUserId) {
		this.couponUserId = couponUserId;
	}

	
	public Integer getFaceValue() {
		return faceValue;
	}

	public void setFaceValue(Integer faceValue) {
		this.faceValue = faceValue;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public Integer getCouponType() {
		return couponType;
	}

	public void setCouponType(Integer couponType) {
		this.couponType = couponType;
	}

	public Integer getNeedOverMon() {
		return needOverMon;
	}

	public void setNeedOverMon(Integer needOverMon) {
		this.needOverMon = needOverMon;
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

	public String getUseRange() {
		return useRange;
	}

	public void setUseRange(String useRange) {
		this.useRange = useRange;
	}

	public Double getCouponDiscount() {
		return couponDiscount;
	}

	public void setCouponDiscount(Double couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public Integer getCouponStatus() {
		return couponStatus;
	}

	public void setCouponStatus(Integer couponStatus) {
		this.couponStatus = couponStatus;
	}

	public Long getPromoterId() {
		return promoterId;
	}

	public void setPromoterId(Long promoterId) {
		this.promoterId = promoterId;
	}

}
