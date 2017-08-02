/**
 * 
 */
package com.tp.query.mmp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tp.dto.mmp.enums.CmsForcaseType;

/**
 * @author szy
 *
 */
public class TopicQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8286920740411851254L;

	private int rangeDays;

	private long topicId;

	private String name;

	private String sku;

	private Date startTime;

	private Date endTime;

	private int pageSize;

	private int pageId;

	/** 专题类型 1-单品团 2-品牌团 3-主题团 */
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

	/** 销售模式 1-不限 2-旗舰店 3-西客商城商城 4-洋淘派 5-闪购 6-秒杀 7-阶梯团 8-专场 */
	private Integer salesPartten;

	/** 排序方式 是否降序 */
	private boolean desc = true;

	/** 是否有库存 */
	private boolean stock = true;

	/** 返回结果方式 */
	private CmsForcaseType forcaseType;

	/**
	 * @return the topicId
	 */
	public long getTopicId() {
		return topicId;
	}

	/**
	 * @param topicId
	 *            the topicId to set
	 */
	public void setTopicId(long topicId) {
		this.topicId = topicId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the sku
	 */
	public String getSku() {
		return sku;
	}

	/**
	 * @param sku
	 *            the sku to set
	 */
	public void setSku(String sku) {
		this.sku = sku;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the pageId
	 */
	public int getPageId() {
		return pageId;
	}

	/**
	 * @param pageId
	 *            the pageId to set
	 */
	public void setPageId(int pageId) {
		this.pageId = pageId;
	}

	/**
	 * @return the topicType
	 */
	public Integer getTopicType() {
		return topicType;
	}

	/**
	 * @param topicType
	 *            the topicType to set
	 */
	public void setTopicType(Integer topicType) {
		this.topicType = topicType;
	}

	/**
	 * @return the platformType
	 */
	public Integer getPlatformType() {
		return platformType;
	}

	/**
	 * @param platformType
	 *            the platformType to set
	 */
	public void setPlatformType(Integer platformType) {
		this.platformType = platformType;
	}

	/**
	 * @return the areaId
	 */
	public Integer getAreaId() {
		return areaId;
	}

	/**
	 * @param areaId
	 *            the areaId to set
	 */
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
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
	 * @return the categoryId
	 */
	public Long getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryId
	 *            the categoryId to set
	 */
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
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
	 * @return the salesPartten
	 */
	public Integer getSalesPartten() {
		return salesPartten;
	}

	/**
	 * @param salesPartten
	 *            the salesPartten to set
	 */
	public void setSalesPartten(Integer salesPartten) {
		this.salesPartten = salesPartten;
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
	 * @return the stock
	 */
	public boolean isStock() {
		return stock;
	}

	/**
	 * @param stock
	 *            the stock to set
	 */
	public void setStock(boolean stock) {
		this.stock = stock;
	}

	/**
	 * @return the forcaseType
	 */
	public CmsForcaseType getForcaseType() {
		return forcaseType;
	}

	/**
	 * @param forcaseType
	 *            the forcaseType to set
	 */
	public void setForcaseType(CmsForcaseType forcaseType) {
		this.forcaseType = forcaseType;
	}

	/**
	 * @return the rangeDays
	 */
	public int getRangeDays() {
		return rangeDays;
	}

	/**
	 * @param rangeDays
	 *            the rangeDays to set
	 */
	public void setRangeDays(int rangeDays) {
		this.rangeDays = rangeDays;
	}

}
