package com.tp.dto.cms.app;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tp.dto.cms.temple.Products;

/**
 * APP的模板实体类
 * @author szy
 *
 */
public class AppSingleAllInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean isSingle;
	/** 折扣 **/
	private String discount;
	
	/** 剩余时间 **/
	private String surplusTime;
	
	/** 开始时间 **/
	private Long startTime;
	
	/** 结束时间 **/
	private Long endTime;
	
	/** 开始时间 **/
	private Date startTimeDate;
	
	/** 结束时间 **/
	private Date endTimeDate;

	
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
	/** 专场图片:切换后image,新 **/
	private String mobileImage;
	
	
	///////////////////////////////////////////////////////////
	/** 是否已关注:0表示已关注，1标识未关注 **/
	private boolean attentionStatus;
	
	/** text **/
	private String text;
	
	/** 持续时间0-长期有效 1-固定期限 */
	private Integer lastingType;
	/** 国家图片地址 **/
	private String countryImageUrl;
	
	/** 国家名称 **/
	private String countryName;
	
	/** 渠道名称 **/
	private String channelName;
	
	/** 预览活动商品列表 **/
	private List<Products> topicItemList;
	

	/** 店铺显示方式  分销店铺专用**/
	private Integer shopShowMode;
	
	/** 佣金  分销店铺专用**/
	private Double commission;

	private String shopName;

	private String shopLogo;


	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopLogo() {
		return shopLogo;
	}

	public void setShopLogo(String shopLogo) {
		this.shopLogo = shopLogo;
	}

	Long itemid;
	

	Long bondedArea;
	
	Long countryId;
	

	
	
	
	
	public Date getStartTimeDate() {
		return startTimeDate;
	}

	public void setStartTimeDate(Date startTimeDate) {
		this.startTimeDate = startTimeDate;
	}

	public Date getEndTimeDate() {
		return endTimeDate;
	}

	public void setEndTimeDate(Date endTimeDate) {
		this.endTimeDate = endTimeDate;
	}

	public Long getItemid() {
		return itemid;
	}

	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}

	public Long getBondedArea() {
		return bondedArea;
	}

	public void setBondedArea(Long bondedArea) {
		this.bondedArea = bondedArea;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public Double getCommission() {
		return commission;
	}

	public void setCommission(Double commission) {
		this.commission = commission;
	}

	public Integer getShopShowMode() {
		return shopShowMode;
	}

	public void setShopShowMode(Integer shopShowMode) {
		this.shopShowMode = shopShowMode;
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

	public Integer getLastingType() {
		return lastingType;
	}

	public void setLastingType(Integer lastingType) {
		this.lastingType = lastingType;
	}

	public boolean isSingle() {
		return isSingle;
	}

	public void setSingle(boolean isSingle) {
		this.isSingle = isSingle;
	}

	public String getMobileImage() {
		return mobileImage;
	}

	public void setMobileImage(String mobileImage) {
		this.mobileImage = mobileImage;
	}

	public String getCountryImageUrl() {
		return countryImageUrl;
	}

	public void setCountryImageUrl(String countryImageUrl) {
		this.countryImageUrl = countryImageUrl;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public List<Products> getTopicItemList() {
		return topicItemList;
	}

	public void setTopicItemList(List<Products> topicItemList) {
		this.topicItemList = topicItemList;
	}
}
