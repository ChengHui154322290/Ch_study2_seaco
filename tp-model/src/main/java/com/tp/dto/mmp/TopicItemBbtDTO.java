/**
 * 
 */
package com.tp.dto.mmp;

import java.io.Serializable;

/**
 * @author szy
 *
 */
public class TopicItemBbtDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4862975038769942203L;

	/** 主键 */
	private Long id;

	/** 活动图片 */
	private String topicImage;

	/** 活动价格 */
	private Double topicPrice;

	/** 专题id */
	private Long topicId;

	/** 商品sku */
	private String sku;

	/** 商品名称 */
	private String name;

	private Boolean isWap = false;

	private Boolean isPc = false;

	/** 商品原价 */
	private Double salePrice;

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
	 * @return the isWap
	 */
	public Boolean getIsWap() {
		return isWap;
	}

	/**
	 * @param isWap
	 *            the isWap to set
	 */
	public void setIsWap(Boolean isWap) {
		this.isWap = isWap;
	}

	/**
	 * @return the isPc
	 */
	public Boolean getIsPc() {
		return isPc;
	}

	/**
	 * @param isPc
	 *            the isPc to set
	 */
	public void setIsPc(Boolean isPc) {
		this.isPc = isPc;
	}

	/**
	 * @return the salePrice
	 */
	public Double getSalePrice() {
		return salePrice;
	}

	/**
	 * @param salePrice
	 *            the salePrice to set
	 */
	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

}
