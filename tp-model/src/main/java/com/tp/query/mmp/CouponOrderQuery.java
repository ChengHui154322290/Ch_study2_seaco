package com.tp.query.mmp;

import java.io.Serializable;
import java.util.List;

import com.tp.dto.mmp.CouponOrderDTO;

public class CouponOrderQuery implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6068035391454332561L;

	private List< CouponOrderDTO> couponOrderDTOList;
	
	/**  当前的总价  */
	private Double price;
	
	private Long userId;
	
	/** 平台*/
	private Integer platformType;
	
	/**
	 * 商品类型，1 非海淘   2海淘
	 */
	private Integer itemType;
	
	
	public Integer getItemType() {
		return itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}

	public Integer getPlatformType() {
		return platformType;
	}

	public void setPlatformType(Integer platformType) {
		this.platformType = platformType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<CouponOrderDTO> getCouponOrderDTOList() {
		return couponOrderDTOList;
	}

	public void setCouponOrderDTOList(List<CouponOrderDTO> couponOrderDTOList) {
		this.couponOrderDTOList = couponOrderDTOList;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	
	
	
	
	
}
