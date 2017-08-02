package com.tp.dto.promoter;

import java.io.Serializable;

/**
 * 
 * <pre>
 * 	分销上下架主题商品
 * </pre>
 *
 * @author zhs
 * @version 0.0.1
 */
public class PromoterTopicItemDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7832739413673395832L;

	/** 主题ID */
	private  Long topicId;
	
	/** 商品名称 */
	private String name;
	
	/** 商品 */
	private String sku;
	
	/** 销售价格 */	
	private Double salePrice;
	
	/** 活动价格 */
	private Double topicPrice;

	/** 商品上下架情况 */
	private Integer onShelves;
	
	/** 商品活动图片 */
	private String imgUrl;

	/** 商品分销比例 */
	private Double commissionRate;


	public Double getCommissionRate() {
		return commissionRate;
	}

	public void setCommissionRate(Double commissionRate) {
		this.commissionRate = commissionRate;
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
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

	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	public Double getTopicPrice() {
		return topicPrice;
	}

	public void setTopicPrice(Double topicPrice) {
		this.topicPrice = topicPrice;
	}

	public Integer getOnShelves() {
		return onShelves;
	}

	public void setOnShelves(Integer onShelves) {
		this.onShelves = onShelves;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
			
}
