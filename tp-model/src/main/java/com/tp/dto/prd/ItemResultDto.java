package com.tp.dto.prd;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tp.dto.prd.enums.ItemSaleTypeEnum;
import com.tp.dto.prd.enums.ItemStatusEnum;
import com.tp.dto.prd.enums.ItemTypesEnum;
import com.tp.model.prd.ItemAttribute;
import com.tp.model.prd.ItemDetailSpec;
import com.tp.model.prd.ItemSku;
/**
 * 
 * <pre>
 * 		提供商品实体给客户端调用
 * 		商品的基本属性给购物车调用
 * 
 * 
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class ItemResultDto implements Serializable {

	private static final long serialVersionUID = 5547057661319081750L;
	/**商品ID*/
	private Long itemId; 
	/**sku ID*/
	private Long skuId;
	/** prdid纬度 detailid */
	private Long detailId;
	/** prdid编号 */
	private String prdid;
	/**sku*/
	private String sku;
	/**spu*/
	private String spu;
	/**商品原价 （市场价）*/
	private Double basicPrice ;
	/**商品促销价*/
	private Double topicPrice;
	/**商品介绍*/
	private String desc; 
	/**商品主标题*/
	private String mainTitle;
	/**商品子标题 卖点*/
	private String subTitle ;
	/**商品单位*/
	private Long unitId;
	/**关联的品牌*/
	private Long brandId;
	/**大类ID*/
	private Long largeId;
	/**大类名称*/
	private String largeName;
	/**中类ID*/
	private Long mediumId;
	/**中类名称*/
	private String mediumName;
	/**小类ID*/
	private Long smallId;
	/**小类Code*/
	private String categoryCode;
	/**小类名称*/
	private String categoryName;
	
	/**商品类型：1-正常商品，2-服务商品，3-二手商品,默认1*/
	private Integer itemType;
	/**供应商或者商户ID */
	private Long supplierId;
	/**供应商商品编码 */
	private String supplierCode;
	/**供应商或商户名称 */
	private String supplierName;
	/**供应商类别 即saleType*/
	private Integer saleType;
	/**商品对应属性*/
	private List<ItemAttribute> itemAttribute;
	/**创建日期 */
	private Date createTime;
	/**修改时间*/
	private Date modifyTime;
	/**购物车里面的图片*/
	private String imageUrl;
	/**是否上架 0-未上架 1-上架 2-作废 默认为0 */
	private Integer status;
	/**是否海淘商品,1-否，2-是，默认1 */
	private Integer wavesSign ;
	/**条码*/
	private String barcode;
	/***/
	private List<ItemSku> skuList;
	/** 商品规格信息 */
	private List<ItemDetailSpec> itemDetailSpecList;

	/** 运费模板 */
	private Long freightTemplateId;
	
	/** 净重 单位g */
	private Double weightNet;
	
	/** 毛重 单位g */
	private Double weight;
	
	/** 关税税率Id */
	private Long tarrifRateId;
	/** 关税税率Value */
	private Double tarrifRateValue;
	
	/** 规格 */
	private String specifications;
	/** 箱规 */
	private String cartonSpec;
	/**商品名称 */
	private String itemTitle;
	
	
	/** 适用年龄**/
	private Long applyAgeId;
	
	/***海淘商品产地（国家ID）**/
	private Long countryId;
	
	/**海淘商品配送方式***/
	private String sendType;
	
	/**退货天数**/
	private Integer returnDays;
	
	/***单位名称***/
	private String unitName;
	
	
	/**存储在仓库中的名称*/
	private String storageTitle;
	
	/**spu_name*/
	private String spuName;
	
	/**品牌名称**/
	private String brandName;
	
	/**增值税率*/
	private Long addedValueRateId;
	/**消费税率*/
	private Long exciseRateId;
	/**关税率*/
	private Long customsRateId;
	/**增值税率*/
	private Double addedValueRate;
	/**消费税率*/
	private Double exciseRate;
	/**关税率*/
	private Double customsRate;
	/**分销提佣比率 数据类型double(5,2)*/
	private Double commisionRate;
	
	//新增
	private Integer unitQuantity;
	private Integer wrapQuantity;
	
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	public Double getBasicPrice() {
		return basicPrice;
	}
	public void setBasicPrice(Double basicPrice) {
		this.basicPrice = basicPrice;
	}
	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
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
	
	public String getPrdid() {
		return prdid;
	}
	public void setPrdid(String prdid) {
		this.prdid = prdid;
	}
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public Long getLargeId() {
		return largeId;
	}
	public void setLargeId(Long largeId) {
		this.largeId = largeId;
	}
	public Long getMediumId() {
		return mediumId;
	}
	public void setMediumId(Long mediumId) {
		this.mediumId = mediumId;
	}
	public Long getSmallId() {
		return smallId;
	}
	public void setSmallId(Long smallId) {
		this.smallId = smallId;
	}
	public Integer getItemType() {
		return itemType;
	}
	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}
	
	public String getItemTypeDesc(){
		ItemTypesEnum type = ItemTypesEnum.getByValue(itemType);
		if(null==type){
			return null;
		}
		return type.getKey();
	}
	
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	
	public String getSaleTypeDesc(){
		ItemSaleTypeEnum type = ItemSaleTypeEnum.getByValue(saleType);
		if(null==type){
			return null;
		}
		return type.getKey();
	}
	
	public List<ItemAttribute> getItemAttribute() {
		return itemAttribute;
	}
	public void setItemAttribute(List<ItemAttribute> itemAttribute) {
		this.itemAttribute = itemAttribute;
	}
	public List<ItemSku> getSkuList() {
		return skuList;
	}
	public void setSkuList(List<ItemSku> skuList) {
		this.skuList = skuList;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	//图片?
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getStatusDesc(){
		ItemStatusEnum type = ItemStatusEnum.getByValue(status);
		if(null==type){
			return null;
		}
		return type.getKey();
	}
	
	public Integer getWavesSign() {
		return wavesSign;
	}
	public void setWavesSign(Integer wavesSign) {
		this.wavesSign = wavesSign;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public Long getDetailId() {
		return detailId;
	}
	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}
	
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	/**
	 * Getter method for property <tt>freightTemplateId</tt>.
	 * 
	 * @return property value of freightTemplateId
	 */
	public Long getFreightTemplateId() {
		return freightTemplateId;
	}
	/**
	 * Setter method for property <tt>freightTemplateId</tt>.
	 * 
	 * @param freightTemplateId value to be assigned to property freightTemplateId
	 */
	public void setFreightTemplateId(Long freightTemplateId) {
		this.freightTemplateId = freightTemplateId;
	}
	
	public Integer getSaleType() {
		return saleType;
	}
	public void setSaleType(Integer saleType) {
		this.saleType = saleType;
	}
	/**
	 * Getter method for property <tt>weightNet</tt>.
	 * 
	 * @return property value of weightNet
	 */
	public Double getWeightNet() {
		return weightNet;
	}
	/**
	 * Setter method for property <tt>weightNet</tt>.
	 * 
	 * @param weightNet value to be assigned to property weightNet
	 */
	public void setWeightNet(Double weightNet) {
		this.weightNet = weightNet;
	}
	
	/**
	 * Getter method for property <tt>tarrifRateId</tt>.
	 * 
	 * @return property value of tarrifRateId
	 */
	public Long getTarrifRateId() {
		return tarrifRateId;
	}
	/**
	 * Setter method for property <tt>tarrifRateId</tt>.
	 * 
	 * @param tarrifRateId value to be assigned to property tarrifRateId
	 */
	public void setTarrifRateId(Long tarrifRateId) {
		this.tarrifRateId = tarrifRateId;
	}
	/**
	 * Getter method for property <tt>tarrifRateValue</tt>.
	 * 
	 * @return property value of tarrifRateValue
	 */
	public Double getTarrifRateValue() {
		return tarrifRateValue;
	}
	/**
	 * Setter method for property <tt>tarrifRateValue</tt>.
	 * 
	 * @param tarrifRateValue value to be assigned to property tarrifRateValue
	 */
	public void setTarrifRateValue(Double tarrifRateValue) {
		this.tarrifRateValue = tarrifRateValue;
	}
	
	/**
	 * Getter method for property <tt>largeName</tt>.
	 * 
	 * @return property value of largeName
	 */
	public String getLargeName() {
		return largeName;
	}
	/**
	 * Setter method for property <tt>largeName</tt>.
	 * 
	 * @param largeName value to be assigned to property largeName
	 */
	public void setLargeName(String largeName) {
		this.largeName = largeName;
	}
	/**
	 * Getter method for property <tt>mediumName</tt>.
	 * 
	 * @return property value of mediumName
	 */
	public String getMediumName() {
		return mediumName;
	}
	/**
	 * Setter method for property <tt>mediumName</tt>.
	 * 
	 * @param mediumName value to be assigned to property mediumName
	 */
	public void setMediumName(String mediumName) {
		this.mediumName = mediumName;
	}
	
	
	/**
	 * Getter method for property <tt>specifications</tt>.
	 * 
	 * @return property value of specifications
	 */
	public String getSpecifications() {
		return specifications;
	}
	/**
	 * Setter method for property <tt>specifications</tt>.
	 * 
	 * @param specifications value to be assigned to property specifications
	 */
	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}
	/**
	 * Getter method for property <tt>cartonSpec</tt>.
	 * 
	 * @return property value of cartonSpec
	 */
	public String getCartonSpec() {
		return cartonSpec;
	}
	/**
	 * Setter method for property <tt>cartonSpec</tt>.
	 * 
	 * @param cartonSpec value to be assigned to property cartonSpec
	 */
	public void setCartonSpec(String cartonSpec) {
		this.cartonSpec = cartonSpec;
	}
	
	
	/**
	 * Getter method for property <tt>weight</tt>.
	 * 
	 * @return property value of weight
	 */
	public Double getWeight() {
		return weight;
	}
	/**
	 * Setter method for property <tt>weight</tt>.
	 * 
	 * @param weight value to be assigned to property weight
	 */
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	
	/**
	 * Getter method for property <tt>itemTitle</tt>.
	 * 
	 * @return property value of itemTitle
	 */
	public String getItemTitle() {
		return itemTitle;
	}
	/**
	 * Setter method for property <tt>itemTitle</tt>.
	 * 
	 * @param itemTitle value to be assigned to property itemTitle
	 */
	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}
	
	
	public Long getApplyAgeId() {
		return applyAgeId;
	}
	public void setApplyAgeId(Long applyAgeId) {
		this.applyAgeId = applyAgeId;
	}
	public Long getCountryId() {
		return countryId;
	}
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
	public String getSendType() {
		return sendType;
	}
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	
	public Integer getReturnDays() {
		return returnDays;
	}
	public void setReturnDays(Integer returnDays) {
		this.returnDays = returnDays;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	public String getStorageTitle() {
		return storageTitle;
	}
	public void setStorageTitle(String storageTitle) {
		this.storageTitle = storageTitle;
	}
	
	public String getSpuName() {
		return spuName;
	}
	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public List<ItemDetailSpec> getItemDetailSpecList() {
		return itemDetailSpecList;
	}
	public void setItemDetailSpecList(List<ItemDetailSpec> itemDetailSpecList) {
		this.itemDetailSpecList = itemDetailSpecList;
	}
	public Long getAddedValueRateId() {
		return addedValueRateId;
	}
	public void setAddedValueRateId(Long addedValueRateId) {
		this.addedValueRateId = addedValueRateId;
	}
	public Long getExciseRateId() {
		return exciseRateId;
	}
	public void setExciseRateId(Long exciseRateId) {
		this.exciseRateId = exciseRateId;
	}
	public Long getCustomsRateId() {
		return customsRateId;
	}
	public void setCustomsRateId(Long customsRateId) {
		this.customsRateId = customsRateId;
	}
	public Double getAddedValueRate() {
		return addedValueRate;
	}
	public void setAddedValueRate(Double addedValueRate) {
		this.addedValueRate = addedValueRate;
	}
	public Double getExciseRate() {
		return exciseRate;
	}
	public void setExciseRate(Double exciseRate) {
		this.exciseRate = exciseRate;
	}
	public Double getCustomsRate() {
		return customsRate;
	}
	public void setCustomsRate(Double customsRate) {
		this.customsRate = customsRate;
	}
	public Double getCommisionRate() {
		return commisionRate;
	}
	public void setCommisionRate(Double commisionRate) {
		this.commisionRate = commisionRate;
	}
	public Double getTopicPrice() {
		return topicPrice;
	}
	public void setTopicPrice(Double topicPrice) {
		this.topicPrice = topicPrice;
	}
	public Integer getUnitQuantity() {
		return unitQuantity;
	}
	public void setUnitQuantity(Integer unitQuantity) {
		this.unitQuantity = unitQuantity;
	}
	public Integer getWrapQuantity() {
		return wrapQuantity;
	}
	public void setWrapQuantity(Integer wrapQuantity) {
		this.wrapQuantity = wrapQuantity;
	}
}
