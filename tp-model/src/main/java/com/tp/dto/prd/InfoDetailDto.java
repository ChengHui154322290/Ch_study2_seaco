package com.tp.dto.prd;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tp.model.prd.ItemAttribute;
import com.tp.model.prd.ItemInfo;
import com.tp.model.prd.ItemPictures;
import com.tp.result.bse.SpecGroupResult;

/***
 * 商品详情信息Dto
 * @author szy
 *
 */
public class InfoDetailDto  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4378976516944006777L;
	/**
	 */
	/**商品Id**/
	private Long id;
	/***sku ID***/
	private Long skuId;
	/**sku**/
	private String sku;
	/***spu code*/
	private String spu;
	/**商品中文名称**/
	private String cnName= "";
	/**商品英文名称**/
	private String enName = ""; 
	/**商品原价**/
	private Double basicPrice ;
	/**商品西客商城价**/
	private Double xgPrice ;
	/**商品主标题**/
	private String mainTitle;
	/***商品子标题**/
	private String subTitle ;
	/**商品对应属性**/
	private List<ItemAttribute> itemAttribute;
	/**图片信息**/
	private List<ItemPictures> itemPicturesList;
	/**定时上架，时间需要放到内存中去的**/
	private Date  effectStartDate ; 
	private Date  effectEndDate ;
	/**图片数组**/
	private String listPictures;
	/**sku数组**/
	private String listSkus;
	/**sku对应的颜色和尺寸信息**/
	private String skuInfos;
	/**折扣**/
	private Double discount;//折扣
	/**商品倒计时字符串 以 “，” 分割***/
	private String  cutDownTime;
	/***供应商ID**/
	private Long spId;
	/**itemId***/
	private Long itemId;
	/**detailId****/
	private Long detailId;
	/***prdid***/
	private String prdId;
	/***itemType***/
	private String itemType;//商品类型 1-正常商品，2-服务商品，3-二手商品,4-报废商品 默认1',
	/**商品详情描述**/
	private String detailDesc;
	/**商品属性组信息**/
	private String listSpecGroup;
	/**sku 对应的促销活动类型**/
	private String activityType;
	/**判断活动是否过期**/
	private String isOverTime;
	/**活动是否开始***/
	private String isNotStart;
	/**供应商名称**/
	private String spName = "";
	/**是否还有库存 1 没有库存**/
	private String outOfStock= "";
	/**活动名称**/
	private String topicName="";
	/**商品上下架状态 0 下架 1上架**/
	private String status;
	/***商品活动 离开始多少毫秒***/
	private Long beforeStartTime;
	/**商品还没开始活动倒计时字符串 以 “，” 分割***/
	private String  noStartCutDown;
	
	private Integer quantity;
	
	private SkuDto skuDto;
	
	/**手机APP 规格组**/
	private List<SpecGroupResult> specGroupList;
	
	/**手机APP使用  上架时间（UTC时间） 精确到秒，例子："10000000"**/
	private  Long storetime;
	/**手机APP使用  剩余时间（UTC时间） 精确到秒，例子："10000000"**/
	private  Long  endtime;
	
	/***手机APP 使用  分类ID***/
	private Long categoryId;
	
	/***手机APP 使用  brandID***/
	private Long brandId;
	
	/**错误**/
	private String isError;
	
	/**没有折扣***/
	private String noDiscount;
	
	/***活动类型***/
	private  String topicType;
	
	/**商品infodto***/
	private ItemInfo itemInfo;
	
	private List<Long> existDetailIds;
	
	/**品牌名称**/
	private String brandName;
	
	/***热卖 和即将卖的商品**/
	private  String listOnSaleAndWillSale;
	
	/**商品限购数量**/
	private Integer limitCount;  
	
	
	/**国家ID***/
	private Long countryId;
	
	/***国家图片**/
	private String countryImagePath;
	
	/***配送方式**/
	private String sendType;
	
	/**适用年龄**/
	private Long applyAgeId;
	
	/**是否海淘商品**/
	private String isHT;
	
	/** 关税税率 */
	private Long tarrifRate;
	
	/**税率 */
	private Double taxRate;
	/**单品税费*/
	private Double rateFee;
	
	private Integer rateType;
	/**税种名*/
	private String rateName;
	
	/**海淘商品产地***/
	private String countryName;
	
	/**飞入图片动画***/
	private String flyImage;
	
	/***立即购买按钮标示***/
	private String purchasePage;
	
	/** 已售数量 **/
	private int salesCount;
	
	/** 活动商品标签 **/
	private String itemTags;
	
	/** 分销佣金比例 **/
	private String commisionRate;
	
	/** 分销佣金 **/
	private Double commision;

	private Integer salesPattern;
	
	public Double getCommision() {
		return commision;
	}

	public void setCommision(Double commision) {
		this.commision = commision;
	}

	public Integer getSalesPattern() {
		return salesPattern;
	}

	public void setSalesPattern(Integer salesPattern) {
		this.salesPattern = salesPattern;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getSpu() {
		return spu;
	}

	public void setSpu(String spu) {
		this.spu = spu;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public Double getBasicPrice() {
		return basicPrice;
	}

	public void setBasicPrice(Double basicPrice) {
		this.basicPrice = basicPrice;
	}

	public String getMainTitle() {
		return mainTitle;
	}

	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public List<ItemAttribute> getItemAttribute() {
		return itemAttribute;
	}

	public void setItemAttribute(List<ItemAttribute> itemAttribute) {
		this.itemAttribute = itemAttribute;
	}

	public List<ItemPictures> getItemPicturesList() {
		return itemPicturesList;
	}

	public void setItemPicturesList(List<ItemPictures> itemPicturesList) {
		this.itemPicturesList = itemPicturesList;
	}

	public Date getEffectStartDate() {
		return effectStartDate;
	}

	public void setEffectStartDate(Date effectStartDate) {
		this.effectStartDate = effectStartDate;
	}

	public Date getEffectEndDate() {
		return effectEndDate;
	}

	public void setEffectEndDate(Date effectEndDate) {
		this.effectEndDate = effectEndDate;
	}

	public String getListPictures() {
		return listPictures;
	}

	public void setListPictures(String listPictures) {
		this.listPictures = listPictures;
	}

	public String getListSkus() {
		return listSkus;
	}

	public void setListSkus(String listSkus) {
		this.listSkus = listSkus;
	}

	public String getSkuInfos() {
		return skuInfos;
	}

	public void setSkuInfos(String skuInfos) {
		this.skuInfos = skuInfos;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getCutDownTime() {
		return cutDownTime;
	}

	public void setCutDownTime(String cutDownTime) {
		this.cutDownTime = cutDownTime;
	}

	public Long getSpId() {
		return spId;
	}

	public void setSpId(Long spId) {
		this.spId = spId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public String getPrdId() {
		return prdId;
	}

	public void setPrdId(String prdId) {
		this.prdId = prdId;
	}

	public String getDetailDesc() {
		return detailDesc;
	}

	public void setDetailDesc(String detailDesc) {
		this.detailDesc = detailDesc;
	}

	public String getListSpecGroup() {
		return listSpecGroup;
	}

	public void setListSpecGroup(String listSpecGroup) {
		this.listSpecGroup = listSpecGroup;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getIsOverTime() {
		return isOverTime;
	}

	public void setIsOverTime(String isOverTime) {
		this.isOverTime = isOverTime;
	}

	public String getIsNotStart() {
		return isNotStart;
	}

	public void setIsNotStart(String isNotStart) {
		this.isNotStart = isNotStart;
	}

	public String getSpName() {
		return spName;
	}

	public void setSpName(String spName) {
		this.spName = spName;
	}

	public String getOutOfStock() {
		return outOfStock;
	}

	public void setOutOfStock(String outOfStock) {
		this.outOfStock = outOfStock;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getBeforeStartTime() {
		return beforeStartTime;
	}

	public void setBeforeStartTime(Long beforeStartTime) {
		this.beforeStartTime = beforeStartTime;
	}

	public String getNoStartCutDown() {
		return noStartCutDown;
	}

	public void setNoStartCutDown(String noStartCutDown) {
		this.noStartCutDown = noStartCutDown;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public SkuDto getSkuDto() {
		return skuDto;
	}

	public void setSkuDto(SkuDto skuDto) {
		this.skuDto = skuDto;
	}

	public List<SpecGroupResult> getSpecGroupList() {
		return specGroupList;
	}

	public void setSpecGroupList(List<SpecGroupResult> specGroupList) {
		this.specGroupList = specGroupList;
	}

	public Long getStoretime() {
		return storetime;
	}

	public void setStoretime(Long storetime) {
		this.storetime = storetime;
	}

	public Long getEndtime() {
		return endtime;
	}

	public void setEndtime(Long endtime) {
		this.endtime = endtime;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getIsError() {
		return isError;
	}

	public void setIsError(String isError) {
		this.isError = isError;
	}

	public String getNoDiscount() {
		return noDiscount;
	}

	public void setNoDiscount(String noDiscount) {
		this.noDiscount = noDiscount;
	}

	public String getTopicType() {
		return topicType;
	}

	public void setTopicType(String topicType) {
		this.topicType = topicType;
	}

	public ItemInfo getItemInfo() {
		return itemInfo;
	}

	public void setItemInfo(ItemInfo itemInfo) {
		this.itemInfo = itemInfo;
	}

	public List<Long> getExistDetailIds() {
		return existDetailIds;
	}

	public void setExistDetailIds(List<Long> existDetailIds) {
		this.existDetailIds = existDetailIds;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getListOnSaleAndWillSale() {
		return listOnSaleAndWillSale;
	}

	public void setListOnSaleAndWillSale(String listOnSaleAndWillSale) {
		this.listOnSaleAndWillSale = listOnSaleAndWillSale;
	}

	public Integer getLimitCount() {
		return limitCount;
	}

	public void setLimitCount(Integer limitCount) {
		this.limitCount = limitCount;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public String getCountryImagePath() {
		return countryImagePath;
	}

	public void setCountryImagePath(String countryImagePath) {
		this.countryImagePath = countryImagePath;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public Long getApplyAgeId() {
		return applyAgeId;
	}

	public void setApplyAgeId(Long applyAgeId) {
		this.applyAgeId = applyAgeId;
	}

	public String getIsHT() {
		return isHT;
	}

	public void setIsHT(String isHT) {
		this.isHT = isHT;
	}

	public Long getTarrifRate() {
		return tarrifRate;
	}

	public void setTarrifRate(Long tarrifRate) {
		this.tarrifRate = tarrifRate;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getFlyImage() {
		return flyImage;
	}

	public void setFlyImage(String flyImage) {
		this.flyImage = flyImage;
	}

	public String getPurchasePage() {
		return purchasePage;
	}

	public void setPurchasePage(String purchasePage) {
		this.purchasePage = purchasePage;
	}

	public Double getXgPrice() {
		return xgPrice;
	}

	public void setXgPrice(Double xgPrice) {
		this.xgPrice = xgPrice;
	}

	public String getItemTags() {
		return itemTags;
	}

	public void setItemTags(String itemTags) {
		this.itemTags = itemTags;
	}

	/**
	 * @return the salesCount
	 */
	public int getSalesCount() {
		return salesCount;
	}

	/**
	 * @param salesCount the salesCount to set
	 */
	public void setSalesCount(int salesCount) {
		this.salesCount = salesCount;
	}

	public String getCommisionRate() {
		return commisionRate;
	}

	public void setCommisionRate(String commisionRate) {
		this.commisionRate = commisionRate;
	}

	public Double getRateFee() {
		return rateFee;
	}

	public void setRateFee(Double rateFee) {
		this.rateFee = rateFee;
	}

	public String getRateName() {
		if(rateName==null){
			rateName="";
		}
		return rateName;
	}

	public void setRateName(String rateName) {
		this.rateName = rateName;
	}

	public Integer getRateType() {
		return rateType;
	}

	public void setRateType(Integer rateType) {
		this.rateType = rateType;
	}

	public Double getTaxRate() {
		if(taxRate==null){
			return (double) 0;
		}
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	
	
}
