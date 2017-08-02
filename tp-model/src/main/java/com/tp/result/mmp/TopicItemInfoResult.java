package com.tp.result.mmp;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品查询专题信息
 * 
 * @author szy
 *
 */
public class TopicItemInfoResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6613355546178923769L;

	/** sku 信息 **/
	private String sku;
	/** 商品id **/

	private long itemId;
	/** 专题id **/

	private long topicId;

	/** 活动类型 */
	private Integer topicType;

	/** 活动地址 */
	private String topicUrl;

	/** 专题名称 **/
	private String topicName;

	/** 库存数量 **/
	private int stockAmount;

	/** 限购数量 **/
	private int limitAmount;

	/** 促销价格 **/
	private double topicPrice;

	/** 商品原价 */
	private Double salePrice;

	/** 本商品促销图片 **/
	private String topicImage;

	/** 可能感兴趣图片全路径 */
	private String imageInterested;

	/** 专题状态 **/
	private int topicStatus;

	/** 专题持续类型 长期是0， 固定期限是1 **/
	private int lastingType;

	/** 专题开始时间 **/
	private Date startTime;

	/** 专题结束时间 **/
	private Date endTime;

	/** 商品名称 */
	private String itemName;

	/*** 多少人已经买 */
	private Integer saledAmount;

	/** 仓库Id */
	private Long stockLocationId;

	/** 仓库名 */
	private String stockLocationName;

	/** 商品锁定状态 */
	private Integer lockStatus;

	/** 国家序号 */
	private Long countryId;

	/** 国家名称 */
	private Long countryName;

	/** 是否有库存 */
	private boolean hasStock;

	/** 购买方式 1-普通 2-立即购买 */
	private Integer purchaseMethod;
	
	/** 活动商品标签 **/
	private String itemTags;
	
	/** 活动是否预占库存：0否1是 */
	private Integer topicInventoryFlag;
	

	public Integer getTopicInventoryFlag() {
		return topicInventoryFlag;
	}

	public void setTopicInventoryFlag(Integer topicInventoryFlag) {
		this.topicInventoryFlag = topicInventoryFlag;
	}

	public Integer getTopicType() {
		return topicType;
	}

	public void setTopicType(Integer topicType) {
		this.topicType = topicType;
	}

	public String getTopicUrl() {
		return topicUrl;
	}

	public void setTopicUrl(String topicUrl) {
		this.topicUrl = topicUrl;
	}

	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public long getTopicId() {
		return topicId;
	}

	public void setTopicId(long topicId) {
		this.topicId = topicId;
	}

	public int getStockAmount() {
		return stockAmount;
	}

	public void setStockAmount(int stockAmount) {
		this.stockAmount = stockAmount;
	}

	public int getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(int limitAmount) {
		this.limitAmount = limitAmount;
	}

	public double getTopicPrice() {
		return topicPrice;
	}

	public void setTopicPrice(double topicPrice) {
		this.topicPrice = topicPrice;
	}

	public String getTopicImage() {
		return topicImage;
	}

	public void setTopicImage(String topicImage) {
		this.topicImage = topicImage;
	}

	public int getTopicStatus() {
		return topicStatus;
	}

	public void setTopicStatus(int topicStatus) {
		this.topicStatus = topicStatus;
	}

	public int getLastingType() {
		return lastingType;
	}

	public void setLastingType(int lastingType) {
		this.lastingType = lastingType;
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

	public Integer getSaledAmount() {
		return saledAmount;
	}

	public void setSaledAmount(Integer saledAmount) {
		this.saledAmount = saledAmount;
	}

	/**
	 * @return the imageInterested
	 */
	public String getImageInterested() {
		return imageInterested;
	}

	/**
	 * @param imageInterested
	 *            the imageInterested to set
	 */
	public void setImageInterested(String imageInterested) {
		this.imageInterested = imageInterested;
	}

	/**
	 * @return the lockStatus
	 */
	public Integer getLockStatus() {
		return lockStatus;
	}

	/**
	 * @param lockStatus
	 *            the lockStatus to set
	 */
	public void setLockStatus(Integer lockStatus) {
		this.lockStatus = lockStatus;
	}

	/**
	 * @return the stockLocationId
	 */
	public Long getStockLocationId() {
		return stockLocationId;
	}

	/**
	 * @param stockLocationId
	 *            the stockLocationId to set
	 */
	public void setStockLocationId(Long stockLocationId) {
		this.stockLocationId = stockLocationId;
	}

	/**
	 * @return the stockLocationName
	 */
	public String getStockLocationName() {
		return stockLocationName;
	}

	/**
	 * @param stockLocationName
	 *            the stockLocationName to set
	 */
	public void setStockLocationName(String stockLocationName) {
		this.stockLocationName = stockLocationName;
	}

	/**
	 * @return the countryId
	 */
	public Long getCountryId() {
		return countryId;
	}

	/**
	 * @param countryId
	 *            the countryId to set
	 */
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	/**
	 * @return the countryName
	 */
	public Long getCountryName() {
		return countryName;
	}

	/**
	 * @param countryName
	 *            the countryName to set
	 */
	public void setCountryName(Long countryName) {
		this.countryName = countryName;
	}

	/**
	 * @return the hasStock
	 */
	public boolean isHasStock() {
		return hasStock;
	}

	/**
	 * @param hasStock the hasStock to set
	 */
	public void setHasStock(boolean hasStock) {
		this.hasStock = hasStock;
	}

	/**
	 * @return the purchaseMethod
	 */
	public Integer getPurchaseMethod() {
		return purchaseMethod;
	}

	/**
	 * @param purchaseMethod the purchaseMethod to set
	 */
	public void setPurchaseMethod(Integer purchaseMethod) {
		this.purchaseMethod = purchaseMethod;
	}

	public String getItemTags() {
		return itemTags;
	}

	public void setItemTags(String itemTags) {
		this.itemTags = itemTags;
	}
}
