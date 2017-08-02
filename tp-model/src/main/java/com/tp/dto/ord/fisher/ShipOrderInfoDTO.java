/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.dto.ord.fisher;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <pre>
 * 发货订单信息
 * </pre>
 * 
 * @author szy
 * @time 2015-4-17 下午3:36:38
 */
public class ShipOrderInfoDTO implements Serializable {
	private static final long serialVersionUID = 6715446515284319894L;
	/** 订单编号 */
	private String orderCode;
	/** 快递公司编号 */
	private String companyNo;
	/** 快递单号 */
	private String expressNo;
	/** 发货时间 */
	private Date shippingTime;
	/** 包裹实际重量 */
	private Double realWeight;
	/** 商品详情列表 */
	private List<CommodityDetailDTO> details;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getCompanyNo() {
		return companyNo;
	}

	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public Date getShippingTime() {
		return shippingTime;
	}

	public void setShippingTime(Date shippingTime) {
		this.shippingTime = shippingTime;
	}

	public Double getRealWeight() {
		return realWeight;
	}

	public void setRealWeight(Double realWeight) {
		this.realWeight = realWeight;
	}

	public List<CommodityDetailDTO> getDetails() {
		return details;
	}

	public void setDetails(List<CommodityDetailDTO> details) {
		this.details = details;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
