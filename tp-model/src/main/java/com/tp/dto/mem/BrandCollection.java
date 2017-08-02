package com.tp.dto.mem;

import java.io.Serializable;

public class BrandCollection implements Serializable{

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = 6978265760639915406L;
	
	/** skuCode */
	private Long brandId;
	
	/** topicId */
	private Long topicId;
	
	/** 是否被收藏 */
	private Boolean isCollection = false;

	public BrandCollection() {}

	public BrandCollection(Long brandId, Boolean isCollection) {
		super();
		this.brandId = brandId;
		this.isCollection = isCollection;
	}

	public BrandCollection(Long brandId, Long topicId, Boolean isCollection) {
		super();
		this.brandId = brandId;
		this.topicId = topicId;
		this.isCollection = isCollection;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
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
		return "ItemCollection [brandId=" + brandId + ", topicId=" + topicId
				+ ", isCollection=" + (isCollection?"已收藏":"未收藏") + "]";
	}
	
	
	
}
