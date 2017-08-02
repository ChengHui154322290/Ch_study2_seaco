package com.tp.dto.cms.app;

import java.io.Serializable;

/**
 * APP的图片实体类
 * @author szy
 * @param <T>
 *
 */
public class AppAdvertiseInfoDTO<T> implements Serializable/*,JSONAware*/ {

	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private String imageurl;
	 
	private String linkurl;
	
	private Long productid;
	
	private Integer time;
	
	private String type;
	
	private String sku;
	
	private T info;

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getLinkurl() {
		return linkurl;
	}

	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}

	public Long getProductid() {
		return productid;
	}

	public void setProductid(Long productid) {
		this.productid = productid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public T getInfo() {
		return info;
	}

	public void setInfo(T info) {
		this.info = info;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	
	


}
