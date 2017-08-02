package com.tp.dto.cms.app;

import java.io.Serializable;
import java.util.List;

/**
 * APP的模板实体类
 * @author szy
 *
 */
public class AppSingleInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** 专场名称 **/
	private String name;
	
	/** 折扣 **/
	private String discount;
	
	/** 剩余时间 **/
	private String surplusTime;
	
	/** 开始时间 **/
	private Long startTime;
	
	/** 结束时间 **/
	private Long endTime;
	
	/** 专场图片 **/
	private String imageurl;
	
	/** 专场图片:切换后image,新 **/
	private String mobileImage;
	
	/** 专场id **/
	private Long specialid;
	
	/** 专场id **/
	private Long productid;
	
	/** sku **/
	private String sku;
	
	/** 默认分类 **/
	private String defclassifyid;
	
	/** 分类id **/
	private List<Long> classifylist;
	
	/** 品牌id **/
	private List<Long> brandlist;
	
	/** 是否已关注:0表示已关注，1标识未关注 **/
	private boolean attentionStatus;
	
	/** text **/
	private String text;
	
	/** 活动状态 **/
	private Integer status;
	
	/** 持续时间0-长期有效 1-固定期限 */
	private Integer lastingType;
	
	private String searchKey;
	private Long brandId;
	private Long categoryId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getSurplusTime() {
		return surplusTime;
	}

	public void setSurplusTime(String surplusTime) {
		this.surplusTime = surplusTime;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public Long getSpecialid() {
		return specialid;
	}

	public void setSpecialid(Long specialid) {
		this.specialid = specialid;
	}

	public String getDefclassifyid() {
		return defclassifyid;
	}

	public void setDefclassifyid(String defclassifyid) {
		this.defclassifyid = defclassifyid;
	}

	public List<Long> getClassifylist() {
		return classifylist;
	}

	public void setClassifylist(List<Long> classifylist) {
		this.classifylist = classifylist;
	}

	public List<Long> getBrandlist() {
		return brandlist;
	}

	public void setBrandlist(List<Long> brandlist) {
		this.brandlist = brandlist;
	}

	public Long getProductid() {
		return productid;
	}

	public void setProductid(Long productid) {
		this.productid = productid;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public boolean isAttentionStatus() {
		return attentionStatus;
	}

	public void setAttentionStatus(boolean attentionStatus) {
		this.attentionStatus = attentionStatus;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMobileImage() {
		return mobileImage;
	}

	public void setMobileImage(String mobileImage) {
		this.mobileImage = mobileImage;
	}

	public void setLastingType(Integer lastingType) {
		this.lastingType = lastingType;
	}
	
	public Integer getLastingType() {
		return lastingType;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
}
