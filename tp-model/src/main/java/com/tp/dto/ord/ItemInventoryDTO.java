/**
 * NewHeight.com Inc.
 * Copyright (c) 2008-2010 All Rights Reserved.
 */
package com.tp.dto.ord;

import java.io.Serializable;


/**
 * <pre>
 * 商品仓库信息
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class ItemInventoryDTO implements Serializable {
    
	private static final long serialVersionUID = 4675742639978446532L;

	/** topic id */
	private Long topicId;

	/** sku code */
	private String skuCode;
	
	/** 仓库Id */
	private Long storageId;
	
	/** 仓库类型 */
	private Integer storageType;
	
	/** 通关渠道 */
	private Long bondedArea;
	
	/** 通关渠道名称 */
	private String bondedAreaName;
	
	/** 货号 */
	private String articleNumber;

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public Long getStorageId() {
		return storageId;
	}

	public void setStorageId(Long storageId) {
		this.storageId = storageId;
	}

	public Integer getStorageType() {
		return storageType;
	}

	public void setStorageType(Integer storageType) {
		this.storageType = storageType;
	}

	public Long getBondedArea() {
		return bondedArea;
	}

	public void setBondedArea(Long bondedArea) {
		this.bondedArea = bondedArea;
	}

	public String getArticleNumber() {
		return articleNumber;
	}

	public void setArticleNumber(String articleNumber) {
		this.articleNumber = articleNumber;
	}

	public String getBondedAreaName() {
		return bondedAreaName;
	}

	public void setBondedAreaName(String bondedAreaName) {
		this.bondedAreaName = bondedAreaName;
	}
}
