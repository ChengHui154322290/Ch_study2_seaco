package com.tp.dto.mem;

import java.io.Serializable;

public class ItemCollection implements Serializable{

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = 6978265760639915406L;
	
	/** skuCode */
	private String skuCode;
	
	/** topicId */
	private Long topicId;
	
	/** 是否被收藏 */
	private Boolean isCollection = false;

	public ItemCollection() {}

	public ItemCollection(String skuCode, Boolean isCollection) {
		super();
		this.skuCode = skuCode;
		this.isCollection = isCollection;
	}

	public ItemCollection(String skuCode, Long topicId, Boolean isCollection) {
		super();
		this.skuCode = skuCode;
		this.topicId = topicId;
		this.isCollection = isCollection;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public Boolean getIsCollection() {
		return isCollection;
	}

	public void setIsCollection(Boolean isCollection) {
		this.isCollection = isCollection;
	}

	@Override
	public String toString() {
		return "ItemCollection [skuCode=" + skuCode + ", topicId=" + topicId
				+ ", isCollection=" + (isCollection?"已收藏":"未收藏") + "]";
	}
	
	
	
}
