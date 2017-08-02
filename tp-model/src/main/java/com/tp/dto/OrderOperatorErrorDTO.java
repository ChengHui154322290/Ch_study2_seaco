/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.dto;

import java.io.Serializable;

/**
 * 订单处理失败信息
 * 
 * @author szy
 * @time 2016-1-21 下午1:44:03
 */
public class OrderOperatorErrorDTO implements Serializable {
	private static final long serialVersionUID = 1651903410988316921L;
	/** 子订单编号 */
	private Long subOrderCode;
	/** 错误编码 */
	private Integer errorCode;
	/** 错误信息描述 */
	private String errorMessage;
	/** 订单状态*/
	private String orderStatus;

	public OrderOperatorErrorDTO(Long subOrderCode, Integer errorCode) {
		super();
		this.subOrderCode = subOrderCode;
		this.errorCode = errorCode;
	}

	public OrderOperatorErrorDTO(Long subOrderCode, Integer errorCode, String errorMessage) {
		super();
		this.subOrderCode = subOrderCode;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public Long getSubOrderCode() {
		return subOrderCode;
	}

	public void setSubOrderCode(Long subOrderCode) {
		this.subOrderCode = subOrderCode;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
}
