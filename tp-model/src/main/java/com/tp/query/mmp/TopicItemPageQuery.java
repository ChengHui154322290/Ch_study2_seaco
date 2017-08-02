package com.tp.query.mmp;

import java.io.Serializable;

/**
 * @author szy
 * 专题页面，根据条件查询专题下的商品列表
 * @version 0.0.1
 */
public class TopicItemPageQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6896217805487855001L;

	private Long  topicId;
	
	private Long categoryId;
	
	private Long brandId;
	
	private String priceOrder;
	
	private Boolean stock;
	
	private Integer pageId = 1;
	
	private Integer pageSize = 10;
	
	// filter by dss
	private Long promoterId;
	

	public Long getPromoterId() {
		return promoterId;
	}

	public void setPromoterId(Long promoterId) {
		this.promoterId = promoterId;
	}

	public long getTopicId() {
		return topicId;
	}

	public void setTopicId(long topicId) {
		this.topicId = topicId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}


	public String getPriceOrder() {
		return priceOrder;
	}

	public void setPriceOrder(String priceOrder) {
		this.priceOrder = priceOrder;
	}

	public Boolean getStock() {
		return stock;
	}

	public void setStock(Boolean stock) {
		this.stock = stock;
	}

	public Integer getPageId() {
		return pageId;
	}

	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	

}
