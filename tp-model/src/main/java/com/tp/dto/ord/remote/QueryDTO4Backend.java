package com.tp.dto.ord.remote;

import java.util.Date;

import com.tp.model.BaseDO;

/**
 * 订单查询DTO（用户）
 * 
 * @author szy
 * @version 0.0.1
 */
public class QueryDTO4Backend extends BaseDO {

	private static final long serialVersionUID = -2944227507275227869L;
	
	/** 子订单号 */
	private String subCode;
	/** 订单号 */
	private String code;
	/** 类型 */
	private Integer type;
	/** 状态 */
	private Integer status;
	/** 下单时间范围 - 开始时间 */
	private Date startTime;
	/** 下单时间范围 - 结束时间 */
	private Date endTime;
	/** 登录名 */
	private String loginName;
	/** 收货人名称 */
	private String consigneeName;
	/** 供应商名称 */
	private String supplierName;
	/** 订单ID */
	private Long orderId;
	/** 用户ID */
	private Long memberId;
	
	public String getSubCode() {
		return subCode;
	}
	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getConsigneeName() {
		return consigneeName;
	}
	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
}
