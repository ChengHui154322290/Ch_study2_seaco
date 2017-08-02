/**
 * 
 */
package com.tp.dto.mmp;

import java.io.Serializable;

/**
 * <pre>
 * 活动商品DTO
 * </pre>
 * 
 * @author szy
 *
 */
public class TopicItemDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 432385259233608110L;

	/**
	 * 主键
	 */
	private Long id;

	/**
	 * 排序
	 */
	private Integer sortIndex;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * sku
	 */
	private String sku;

	/**
	 * 商品颜色
	 */
	private String itemColor;

	/**
	 * 商品尺寸
	 */
	private String itemSize;

	/**
	 * 专题Id
	 */
	private Long topicId;

	/**
	 * 产品Id
	 */
	private Long itemId;

	/**
	 * 供应商编号
	 */
	private int supplierId;

	/**
	 * 图片
	 */
	private String topicImage;

	/**
	 * 活动价
	 */
	private Double topicPrice;

	/**
	 * 限购数量
	 */
	private Integer limitAmount;

	/**
	 * 限购总量
	 */
	private Integer limitTotal;

	/**
	 * 销售数量
	 */
	private Integer saledAmount;

	/**
	 * 仓库名
	 */
	private String stockLocation;

	/**
	 * 库存数量
	 */
	private Integer stockAmout;

	/**
	 * 备注
	 */
	private String remark;

	/** 通关渠道 */
	private Long bondedArea;

	/** 仓库类型 */
	private Integer whType;

	/** 国家序号 */
	private Long countryId;

	/** 国家名称 */
	private String countryName;

	/** 购买方式 1-普通 2-立即购买 */
	private Integer purchaseMethod;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the sortIndex
	 */
	public Integer getSortIndex() {
		return sortIndex;
	}

	/**
	 * @param sortIndex
	 *            the sortIndex to set
	 */
	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
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
	 * @return the itemColor
	 */
	public String getItemColor() {
		return itemColor;
	}

	/**
	 * @param itemColor
	 *            the itemColor to set
	 */
	public void setItemColor(String itemColor) {
		this.itemColor = itemColor;
	}

	/**
	 * @return the itemSize
	 */
	public String getItemSize() {
		return itemSize;
	}

	/**
	 * @param itemSize
	 *            the itemSize to set
	 */
	public void setItemSize(String itemSize) {
		this.itemSize = itemSize;
	}

	/**
	 * @return the topicPrice
	 */
	public Double getTopicPrice() {
		return topicPrice;
	}

	/**
	 * @param topicPrice
	 *            the topicPrice to set
	 */
	public void setTopicPrice(Double topicPrice) {
		this.topicPrice = topicPrice;
	}

	/**
	 * @return the limitAmount
	 */
	public Integer getLimitAmount() {
		return limitAmount;
	}

	/**
	 * @param limitAmount
	 *            the limitAmount to set
	 */
	public void setLimitAmount(Integer limitAmount) {
		this.limitAmount = limitAmount;
	}

	/**
	 * @return the limitTotal
	 */
	public Integer getLimitTotal() {
		return limitTotal;
	}

	/**
	 * @param limitTotal
	 *            the limitTotal to set
	 */
	public void setLimitTotal(Integer limitTotal) {
		this.limitTotal = limitTotal;
	}

	/**
	 * @return the saledAmount
	 */
	public Integer getSaledAmount() {
		return saledAmount;
	}

	/**
	 * @param saledAmount
	 *            the saledAmount to set
	 */
	public void setSaledAmount(Integer saledAmount) {
		this.saledAmount = saledAmount;
	}

	/**
	 * @return the stockLocation
	 */
	public String getStockLocation() {
		return stockLocation;
	}

	/**
	 * @param stockLocation
	 *            the stockLocation to set
	 */
	public void setStockLocation(String stockLocation) {
		this.stockLocation = stockLocation;
	}

	/**
	 * @return the stockAmout
	 */
	public Integer getStockAmout() {
		return stockAmout;
	}

	/**
	 * @param stockAmout
	 *            the stockAmout to set
	 */
	public void setStockAmout(Integer stockAmout) {
		this.stockAmout = stockAmout;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the topicId
	 */
	public Long getTopicId() {
		return topicId;
	}

	/**
	 * @param topicId
	 *            the topicId to set
	 */
	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	/**
	 * @return the itemId
	 */
	public Long getItemId() {
		return itemId;
	}

	/**
	 * @param itemId
	 *            the itemId to set
	 */
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the topicImage
	 */
	public String getTopicImage() {
		return topicImage;
	}

	/**
	 * @param topicImage
	 *            the topicImage to set
	 */
	public void setTopicImage(String topicImage) {
		this.topicImage = topicImage;
	}

	/**
	 * @return the supplierId
	 */
	public int getSupplierId() {
		return supplierId;
	}

	/**
	 * @param supplierId
	 *            the supplierId to set
	 */
	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	/**
	 * @return the bondedArea
	 */
	public Long getBondedArea() {
		return bondedArea;
	}

	/**
	 * @param bondedArea
	 *            the bondedArea to set
	 */
	public void setBondedArea(Long bondedArea) {
		this.bondedArea = bondedArea;
	}

	/**
	 * @return the whType
	 */
	public Integer getWhType() {
		return whType;
	}

	/**
	 * @param whType
	 *            the whType to set
	 */
	public void setWhType(Integer whType) {
		this.whType = whType;
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
	public String getCountryName() {
		return countryName;
	}

	/**
	 * @param countryName
	 *            the countryName to set
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
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

}
