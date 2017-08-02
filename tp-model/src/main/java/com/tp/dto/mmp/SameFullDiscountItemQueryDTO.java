package com.tp.dto.mmp;

import java.io.Serializable;

public class SameFullDiscountItemQueryDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1475414184765746333L;

	private Long fullDisId; // 满减ID

	private Integer platform; // 平台类型

	private Integer pageId;

	private Integer pageSize;

	/** 1 -价格升序 2 -价格降序 默认价格升序 1 */
	private Integer orderType;

	private Integer areaId;
	
	/**
	 * 是否海淘购物车
	 */
	private Boolean hitao;

	/**
	 * @return the hitao
	 */
	public Boolean getHitao() {
		return hitao;
	}

	/**
	 * @param hitao the hitao to set
	 */
	public void setHitao(Boolean hitao) {
		this.hitao = hitao;
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

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getPageId() {
		return pageId;
	}

	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getFullDisId() {
		return fullDisId;
	}

	public void setFullDisId(Long fullDisId) {
		this.fullDisId = fullDisId;
	}

	public Integer getPlatform() {
		return platform;
	}

	public void setPlatform(Integer platform) {
		this.platform = platform;
	}

}
