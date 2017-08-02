package com.tp.dto.mmp;

import java.io.Serializable;

/**
 * 商品的满减信息
 * @author szy
 *
 */
public class ItemFullDiscountDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 满减对象名称*/
	private String name;
	
	 /** url */
	private String url;
	
	/** 满减ID */
	private Long fullDiscountId;
	
	public Long getFullDiscountId() {
		return fullDiscountId;
	}

	public void setFullDiscountId(Long fullDiscountId) {
		this.fullDiscountId = fullDiscountId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
	
	
}
