package com.tp.model.mmp;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 促销商品 for dss
  */
public class TopicItemDss extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579112L;

	/** 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/**专题id 数据类型bigint(18)*/
	private Long topicId;
	
	/**商品id 数据类型bigint(18)*/
	private Long itemId;
	
	/**排序 数据类型int(4)*/
	private Integer sortIndex;
	
	/**商家 数据类型bigint(11)*/
	private Long supplierId;
	
	/**报价单id 数据类型bigint(11)*/
	private Long quotationId;
	
	/** 数据类型varchar(20)*/
	private String sku;
	
	/**商品名称 数据类型varchar(255)*/
	private String name;
	
	/**是否为测试商品 0-否 1-是 数据类型int(1)*/
	private Integer isTest;
	
	/**商品颜色 数据类型varchar(20)*/
	private String itemColor;
	
	/**商品尺寸 数据类型varchar(20)*/
	private String itemSize;
	
	/**活动图片 数据类型varchar(255)*/
	private String topicImage;
	
	/**活动价格 数据类型double(11,2)*/
	private Double topicPrice;
	
	/**限购数量 数据类型int(11)*/
	private Integer limitAmount;
	
	/**本专题限购总数 数据类型int(11)*/
	private Integer limitTotal;
	
	/**本专题剩余库存数量 数据类型int(11)*/
	private Integer stockAmount;
	
	/**销售数量 数据类型int(11)*/
	private Integer saledAmount;
	
	/**仓库名 数据类型varchar(50)*/
	private String stockLocation;
	
	/**备注 数据类型varchar(255)*/
	private String remark;
	
	/**删除状态 0-正常 1-删除 数据类型int(1)*/
	private Integer deletion;
	
	/**仓库地址Id 数据类型bigint(18)*/
	private Long stockLocationId;
	
	/**商品规格参数 数据类型varchar(255)*/
	private String itemSpec;
	
	/**商品条码 数据类型varchar(100)*/
	private String barCode;
	
	/**商家名称 数据类型varchar(100)*/
	private String supplierName;
	
	/**图片位图大小 数据类型int(4)*/
	private Integer pictureSize;
	
	/**大分类id 数据类型bigint(18)*/
	private Long largeCateoryId;
	
	/**中分类id 数据类型bigint(18)*/
	private Long middleCategoryId;
	
	/**小分类id 数据类型bigint(18)*/
	private Long categoryId;
	
	/**品牌id 数据类型bigint(18)*/
	private Long brandId;
	
	/**1 ：有库存 0： 没库存 数据类型int(1)*/
	private Integer stock;
	
	/**商品原价 数据类型double(11,2)*/
	private Double salePrice;
	
	/**商品spu 数据类型varchar(14)*/
	private String spu;
	
	/**商品录入来源 0 - 手工录入 1 - 黏贴录入 2 - 拷贝 数据类型tinyint(4)*/
	private Integer inputSource;
	
	/**商品锁定状态 0 - 未锁定 1 - 锁定 数据类型tinyint(4)*/
	private Integer lockStatus;
	
	/**冗余通关渠道 数据类型bigint(20)*/
	private Long bondedArea;
	
	/**冗余仓库类型 数据类型tinyint(4)*/
	private Integer whType;
	
	/**海淘商品产地国家ID 数据类型bigint(20)*/
	private Long countryId;
	
	/**海淘商品产地国家名称 数据类型varchar(100)*/
	private String countryName;
	
	/**购买方式 1-普通 2-立即购买 数据类型tinyint(4)*/
	private Integer purchaseMethod;
	
	/**是否为爆款商品 0-否 1-是 数据类型tinyint(4)*/
	private Integer isHot;
	
	/** 数据类型varchar(50)*/
	private String prdid;
	
	/**适用年龄段 数据类型bigint(20)*/
	private Long applyAgeId;
	
	/**上架时间 数据类型datetime*/
	private Date listingTime;
	
	/**商品detail序号 数据类型bigint(20)*/
	private Long detailId;
	
	/**商品状态 0 - 未上架 1 - 已上架 2 - 作废 参考itemStatus 数据类型int(1)*/
	private Integer itemStatus;
	
	/**爆款标题(自定义) 数据类型varchar(255)*/
	private String hotTitle;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	/**修改人 数据类型varchar(32)*/
	private String updateUser;
	/** 活动商品标签 **/
	private String itemTags;
		
	@Virtual
	private Topic topic;
	
	@Virtual
	private Long promoterId;

	// Topic for dss
	private String introMobile; 
	private String imageMobile;
	private Date startTime;
	private Date endTime;
	private Integer topicStatus;
	

	
	
	public String getIntroMobile() {
		return introMobile;
	}
	public void setIntroMobile(String introMobile) {
		this.introMobile = introMobile;
	}
	public String getImageMobile() {
		return imageMobile;
	}
	public void setImageMobile(String imageMobile) {
		this.imageMobile = imageMobile;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getTopicStatus() {
		return topicStatus;
	}
	public void setTopicStatus(Integer topicStatus) {
		this.topicStatus = topicStatus;
	}
	public Long getPromoterId() {
		return promoterId;
	}
	public void setPromoterId(Long promoterId) {
		this.promoterId = promoterId;
	}
	public Long getId(){
		return id;
	}
	public Long getTopicId(){
		return topicId;
	}
	public Long getItemId(){
		return itemId;
	}
	public Integer getSortIndex(){
		return sortIndex;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public Long getQuotationId(){
		return quotationId;
	}
	public String getSku(){
		return sku;
	}
	public String getName(){
		return name;
	}
	public Integer getIsTest(){
		return isTest;
	}
	public String getItemColor(){
		return itemColor;
	}
	public String getItemSize(){
		return itemSize;
	}
	public String getTopicImage(){
		return topicImage;
	}
	public Double getTopicPrice(){
		return topicPrice;
	}
	public Integer getLimitAmount(){
		return limitAmount;
	}
	public Integer getLimitTotal(){
		return limitTotal;
	}
	public Integer getStockAmount(){
		return stockAmount;
	}
	public Integer getSaledAmount(){
		return saledAmount;
	}
	public String getStockLocation(){
		return stockLocation;
	}
	public String getRemark(){
		return remark;
	}
	public Integer getDeletion(){
		return deletion;
	}
	public Long getStockLocationId(){
		return stockLocationId;
	}
	public String getItemSpec(){
		return itemSpec;
	}
	public String getBarCode(){
		return barCode;
	}
	public String getSupplierName(){
		return supplierName;
	}
	public Integer getPictureSize(){
		return pictureSize;
	}
	public Long getLargeCateoryId(){
		return largeCateoryId;
	}
	public Long getMiddleCategoryId(){
		return middleCategoryId;
	}
	public Long getCategoryId(){
		return categoryId;
	}
	public Long getBrandId(){
		return brandId;
	}
	public Integer getStock(){
		return stock;
	}
	public Double getSalePrice(){
		return salePrice;
	}
	public String getSpu(){
		return spu;
	}
	public Integer getInputSource(){
		return inputSource;
	}
	public Integer getLockStatus(){
		return lockStatus;
	}
	public Long getBondedArea(){
		return bondedArea;
	}
	public Integer getWhType(){
		return whType;
	}
	public Long getCountryId(){
		return countryId;
	}
	public String getCountryName(){
		return countryName;
	}
	public Integer getPurchaseMethod(){
		return purchaseMethod;
	}
	public Integer getIsHot(){
		return isHot;
	}
	public String getPrdid(){
		return prdid;
	}
	public Long getApplyAgeId(){
		return applyAgeId;
	}
	public Date getListingTime(){
		return listingTime;
	}
	public Long getDetailId(){
		return detailId;
	}
	public Integer getItemStatus(){
		return itemStatus;
	}
	public String getHotTitle(){
		return hotTitle;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setTopicId(Long topicId){
		this.topicId=topicId;
	}
	public void setItemId(Long itemId){
		this.itemId=itemId;
	}
	public void setSortIndex(Integer sortIndex){
		this.sortIndex=sortIndex;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setQuotationId(Long quotationId){
		this.quotationId=quotationId;
	}
	public void setSku(String sku){
		this.sku=sku;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setIsTest(Integer isTest){
		this.isTest=isTest;
	}
	public void setItemColor(String itemColor){
		this.itemColor=itemColor;
	}
	public void setItemSize(String itemSize){
		this.itemSize=itemSize;
	}
	public void setTopicImage(String topicImage){
		this.topicImage=topicImage;
	}
	public void setTopicPrice(Double topicPrice){
		this.topicPrice=topicPrice;
	}
	public void setLimitAmount(Integer limitAmount){
		this.limitAmount=limitAmount;
	}
	public void setLimitTotal(Integer limitTotal){
		this.limitTotal=limitTotal;
	}
	public void setStockAmount(Integer stockAmount){
		this.stockAmount=stockAmount;
	}
	public void setSaledAmount(Integer saledAmount){
		this.saledAmount=saledAmount;
	}
	public void setStockLocation(String stockLocation){
		this.stockLocation=stockLocation;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setDeletion(Integer deletion){
		this.deletion=deletion;
	}
	public void setStockLocationId(Long stockLocationId){
		this.stockLocationId=stockLocationId;
	}
	public void setItemSpec(String itemSpec){
		this.itemSpec=itemSpec;
	}
	public void setBarCode(String barCode){
		this.barCode=barCode;
	}
	public void setSupplierName(String supplierName){
		this.supplierName=supplierName;
	}
	public void setPictureSize(Integer pictureSize){
		this.pictureSize=pictureSize;
	}
	public void setLargeCateoryId(Long largeCateoryId){
		this.largeCateoryId=largeCateoryId;
	}
	public void setMiddleCategoryId(Long middleCategoryId){
		this.middleCategoryId=middleCategoryId;
	}
	public void setCategoryId(Long categoryId){
		this.categoryId=categoryId;
	}
	public void setBrandId(Long brandId){
		this.brandId=brandId;
	}
	public void setStock(Integer stock){
		this.stock=stock;
	}
	public void setSalePrice(Double salePrice){
		this.salePrice=salePrice;
	}
	public void setSpu(String spu){
		this.spu=spu;
	}
	public void setInputSource(Integer inputSource){
		this.inputSource=inputSource;
	}
	public void setLockStatus(Integer lockStatus){
		this.lockStatus=lockStatus;
	}
	public void setBondedArea(Long bondedArea){
		this.bondedArea=bondedArea;
	}
	public void setWhType(Integer whType){
		this.whType=whType;
	}
	public void setCountryId(Long countryId){
		this.countryId=countryId;
	}
	public void setCountryName(String countryName){
		this.countryName=countryName;
	}
	public void setPurchaseMethod(Integer purchaseMethod){
		this.purchaseMethod=purchaseMethod;
	}
	public void setIsHot(Integer isHot){
		this.isHot=isHot;
	}
	public void setPrdid(String prdid){
		this.prdid=prdid;
	}
	public void setApplyAgeId(Long applyAgeId){
		this.applyAgeId=applyAgeId;
	}
	public void setListingTime(Date listingTime){
		this.listingTime=listingTime;
	}
	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}
	public void setItemStatus(Integer itemStatus){
		this.itemStatus=itemStatus;
	}
	public void setHotTitle(String hotTitle){
		this.hotTitle=hotTitle;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
	public Topic getTopic() {
		return topic;
	}
	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	public String getItemTags() {
		return itemTags;
	}
	public void setItemTags(String itemTags) {
		this.itemTags = itemTags;
	}
}
