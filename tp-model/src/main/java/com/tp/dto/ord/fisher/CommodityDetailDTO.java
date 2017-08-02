/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.dto.ord.fisher;

import java.io.Serializable;

/**
 * <pre>
 * 商品详情
 * </pre>
 * 
 * @author szy
 * @time 2015-4-17 下午3:33:26
 */
public class CommodityDetailDTO implements Serializable {
	private static final long serialVersionUID = 6632800085029117444L;
	/** 商品编码 */
	private String commodityCode;
	/** 商品名称 */
	private String commodityName;
	/** 商品规格 */
	private String commoditySpec;
	/** 厂商货号 */
	private String commodityArtNo;
	/** 商品发货数量 */
	private String qty;

	public String getCommodityCode() {
		return commodityCode;
	}

	public void setCommodityCode(String commodityCode) {
		this.commodityCode = commodityCode;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public String getCommoditySpec() {
		return commoditySpec;
	}

	public void setCommoditySpec(String commoditySpec) {
		this.commoditySpec = commoditySpec;
	}

	public String getCommodityArtNo() {
		return commodityArtNo;
	}

	public void setCommodityArtNo(String commodityArtNo) {
		this.commodityArtNo = commodityArtNo;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
