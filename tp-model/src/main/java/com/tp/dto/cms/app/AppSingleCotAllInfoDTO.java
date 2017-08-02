package com.tp.dto.cms.app;

import java.io.Serializable;
import java.util.List;

/**
 * APP的模板实体类
 * @author szy
 *
 */
public class AppSingleCotAllInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** 折扣 **/
	private String discount;
	
	/** 剩余时间 **/
	private String surplusTime;
	
	/** 开始时间 **/
	private Long startTime;
	
	/** 结束时间 **/
	private Long endTime;
	
	/** 专场图片 **/
	private List<String> imageurl = null;
	
	/** 默认分类 **/
	private String defclassifyid;
	
	/** 优惠价 **/
	private Double price;
	
	/** 原价 **/
	private Double oldprice;
	
	/** 已海淘数 **/
	private Integer unk;
	
	/** 促销主题 **/
	private String salesName;
	
	/** 商品名称 **/
	private String goodsName;
	
	/** 分类id **/
	private List<Long> classifylist;
	
	/** 品牌id **/
	private List<Long> brandlist;
	
	/** 商品详情detail **/
	private String detail;
	
	/** 商品规格specs **/
	private String specs;
	
	/** 库存数量count **/
	private Long count;
	
	/** 详情图片imagethreeurl **/
	private String imagethreeurl;
	
	/** 供应商supplier **/
	private Long supplier;
	
	/** 专场id(specialid) **/
	private Long specialid;
	
	/** 专场名称(specialName) **/
	private String specialName;
	
	/** 商品id(productid) **/
	private Long productid;
	
	/** sku(sku编码) **/
	private String sku;
	
	/** 卖点 **/
	private String topicPoint;
	
	private Integer status;

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

	public List<String> getImageurl() {
		return imageurl;
	}

	public void setImageurl(List<String> imageurl) {
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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getOldprice() {
		return oldprice;
	}

	public void setOldprice(Double oldprice) {
		this.oldprice = oldprice;
	}

	public Integer getUnk() {
		return unk;
	}

	public void setUnk(Integer unk) {
		this.unk = unk;
	}

	public String getSalesName() {
		return salesName;
	}

	public void setSalesName(String salesName) {
		this.salesName = salesName;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getSpecs() {
		return specs;
	}

	public void setSpecs(String specs) {
		this.specs = specs;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getImagethreeurl() {
		return imagethreeurl;
	}

	public void setImagethreeurl(String imagethreeurl) {
		this.imagethreeurl = imagethreeurl;
	}

	public Long getSupplier() {
		return supplier;
	}

	public void setSupplier(Long supplier) {
		this.supplier = supplier;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getSpecialName() {
		return specialName;
	}

	public void setSpecialName(String specialName) {
		this.specialName = specialName;
	}

	public String getTopicPoint() {
		return topicPoint;
	}

	public void setTopicPoint(String topicPoint) {
		this.topicPoint = topicPoint;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}


}
