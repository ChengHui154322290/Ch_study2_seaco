package com.tp.dto.mmp;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.vo.mmp.CouponConstant;
import com.tp.dto.mmp.enums.CouponUserSource;

/**
 * 后台管理 ，优惠券信息
 * 
 * @author szy
 *
 */

public class CouponUserInfoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1868428977878467159L;

	/** 优惠券的批次id */
	private Long couponId;

	/** 优惠券的id */
	private Long couponUserId;

	private String couponName;

	private String toUserName;

	/** 发放人姓名 */
	private String sendEmp;

	private Long toUserId;

	private String number;
	/** 面值 */
	private Double faceValue;
	/** 需满金额 */
	private Double needOverMon;

	private Integer couponType;

	private Integer couponUserStatus;

	/** 开始使用时间 */
	private Date couponUseStime;

	/** 结束使用时间 */
	private Date couponUseEtime;

	/** 适用平台 */
	private String usePlantform;

	/** 适用范围 */
	private String useRange;

	/** 发放时间 */
	private Date sendTime;

	/** 发放人员 */
	private String sendEmpId;

	/** 优惠券 使用 类型 */
	private String couponUseType;

	/** 领取 几日内有效 */
	private Integer useReceiveDay;

	/** 来源类型 */
	private Integer source;

	/** 来源名称 */
	private String sourceName;

	/** 参考编号 */
	private String refCode;
	
	private String toUserLogin;
	
	/** 是否是海淘**/
	private String hitaoSign;
	
	
	public String getHitaoSign() {
		return hitaoSign;
	}

	public void setHitaoSign(String hitaoSign) {
		this.hitaoSign = hitaoSign;
	}

	public String getToUserLogin() {
		return toUserLogin;
	}

	public void setToUserLogin(String toUserLogin) {
		this.toUserLogin = toUserLogin;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public Long getCouponUserId() {
		return couponUserId;
	}

	public void setCouponUserId(Long couponUserId) {
		this.couponUserId = couponUserId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public Long getToUserId() {
		return toUserId;
	}

	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Double getFaceValue() {
		return faceValue;
	}

	public void setFaceValue(Double faceValue) {
		this.faceValue = faceValue;
	}

	public Double getNeedOverMon() {
		return needOverMon;
	}

	public void setNeedOverMon(Double needOverMon) {
		this.needOverMon = needOverMon;
	}

	public Integer getCouponType() {
		return couponType;
	}

	public void setCouponType(Integer couponType) {
		this.couponType = couponType;
	}

	public Integer getCouponUserStatus() {
		return couponUserStatus;
	}

	public void setCouponUserStatus(Integer couponUserStatus) {
		this.couponUserStatus = couponUserStatus;
	}

	/**
	 * @return the couponUseStime
	 */
	public Date getCouponUseStime() {
		return couponUseStime;
	}

	/**
	 * @param couponUseStime
	 *            the couponUseStime to set
	 */
	public void setCouponUseStime(Date couponUseStime) {
		this.couponUseStime = couponUseStime;
	}

	/**
	 * @return the couponUseEtime
	 */
	public Date getCouponUseEtime() {
		return couponUseEtime;
	}

	/**
	 * @param couponUseEtime
	 *            the couponUseEtime to set
	 */
	public void setCouponUseEtime(Date couponUseEtime) {
		this.couponUseEtime = couponUseEtime;
	}

	/**
	 * @return the usePlantform
	 */
	public String getUsePlantform() {
		return usePlantform;
	}

	/**
	 * @param usePlantform
	 *            the usePlantform to set
	 */
	public void setUsePlantform(String usePlantform) {
		this.usePlantform = usePlantform;
	}

	/**
	 * 全范围
	 * 
	 * @return
	 */
	public Boolean getAllRange() {
		if (null != this.useRange) {
			return this.useRange.contains(CouponConstant.RANGE_ALL);
		}
		return false;
	}

	/**
	 * 自营+海淘
	 * 
	 * @return
	 */
	public Boolean getSelfRange() {
		if (null != this.useRange) {
			return this.useRange.contains(CouponConstant.RANGE_SELF);
		}
		return false;
	}

	/**
	 * 联营
	 * 
	 * @return
	 */
	public Boolean getJointRange() {
		if (null != this.useRange) {
			return this.useRange.contains(CouponConstant.RANGE_JOINT);
		}
		return false;
	}




	/**
	 * @return the useRange
	 */
	public String getUseRange() {
		return useRange;
	}

	/**
	 * @param useRange
	 *            the useRange to set
	 */
	public void setUseRange(String useRange) {
		this.useRange = useRange;
	}

	/**
	 * @return the sendEmp
	 */
	public String getSendEmp() {
		return sendEmp;
	}

	/**
	 * @param sendEmp
	 *            the sendEmp to set
	 */
	public void setSendEmp(String sendEmp) {
		this.sendEmp = sendEmp;
	}

	/**
	 * @return the sendTime
	 */
	public Date getSendTime() {
		return sendTime;
	}

	/**
	 * @param sendTime
	 *            the sendTime to set
	 */
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	/**
	 * @return the sendEmpId
	 */
	public String getSendEmpId() {
		return sendEmpId;
	}

	/**
	 * @param sendEmpId
	 *            the sendEmpId to set
	 */
	public void setSendEmpId(String sendEmpId) {
		this.sendEmpId = sendEmpId;
	}

	/**
	 * @return the couponUseType
	 */
	public String getCouponUseType() {
		return couponUseType;
	}

	/**
	 * @param couponUseType
	 *            the couponUseType to set
	 */
	public void setCouponUseType(String couponUseType) {
		this.couponUseType = couponUseType;
	}

	/**
	 * @return the useReceiveDay
	 */
	public Integer getUseReceiveDay() {
		return useReceiveDay;
	}

	/**
	 * @param useReceiveDay
	 *            the useReceiveDay to set
	 */
	public void setUseReceiveDay(Integer useReceiveDay) {
		this.useReceiveDay = useReceiveDay;
	}

	/**
	 * @return the source
	 */
	public Integer getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(Integer source) {
		this.source = source;
	}

	/**
	 * @return the sourceName
	 */
	public String getSourceName() {
		return sourceName;
	}

	/**
	 * @param sourceName
	 *            the sourceName to set
	 */
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	/**
	 * @return the refCode
	 */
	public String getRefCode() {
		return refCode;
	}

	/**
	 * @param refCode
	 *            the refCode to set
	 */
	public void setRefCode(String refCode) {
		this.refCode = refCode;
	}
	
	public String getSourceFrom(){
		if(this.source != null && this.source == CouponUserSource.FANS_PLAN.getValue()){
			return CouponUserSource.FANS_PLAN.getDescription();
		}
		return null;
	}
}
