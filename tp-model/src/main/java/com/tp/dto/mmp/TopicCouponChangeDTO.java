/**
 * 
 */
package com.tp.dto.mmp;

import java.io.Serializable;

/**
 * @author szy
 *
 */
public class TopicCouponChangeDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4309133324241733664L;

	/** 主键 */
	private Long id;

	/** 显示排序 */
	private Integer sortIndex;

	/** 活动变更单id */
	private Long topicChangeId;

	/** 优惠券id */
	private Long couponId;

	/** 优惠券图片 */
	private String couponImage;

	/** 图片大小 1 - 1*1 2 - 1*2 3 - 1*4 */
	private Integer couponSize;

	/** 优惠券名称 */
	private String couponName;

	/** 优惠券图片全路径 */
	private String couponFullImage;

	/** 优惠券类型 */
	private Integer couponType;
	
	/**
	 * 设置 主键
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 设置 显示排序
	 * 
	 * @param sortIndex
	 */
	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

	/**
	 * 设置 活动变更单id
	 * 
	 * @param topicChangeId
	 */
	public void setTopicChangeId(Long topicChangeId) {
		this.topicChangeId = topicChangeId;
	}

	/**
	 * 设置 优惠券id
	 * 
	 * @param couponId
	 */
	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	/**
	 * 设置 优惠券图片
	 * 
	 * @param couponImage
	 */
	public void setCouponImage(String couponImage) {
		this.couponImage = couponImage;
	}

	/**
	 * 设置 图片大小 1 - 1*1 2 - 1*2 3 - 1*4
	 * 
	 * @param couponSize
	 */
	public void setCouponSize(Integer couponSize) {
		this.couponSize = couponSize;
	}

	/**
	 * 获取 主键
	 * 
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 获取 显示排序
	 * 
	 * @return sortIndex
	 */
	public Integer getSortIndex() {
		return sortIndex;
	}

	/**
	 * 获取 活动变更单id
	 * 
	 * @return topicChangeId
	 */
	public Long getTopicChangeId() {
		return topicChangeId;
	}

	/**
	 * 获取 优惠券id
	 * 
	 * @return couponId
	 */
	public Long getCouponId() {
		return couponId;
	}

	/**
	 * 获取 优惠券图片
	 * 
	 * @return couponImage
	 */
	public String getCouponImage() {
		return couponImage;
	}

	/**
	 * 获取 图片大小 1 - 1*1 2 - 1*2 3 - 1*4
	 * 
	 * @return couponSize
	 */
	public Integer getCouponSize() {
		return couponSize;
	}

	/**
	 * @return the couponName
	 */
	public String getCouponName() {
		return couponName;
	}

	/**
	 * @param couponName
	 *            the couponName to set
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
