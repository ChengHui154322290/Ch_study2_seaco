package com.tp.dto.cms.app;

import java.io.Serializable;

/**
 * APP的图片实体类
 * @author szy
 * @param <T>
 *
 */
public class AppAdvertDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;//图片名称
	
	private String imageurl;//图片路径
	 
	private String linkurl;//链接
	
	private Long productid;//商品id
	
	private Integer time;//启动时间,单位是秒
	
	private String actType;//活动类型
	
	private String sku;//sku
	
	private Long specialid;//活动id
	
	private Integer sort;//排序
	
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

	public String getActType() {
		return actType;
	}

	public void setActType(String actType) {
		this.actType = actType;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Long getSpecialid() {
		return specialid;
	}

	public void setSpecialid(Long specialid) {
		this.specialid = specialid;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

}
