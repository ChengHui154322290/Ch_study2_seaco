/**
 * 
 */
package com.tp.dto.mmp;

import java.io.Serializable;

/**
 * @author szy
 *
 */
public class TopicCouponDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1425732735480262740L;

	/** 主键 */
	private Long id;

	/** 显示排序 */
	private Integer sortIndex;

	/** 活动专题id */
	private Long topicId;

	/** 优惠券id */
	private Long couponId;

	/** 优惠券图片 */
	private String couponImage;

	/** 图片大小 1 - 1*1    2 - 1*2     3 - 1*4 */
	private Integer couponSize;
	
	/** 优惠券名称 */
	private String couponName;
	
	/** 优惠券图片全路径 */
	private String couponFullImage;
	
	/** 优惠券类型 */
	private Integer couponType;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
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
	 * @param sortIndex the sortIndex to set
	 */
	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

	/**
	 * @return the topicId
	 */
	public Long getTopicId() {
		return topicId;
	}

	/**
	 * @param topicId the topicId to set
	 */
	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	/**
	 * @return the couponId
	 */
	public Long getCouponId() {
		return couponId;
	}

	/**
	 * @param couponId the couponId to set
	 */
	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	/**
	 * @return the couponImage
	 */
	public String getCouponImage() {
		return couponImage;
	}

	/**
	 * @param couponImage the couponImage to set
	 */
	public void setCouponImage(String couponImage) {
		this.couponImage = couponImage;
	}

	/**
	 * @return the couponSize
	 */
	public Integer getCouponSize() {
		return couponSize;
	}

	/**
	 * @param couponSize the couponSize to set
	 */
	public void setCouponSize(Integer couponSize) {
		this.couponSize = couponSize;
	}

	/**
	 * @return the couponName
	 */
	public String getCouponName() {
		return couponName;
	}

	/**
	 * @param couponName the couponName to set
	 */
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	/**
	 * @return the couponFullImage
	 */
	public String getCouponFullImage() {
		return couponFullImage;
	}

	/**
	 * @param couponFullImage the couponFullImage to set
	 */
	public void setCouponFullImage(String couponFullImage) {
		this.couponFullImage = couponFullImage;
	}

	/**
	 * @return the couponType
	 */
	public Integer getCouponType() {
		return couponType;
	}

	/**
	 * @param couponType the couponType to set
	 */
	public void setCouponType(Integer couponType) {
		this.couponType = couponType;
	}
	
}
