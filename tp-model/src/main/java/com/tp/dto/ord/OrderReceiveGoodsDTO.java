/**
 * NewHeight.com Inc.
 * Copyright (c) 2008-2010 All Rights Reserved.
 */
package com.tp.dto.ord;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * <pre>
 * 订单收货实体类
 * </pre>
 * 
 * @author szy
 * @time 2015-1-19 下午4:17:06
 */
public class OrderReceiveGoodsDTO implements Serializable {
	private static final long serialVersionUID = 6482344197907575591L;
	/** 订单编号（子订单） */
	private Long subOrderCode;

	/** 收货时间 */
	private Date receiptDate;

	public Long getSubOrderCode() {
		return subOrderCode;
	}

	public void setSubOrderCode(Long subOrderCode) {
		this.subOrderCode = subOrderCode;
	}

	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
