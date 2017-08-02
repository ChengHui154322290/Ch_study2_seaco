package com.tp.dto.mmp;

import java.io.Serializable;
import java.util.Date;

/**
 * 相同满减商品信息
 * 
 * @author szy
 *
 */
public class FullDiscountItemInfo implements Serializable {

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

	/** 限购数量 **/
	private int limitAmount;

	/** 促销价格 **/
	private double topicPrice;

	/** 商品原价 */
	private Double salePrice;

	/** 本商品促销图片 **/
	private String topicImage;

	/** 商品名称 */
	private String itemName;

	/*** 多少人已经买   暂不提供*/
	private Integer saledAmount;

	private Long fullDiscountId;
	
	private String fullDiscountName;
	
	/** 专题开始时间 **/
	private Date startTime;

	/** 专题结束时间 **/
	private Date endTime;

	/**
	 * 当前满减的状态
	 */
	private Integer status;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FullDiscountItemInfo [sku=");
		builder.append(sku);
		builder.append(", itemId=");
		builder.append(itemId);
		builder.append(", topicId=");
		builder.append(topicId);
		builder.append(", limitAmount=");
		builder.append(limitAmount);
		builder.append(", topicPrice=");
		builder.append(topicPrice);
		builder.append(", salePrice=");
		builder.append(salePrice);
		builder.append(", topicImage=");
		builder.append(topicImage);
		builder.append(", itemName=");
		builder.append(itemName);
		builder.append(", saledAmount=");
		builder.append(saledAmount);
		builder.append(", fullDiscountId=");
		builder.append(fullDiscountId);
		builder.append(", fullDiscountName=");
		builder.append(fullDiscountName);
		builder.append(", startTime=");
		builder.append(startTime);
		builder.append(", endTime=");
		builder.append(endTime);
		builder.append("]");
		return builder.toString();
	}
	
	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}



	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}



	public String getFullDiscountName() {
		return fullDiscountName;
	}

	public void setFullDiscountName(String fullDiscountName) {
		this.fullDiscountName = fullDiscountName;
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

	public Long getFullDiscountId() {
		return fullDiscountId;
	}

	public void setFullDiscountId(Long fullDiscountId) {
		this.fullDiscountId = fullDiscountId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
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


	public Integer getSaledAmount() {
		return saledAmount;
	}

	public void setSaledAmount(Integer saledAmount) {
		this.saledAmount = saledAmount;
	}


	
}
