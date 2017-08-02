package com.tp.query.mmp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CmsTopicQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3333566347041332400L;

	private long topicId;

	private String name;

	private String sku;

	private Date startTime;

	private Date endTime;

	private int pageSize;

	private int pageId;

	/** 专题类型 */
	private Integer topicType;

	/*** 平台类型 */
	private Integer platformType;

	/*** 所在地区的id */
	private Integer areaId;

	/** 品牌序号 */
	private List<Long> brandIds;

	/** 类目序号 */
	private Long categoryId;

	/** 排序字段 */
	private List<String> orderSortColumns;
	
	/** 销售模式 */
	private Integer salesPartten;

	/** 排序方式 是否降序 */
	private boolean desc = true;
	
	/** 是否有库存 */
	private boolean stock = true;

	/** 使用范围Id */
	private List<Long> applicableIds;

	public Integer getPlatformType() {
		return platformType;
	}

	public void setPlatformType(Integer platformType) {
		this.platformType = platformType;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer getTopicType() {
		return topicType;
	}

	public void setTopicType(Integer topicType) {
		this.topicType = topicType;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageId() {
		return pageId;
	}

	public void setPageId(int pageId) {
		this.pageId = pageId;
	}

	public long getTopicId() {
		return topicId;
	}

	public void setTopicId(long topicId) {
		this.topicId = topicId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
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

	/**
	 * @return the orderSortColumns
	 */
	public List<String> getOrderSortColumns() {
		return orderSortColumns;
	}

	/**
	 * @param orderSortColumns
	 *            the orderSortColumns to set
	 */
	public void setOrderSortColumns(List<String> orderSortColumns) {
		this.orderSortColumns = orderSortColumns;
	}

	/**
	 * @return the desc
	 */
	public boolean isDesc() {
		return desc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(boolean desc) {
		this.desc = desc;
	}

	/**
	 * @return the brandIds
	 */
	public List<Long> getBrandIds() {
		return brandIds;
	}

	/**
	 * @param brandIds
	 *            the brandIds to set
	 */
	public void setBrandIds(List<Long> brandIds) {
		this.brandIds = brandIds;
	}

	/**
	 * @return the applicableIds
	 */
	public List<Long> getApplicableIds() {
		return applicableIds;
	}

	/**
	 * @param applicableIds
	 *            the applicableIds to set
	 */
	public void setApplicableIds(List<Long> applicableIds) {
		this.applicableIds = applicableIds;
	}

	/**
	 * @return the stock
	 */
	public boolean isStock() {
		return stock;
	}

	/**
	 * @param stock the stock to set
	 */
	public void setStock(boolean stock) {
		this.stock = stock;
	}

	/**
	 * @return the categoryId
	 */
	public Long getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the salesPartten
	 */
	public Integer getSalesPartten() {
		return salesPartten;
	}

	/**
	 * @param salesPartten the salesPartten to set
	 */
	public void setSalesPartten(Integer salesPartten) {
		this.salesPartten = salesPartten;
	}
	
}
