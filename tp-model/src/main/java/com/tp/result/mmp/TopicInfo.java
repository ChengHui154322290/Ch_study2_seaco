package com.tp.result.mmp;

import java.io.Serializable;
import java.util.Date;

public class TopicInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1770055626235808642L;

	private long topicId;

	private String name;

	private String image;

	/** 明日预告的图片 */
	private String imageNew;

	private Date startTime;

	private Date endTime;

	private int lastingType;

	private int status;

	/** 可能感兴趣图片全路径 */
	private String imageInterested;

	/** 海淘图片路径 */
	private String imageHitao;

	/** 折扣率 */
	private String discount;

	/** 专题类型 */
	private Integer topicType;

	/** 如果是单品团，则传递单品团的单品的sku */
	private String itemSku;

	/** 活动的进行状态 */
	private Integer progress;

	/** 活动卖点 */
	private String topicPoint;

	/** PC首页图片 */
	private String pcImage;

	/** PC感兴趣首页图片 */
	private String pcInterestImage;

	/** 商城图片 */
	private String mallImage;

	/** 海淘图片 */
	private String haitaoImage;

	/** 移动首页图片 */
	private String mobileImage;

	/**
	 * @return the pcImage
	 */
	public String getPcImage() {
		return pcImage;
	}

	/**
	 * @param pcImage
	 *            the pcImage to set
	 */
	public void setPcImage(String pcImage) {
		this.pcImage = pcImage;
	}

	/**
	 * @return the pcInterestImage
	 */
	public String getPcInterestImage() {
		return pcInterestImage;
	}

	/**
	 * @param pcInterestImage
	 *            the pcInterestImage to set
	 */
	public void setPcInterestImage(String pcInterestImage) {
		this.pcInterestImage = pcInterestImage;
	}

	/**
	 * @return the mallImage
	 */
	public String getMallImage() {
		return mallImage;
	}

	/**
	 * @param mallImage
	 *            the mallImage to set
	 */
	public void setMallImage(String mallImage) {
		this.mallImage = mallImage;
	}

	/**
	 * @return the haitaoImage
	 */
	public String getHaitaoImage() {
		return haitaoImage;
	}

	/**
	 * @param haitaoImage
	 *            the haitaoImage to set
	 */
	public void setHaitaoImage(String haitaoImage) {
		this.haitaoImage = haitaoImage;
	}

	/**
	 * @return the mobileImage
	 */
	public String getMobileImage() {
		return mobileImage;
	}

	/**
	 * @param mobileImage
	 *            the mobileImage to set
	 */
	public void setMobileImage(String mobileImage) {
		this.mobileImage = mobileImage;
	}

	public String getImageNew() {
		return imageNew;
	}

	public void setImageNew(String imageNew) {
		this.imageNew = imageNew;
	}

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public Integer getTopicType() {
		return topicType;
	}

	public void setTopicType(Integer topicType) {
		this.topicType = topicType;
	}

	public String getItemSku() {
		return itemSku;
	}

	public void setItemSku(String itemSku) {
		this.itemSku = itemSku;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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

	public int getLastingType() {
		return lastingType;
	}

	public void setLastingType(int lastingType) {
		this.lastingType = lastingType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
	 * @return the topicPoint
	 */
	public String getTopicPoint() {
		return topicPoint;
	}

	/**
	 * @param topicPoint
	 *            the topicPoint to set
	 */
	public void setTopicPoint(String topicPoint) {
		this.topicPoint = topicPoint;
	}

	/**
	 * @return the imageHitao
	 */
	public String getImageHitao() {
		return imageHitao;
	}

	/**
	 * @param imageHitao
	 *            the imageHitao to set
	 */
	public void setImageHitao(String imageHitao) {
		this.imageHitao = imageHitao;
	}

}
