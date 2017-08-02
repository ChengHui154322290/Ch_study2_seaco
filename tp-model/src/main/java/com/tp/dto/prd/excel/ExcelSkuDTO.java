package com.tp.dto.prd.excel;

import java.util.ArrayList;
import java.util.List;

import com.tp.common.annotation.excel.poi.ExcelEntity;
import com.tp.common.annotation.excel.poi.ExcelProperty;
import com.tp.model.prd.ItemAttribute;

/**
 * 
 * <pre>
 * 	 excel模板对应的实体类
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
@ExcelEntity
public class ExcelSkuDTO  extends ExcelBaseDTO{
	
	@ExcelProperty(value="商家名称",itemLatitude=3)
	private String supplierName;

	@ExcelProperty(value="*商家ID" ,required=true,itemLatitude=3)
	private Long supplierId;
	
	@ExcelProperty(value="*条形码" ,required=true,itemLatitude=2)
	private String barcode;
	
	@ExcelProperty(value="*款号",required=true,itemLatitude=1)
	private String spuName;
	
	@ExcelProperty(value="大类名称",itemLatitude=1)
	private String largeName;
	@ExcelProperty(value="*大类ID",required=true,itemLatitude=1)
	private Long largId;
	
	@ExcelProperty(value="中类名称",itemLatitude=1)
	private String mediumName;
	@ExcelProperty(value="*中类ID",required=true,itemLatitude=1)
	private Long mediumId;
	
	@ExcelProperty("小类名称")
	private String smallName;
	@ExcelProperty(value="*小类ID",required=true,itemLatitude=1)
	private Long smallId;
	
	/** 小类编码 */
	@ExcelProperty(value="*小类编码",itemLatitude=1)
	private String categoryCode;
	
	
	@ExcelProperty(value="*单位ID",required=true,itemLatitude=1)
	private Long unitId;
	@ExcelProperty(value="单位名称",itemLatitude=1)
	private String unit;

	@ExcelProperty(value="品牌名称",itemLatitude=1)
	private String brandName;
	@ExcelProperty(value="*品牌ID",required=true,itemLatitude=1)
	private Long brandId;
	
	/** 运费模板 */
	@ExcelProperty(value="运费名称",required=true,itemLatitude=2)
	private String freightTemplate;
	@ExcelProperty(value="*运费ID",required=true,itemLatitude=2)
	private Integer freightTemplateId;
	
	
	@ExcelProperty(value="*SKU名称",required=true,itemLatitude=3)
	private String skuName;
	
	/**规格组 */
	@ExcelProperty(value="规格组-1名称",itemLatitude =2 )
	private String specGroup1;
	/**规格组ID */
	@ExcelProperty(value="*规格组-1ID",itemLatitude =2)
	private Long spec1GroupId;
	@ExcelProperty(value="*规格1名称",itemLatitude =2 )
	private String spec1;
	@ExcelProperty(value=" *规格1ID",itemLatitude =2 )
	private Long spec1Id;
	
	@ExcelProperty(value="规格组-2名称",itemLatitude =2 )
	private String specGroup2;
	/**规格组ID */
	@ExcelProperty(value="*规格组-2ID",itemLatitude =2)
	private Long spec2GroupId;
	@ExcelProperty(value="*规格2名称",itemLatitude =2 )
	private String spec2;
	@ExcelProperty(value=" *规格2ID",itemLatitude =2 )
	private Long spec2Id;
	
	@ExcelProperty(value="规格组-3名称",itemLatitude =2 )
	private String specGroup3;
	/**规格组ID */
	@ExcelProperty(value="*规格组-3ID",itemLatitude =2)
	private Long spec3GroupId;
	@ExcelProperty(value="*规格3名称",itemLatitude =2 )
	private String spec3;
	@ExcelProperty(value=" *规格3ID",itemLatitude =2 )
	private Long spec3Id;
	
	//*商品卖点,,*商品类型,,*规格,,*无理由退货期限,,*吊牌价,,*运费模板,,长（CM）,,宽（CM）,,
	@ExcelProperty(value="*副标题",required=true,itemLatitude=2)
	private String skuSubTitle;

	@ExcelProperty(value="*商品类型",required=true,itemLatitude=2)
	private String itemTypeStr;
	private Integer itemType;
	
	@ExcelProperty(value="商品规格",itemLatitude=2)
	private String specifications;
	@ExcelProperty(value="*无理由退货期限（天）",required=true,itemLatitude=2)
	private Integer returnDays;
	@ExcelProperty(value="*市场价（元）",required=true,itemLatitude=2)
	private Double basicPrice;
	

	@ExcelProperty(value="备注（SPU）",itemLatitude=1)
	private String spuRemark;
	
	@ExcelProperty(value = "备注（SKU）",itemLatitude=1)
	private String skuRemark;
	/** 毛重 单位g */
	@ExcelProperty(value="*毛重（g）",required=true,itemLatitude=2)
	private Double weight;
	@ExcelProperty(value="生产厂家",itemLatitude=2)
	private String manufacturer;

	/** 箱规 */
	@ExcelProperty(value="箱规",required=true,itemLatitude=2)
	private String cartonSpec;
	
	/** 净重 单位g */
	@ExcelProperty(value="净重",itemLatitude=2)
	private Double weightNet;


	/** 是否保质期管理:1 是，2-否 默认1 */
	@ExcelProperty(value="*是否有效期管理",itemLatitude=2)
	private String expSignStr;
	
	private Integer expSign;

	/** 保质期天数 */
	@ExcelProperty(value="有效期（月）",itemLatitude=2)
	private Integer expDays;
	
	/** 是否海淘商品,1-否，2-是，默认1 */
	@ExcelProperty(value="是否海淘商品",itemLatitude=2)
	private String wavesSignStr;
	
	private Integer wavesSign;
	
	@ExcelProperty(value="自定义属性组",itemLatitude=2)
	private String attibuteCus;
	
	@ExcelProperty(value="*仓库名称",itemLatitude=2)
	private String storageTitle;
	/** 体积-宽度 */
	@ExcelProperty(value="宽（CM）",itemLatitude=2)
	private Integer volumeWidth;

	/** 体积-长度 */
	@ExcelProperty(value="长（CM）",itemLatitude=2)
	private Integer volumeLength;

	/** 体积-高度 */
	@ExcelProperty(value="高（CM）",itemLatitude=2)
	private Integer volumeHigh;
	//采购税率,,销售税率,,*箱规,,是否有效期管理,,有效期,,是否海淘商品,,关税,,自定义属性组
	/** 销售税率 */
	@ExcelProperty(value="销售税率",itemLatitude=2)
	private Double saleRate;
	/** 采购税率 */
	@ExcelProperty(value="采购税率",itemLatitude=2)
	private Double purRate;
	
	/** 适用年龄 */
	@ExcelProperty(value="适用年龄",itemLatitude=2)
	private String applyAge;
	
	/** 适用年龄 */
	@ExcelProperty(value="*适用年龄ID",itemLatitude=2,required=true)
	private Long applyAgeId;
	
	/**商品属性*/
	private List<ItemAttribute> itemAttributeList;
	
	/**销售状态*/
	private int saleType;
	
	/**校验存在的商品*/
	private Long itemId;
	private Long detailId;
	private Long skuId;

	/**
	 * Getter method for property <tt>supplierName</tt>.
	 * 
	 * @return property value of supplierName
	 */
	public String getSupplierName() {
		return supplierName;
	}

	/**
	 * Setter method for property <tt>supplierName</tt>.
	 * 
	 * @param supplierName value to be assigned to property supplierName
	 */
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	/**
	 * Getter method for property <tt>supplierId</tt>.
	 * 
	 * @return property value of supplierId
	 */
	public Long getSupplierId() {
		return supplierId;
	}

	/**
	 * Setter method for property <tt>supplierId</tt>.
	 * 
	 * @param supplierId value to be assigned to property supplierId
	 */
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	/**
	 * Getter method for property <tt>barcode</tt>.
	 * 
	 * @return property value of barcode
	 */
	public String getBarcode() {
		return barcode;
	}

	/**
	 * Setter method for property <tt>barcode</tt>.
	 * 
	 * @param barcode value to be assigned to property barcode
	 */
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	/**
	 * Getter method for property <tt>spuName</tt>.
	 * 
	 * @return property value of spuName
	 */
	public String getSpuName() {
		return spuName;
	}

	/**
	 * Setter method for property <tt>spuName</tt>.
	 * 
	 * @param spuName value to be assigned to property spuName
	 */
	public void setSpuName(String spuName) {
		this.spuName = spuName;
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
	 * Getter method for property <tt>largId</tt>.
	 * 
	 * @return property value of largId
	 */
	public Long getLargId() {
		return largId;
	}

	/**
	 * Setter method for property <tt>largId</tt>.
	 * 
	 * @param largId value to be assigned to property largId
	 */
	public void setLargId(Long largId) {
		this.largId = largId;
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
	 * Getter method for property <tt>mediumId</tt>.
	 * 
	 * @return property value of mediumId
	 */
	public Long getMediumId() {
		return mediumId;
	}

	/**
	 * Setter method for property <tt>mediumId</tt>.
	 * 
	 * @param mediumId value to be assigned to property mediumId
	 */
	public void setMediumId(Long mediumId) {
		this.mediumId = mediumId;
	}

	/**
	 * Getter method for property <tt>smallName</tt>.
	 * 
	 * @return property value of smallName
	 */
	public String getSmallName() {
		return smallName;
	}

	/**
	 * Setter method for property <tt>smallName</tt>.
	 * 
	 * @param smallName value to be assigned to property smallName
	 */
	public void setSmallName(String smallName) {
		this.smallName = smallName;
	}

	/**
	 * Getter method for property <tt>smallId</tt>.
	 * 
	 * @return property value of smallId
	 */
	public Long getSmallId() {
		return smallId;
	}

	/**
	 * Setter method for property <tt>smallId</tt>.
	 * 
	 * @param smallId value to be assigned to property smallId
	 */
	public void setSmallId(Long smallId) {
		this.smallId = smallId;
	}

	/**
	 * Getter method for property <tt>categoryCode</tt>.
	 * 
	 * @return property value of categoryCode
	 */
	public String getCategoryCode() {
		return categoryCode;
	}

	/**
	 * Setter method for property <tt>categoryCode</tt>.
	 * 
	 * @param categoryCode value to be assigned to property categoryCode
	 */
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	/**
	 * Getter method for property <tt>unitId</tt>.
	 * 
	 * @return property value of unitId
	 */
	public Long getUnitId() {
		return unitId;
	}

	/**
	 * Setter method for property <tt>unitId</tt>.
	 * 
	 * @param unitId value to be assigned to property unitId
	 */
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	/**
	 * Getter method for property <tt>brandName</tt>.
	 * 
	 * @return property value of brandName
	 */
	public String getBrandName() {
		return brandName;
	}

	/**
	 * Setter method for property <tt>brandName</tt>.
	 * 
	 * @param brandName value to be assigned to property brandName
	 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	/**
	 * Getter method for property <tt>brandId</tt>.
	 * 
	 * @return property value of brandId
	 */
	public Long getBrandId() {
		return brandId;
	}

	/**
	 * Setter method for property <tt>brandId</tt>.
	 * 
	 * @param brandId value to be assigned to property brandId
	 */
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	/**
	 * Getter method for property <tt>spuRemark</tt>.
	 * 
	 * @return property value of spuRemark
	 */
	public String getSpuRemark() {
		return spuRemark;
	}

	/**
	 * Setter method for property <tt>spuRemark</tt>.
	 * 
	 * @param spuRemark value to be assigned to property spuRemark
	 */
	public void setSpuRemark(String spuRemark) {
		this.spuRemark = spuRemark;
	}

	/**
	 * Getter method for property <tt>skuName</tt>.
	 * 
	 * @return property value of skuName
	 */
	public String getSkuName() {
		return skuName;
	}

	/**
	 * Setter method for property <tt>skuName</tt>.
	 * 
	 * @param skuName value to be assigned to property skuName
	 */
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	/**
	 * Getter method for property <tt>storageTitle</tt>.
	 * 
	 * @return property value of storageTitle
	 */
	public String getStorageTitle() {
		return storageTitle;
	}

	/**
	 * Setter method for property <tt>storageTitle</tt>.
	 * 
	 * @param storageTitle value to be assigned to property storageTitle
	 */
	public void setStorageTitle(String storageTitle) {
		this.storageTitle = storageTitle;
	}

	/**
	 * Getter method for property <tt>skuRemark</tt>.
	 * 
	 * @return property value of skuRemark
	 */
	public String getSkuRemark() {
		return skuRemark;
	}

	/**
	 * Setter method for property <tt>skuRemark</tt>.
	 * 
	 * @param skuRemark value to be assigned to property skuRemark
	 */
	public void setSkuRemark(String skuRemark) {
		this.skuRemark = skuRemark;
	}

	/**
	 * Getter method for property <tt>skuSubTitle</tt>.
	 * 
	 * @return property value of skuSubTitle
	 */
	public String getSkuSubTitle() {
		return skuSubTitle;
	}

	/**
	 * Setter method for property <tt>skuSubTitle</tt>.
	 * 
	 * @param skuSubTitle value to be assigned to property skuSubTitle
	 */
	public void setSkuSubTitle(String skuSubTitle) {
		this.skuSubTitle = skuSubTitle;
	}

	/**
	 * Getter method for property <tt>itemTypeStr</tt>.
	 * 
	 * @return property value of itemTypeStr
	 */
	public String getItemTypeStr() {
		return itemTypeStr;
	}

	/**
	 * Setter method for property <tt>itemTypeStr</tt>.
	 * 
	 * @param itemTypeStr value to be assigned to property itemTypeStr
	 */
	public void setItemTypeStr(String itemTypeStr) {
		this.itemTypeStr = itemTypeStr;
	}

	/**
	 * Getter method for property <tt>itemType</tt>.
	 * 
	 * @return property value of itemType
	 */
	public Integer getItemType() {
		return itemType;
	}

	/**
	 * Setter method for property <tt>itemType</tt>.
	 * 
	 * @param itemType value to be assigned to property itemType
	 */
	public void setItemType(Integer itemType) {
		this.itemType = itemType;
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
	 * Getter method for property <tt>returnDays</tt>.
	 * 
	 * @return property value of returnDays
	 */
	public Integer getReturnDays() {
		return returnDays;
	}

	/**
	 * Setter method for property <tt>returnDays</tt>.
	 * 
	 * @param returnDays value to be assigned to property returnDays
	 */
	public void setReturnDays(Integer returnDays) {
		this.returnDays = returnDays;
	}

	/**
	 * Getter method for property <tt>basicPrice</tt>.
	 * 
	 * @return property value of basicPrice
	 */
	public Double getBasicPrice() {
		return basicPrice;
	}

	/**
	 * Setter method for property <tt>basicPrice</tt>.
	 * 
	 * @param basicPrice value to be assigned to property basicPrice
	 */
	public void setBasicPrice(Double basicPrice) {
		this.basicPrice = basicPrice;
	}

	/**
	 * Getter method for property <tt>freightTemplate</tt>.
	 * 
	 * @return property value of freightTemplate
	 */
	public String getFreightTemplate() {
		return freightTemplate;
	}

	/**
	 * Setter method for property <tt>freightTemplate</tt>.
	 * 
	 * @param freightTemplate value to be assigned to property freightTemplate
	 */
	public void setFreightTemplate(String freightTemplate) {
		this.freightTemplate = freightTemplate;
	}

	/**
	 * Getter method for property <tt>freightTemplateId</tt>.
	 * 
	 * @return property value of freightTemplateId
	 */
	public Integer getFreightTemplateId() {
		return freightTemplateId;
	}

	/**
	 * Setter method for property <tt>freightTemplateId</tt>.
	 * 
	 * @param freightTemplateId value to be assigned to property freightTemplateId
	 */
	public void setFreightTemplateId(Integer freightTemplateId) {
		this.freightTemplateId = freightTemplateId;
	}

	/**
	 * Getter method for property <tt>manufacturer</tt>.
	 * 
	 * @return property value of manufacturer
	 */
	public String getManufacturer() {
		return manufacturer;
	}

	/**
	 * Setter method for property <tt>manufacturer</tt>.
	 * 
	 * @param manufacturer value to be assigned to property manufacturer
	 */
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
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
	 * Getter method for property <tt>volumeWidth</tt>.
	 * 
	 * @return property value of volumeWidth
	 */
	public Integer getVolumeWidth() {
		return volumeWidth;
	}

	/**
	 * Setter method for property <tt>volumeWidth</tt>.
	 * 
	 * @param volumeWidth value to be assigned to property volumeWidth
	 */
	public void setVolumeWidth(Integer volumeWidth) {
		this.volumeWidth = volumeWidth;
	}

	/**
	 * Getter method for property <tt>volumeLength</tt>.
	 * 
	 * @return property value of volumeLength
	 */
	public Integer getVolumeLength() {
		return volumeLength;
	}

	/**
	 * Setter method for property <tt>volumeLength</tt>.
	 * 
	 * @param volumeLength value to be assigned to property volumeLength
	 */
	public void setVolumeLength(Integer volumeLength) {
		this.volumeLength = volumeLength;
	}

	/**
	 * Getter method for property <tt>volumeHigh</tt>.
	 * 
	 * @return property value of volumeHigh
	 */
	public Integer getVolumeHigh() {
		return volumeHigh;
	}

	/**
	 * Setter method for property <tt>volumeHigh</tt>.
	 * 
	 * @param volumeHigh value to be assigned to property volumeHigh
	 */
	public void setVolumeHigh(Integer volumeHigh) {
		this.volumeHigh = volumeHigh;
	}

	/**
	 * Getter method for property <tt>saleRate</tt>.
	 * 
	 * @return property value of saleRate
	 */
	public Double getSaleRate() {
		return saleRate;
	}

	/**
	 * Setter method for property <tt>saleRate</tt>.
	 * 
	 * @param saleRate value to be assigned to property saleRate
	 */
	public void setSaleRate(Double saleRate) {
		this.saleRate = saleRate;
	}

	/**
	 * Getter method for property <tt>purRate</tt>.
	 * 
	 * @return property value of purRate
	 */
	public Double getPurRate() {
		return purRate;
	}

	/**
	 * Setter method for property <tt>purRate</tt>.
	 * 
	 * @param purRate value to be assigned to property purRate
	 */
	public void setPurRate(Double purRate) {
		this.purRate = purRate;
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
	 * Getter method for property <tt>expSignStr</tt>.
	 * 
	 * @return property value of expSignStr
	 */
	public String getExpSignStr() {
		return expSignStr;
	}

	/**
	 * Setter method for property <tt>expSignStr</tt>.
	 * 
	 * @param expSignStr value to be assigned to property expSignStr
	 */
	public void setExpSignStr(String expSignStr) {
		this.expSignStr = expSignStr;
	}

	/**
	 * Getter method for property <tt>expSign</tt>.
	 * 
	 * @return property value of expSign
	 */
	public Integer getExpSign() {
		return expSign;
	}

	/**
	 * Setter method for property <tt>expSign</tt>.
	 * 
	 * @param expSign value to be assigned to property expSign
	 */
	public void setExpSign(Integer expSign) {
		this.expSign = expSign;
	}

	/**
	 * Getter method for property <tt>expDays</tt>.
	 * 
	 * @return property value of expDays
	 */
	public Integer getExpDays() {
		return expDays;
	}

	/**
	 * Setter method for property <tt>expDays</tt>.
	 * 
	 * @param expDays value to be assigned to property expDays
	 */
	public void setExpDays(Integer expDays) {
		this.expDays = expDays;
	}

	/**
	 * Getter method for property <tt>wavesSignStr</tt>.
	 * 
	 * @return property value of wavesSignStr
	 */
	public String getWavesSignStr() {
		return wavesSignStr;
	}

	/**
	 * Setter method for property <tt>wavesSignStr</tt>.
	 * 
	 * @param wavesSignStr value to be assigned to property wavesSignStr
	 */
	public void setWavesSignStr(String wavesSignStr) {
		this.wavesSignStr = wavesSignStr;
	}

	/**
	 * Getter method for property <tt>wavesSign</tt>.
	 * 
	 * @return property value of wavesSign
	 */
	public Integer getWavesSign() {
		return wavesSign;
	}

	/**
	 * Setter method for property <tt>wavesSign</tt>.
	 * 
	 * @param wavesSign value to be assigned to property wavesSign
	 */
	public void setWavesSign(Integer wavesSign) {
		this.wavesSign = wavesSign;
	}


	/**
	 * Getter method for property <tt>attibuteCus</tt>.
	 * 
	 * @return property value of attibuteCus
	 */
	public String getAttibuteCus() {
		return attibuteCus;
	}

	/**
	 * Setter method for property <tt>attibuteCus</tt>.
	 * 
	 * @param attibuteCus value to be assigned to property attibuteCus
	 */
	public void setAttibuteCus(String attibuteCus) {
		this.attibuteCus = attibuteCus;
	}

	/**
	 * Getter method for property <tt>specGroup1</tt>.
	 * 
	 * @return property value of specGroup1
	 */
	public String getSpecGroup1() {
		return specGroup1;
	}

	/**
	 * Setter method for property <tt>specGroup1</tt>.
	 * 
	 * @param specGroup1 value to be assigned to property specGroup1
	 */
	public void setSpecGroup1(String specGroup1) {
		this.specGroup1 = specGroup1;
	}

	/**
	 * Getter method for property <tt>spec1GroupId</tt>.
	 * 
	 * @return property value of spec1GroupId
	 */
	public Long getSpec1GroupId() {
		return spec1GroupId;
	}

	/**
	 * Setter method for property <tt>spec1GroupId</tt>.
	 * 
	 * @param spec1GroupId value to be assigned to property spec1GroupId
	 */
	public void setSpec1GroupId(Long spec1GroupId) {
		this.spec1GroupId = spec1GroupId;
	}

	/**
	 * Getter method for property <tt>specGroup2</tt>.
	 * 
	 * @return property value of specGroup2
	 */
	public String getSpecGroup2() {
		return specGroup2;
	}

	/**
	 * Setter method for property <tt>specGroup2</tt>.
	 * 
	 * @param specGroup2 value to be assigned to property specGroup2
	 */
	public void setSpecGroup2(String specGroup2) {
		this.specGroup2 = specGroup2;
	}

	/**
	 * Getter method for property <tt>spec2GroupId</tt>.
	 * 
	 * @return property value of spec2GroupId
	 */
	public Long getSpec2GroupId() {
		return spec2GroupId;
	}

	/**
	 * Setter method for property <tt>spec2GroupId</tt>.
	 * 
	 * @param spec2GroupId value to be assigned to property spec2GroupId
	 */
	public void setSpec2GroupId(Long spec2GroupId) {
		this.spec2GroupId = spec2GroupId;
	}

	/**
	 * Getter method for property <tt>specGroup3</tt>.
	 * 
	 * @return property value of specGroup3
	 */
	public String getSpecGroup3() {
		return specGroup3;
	}

	/**
	 * Setter method for property <tt>specGroup3</tt>.
	 * 
	 * @param specGroup3 value to be assigned to property specGroup3
	 */
	public void setSpecGroup3(String specGroup3) {
		this.specGroup3 = specGroup3;
	}

	/**
	 * Getter method for property <tt>spec3GroupId</tt>.
	 * 
	 * @return property value of spec3GroupId
	 */
	public Long getSpec3GroupId() {
		return spec3GroupId;
	}

	/**
	 * Setter method for property <tt>spec3GroupId</tt>.
	 * 
	 * @param spec3GroupId value to be assigned to property spec3GroupId
	 */
	public void setSpec3GroupId(Long spec3GroupId) {
		this.spec3GroupId = spec3GroupId;
	}

	/**
	 * Getter method for property <tt>spec1</tt>.
	 * 
	 * @return property value of spec1
	 */
	public String getSpec1() {
		return spec1;
	}

	/**
	 * Setter method for property <tt>spec1</tt>.
	 * 
	 * @param spec1 value to be assigned to property spec1
	 */
	public void setSpec1(String spec1) {
		this.spec1 = spec1;
	}

	/**
	 * Getter method for property <tt>spec1Id</tt>.
	 * 
	 * @return property value of spec1Id
	 */
	public Long getSpec1Id() {
		return spec1Id;
	}

	/**
	 * Setter method for property <tt>spec1Id</tt>.
	 * 
	 * @param spec1Id value to be assigned to property spec1Id
	 */
	public void setSpec1Id(Long spec1Id) {
		this.spec1Id = spec1Id;
	}

	/**
	 * Getter method for property <tt>spec2</tt>.
	 * 
	 * @return property value of spec2
	 */
	public String getSpec2() {
		return spec2;
	}

	/**
	 * Setter method for property <tt>spec2</tt>.
	 * 
	 * @param spec2 value to be assigned to property spec2
	 */
	public void setSpec2(String spec2) {
		this.spec2 = spec2;
	}

	/**
	 * Getter method for property <tt>spec2Id</tt>.
	 * 
	 * @return property value of spec2Id
	 */
	public Long getSpec2Id() {
		return spec2Id;
	}

	/**
	 * Setter method for property <tt>spec2Id</tt>.
	 * 
	 * @param spec2Id value to be assigned to property spec2Id
	 */
	public void setSpec2Id(Long spec2Id) {
		this.spec2Id = spec2Id;
	}

	/**
	 * Getter method for property <tt>spec3</tt>.
	 * 
	 * @return property value of spec3
	 */
	public String getSpec3() {
		return spec3;
	}

	/**
	 * Setter method for property <tt>spec3</tt>.
	 * 
	 * @param spec3 value to be assigned to property spec3
	 */
	public void setSpec3(String spec3) {
		this.spec3 = spec3;
	}

	/**
	 * Getter method for property <tt>spec3Id</tt>.
	 * 
	 * @return property value of spec3Id
	 */
	public Long getSpec3Id() {
		return spec3Id;
	}

	/**
	 * Setter method for property <tt>spec3Id</tt>.
	 * 
	 * @param spec3Id value to be assigned to property spec3Id
	 */
	public void setSpec3Id(Long spec3Id) {
		this.spec3Id = spec3Id;
	}

	/**
	 * Getter method for property <tt>saleType</tt>.
	 * 
	 * @return property value of saleType
	 */
	public int getSaleType() {
		return saleType;
	}

	/**
	 * Setter method for property <tt>saleType</tt>.
	 * 
	 * @param saleType value to be assigned to property saleType
	 */
	public void setSaleType(int saleType) {
		this.saleType = saleType;
	}

	/**
	 * Getter method for property <tt>unit</tt>.
	 * 
	 * @return property value of unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Setter method for property <tt>unit</tt>.
	 * 
	 * @param unit value to be assigned to property unit
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * 
	 * <pre>
	 *  转换自定义属性
	 *  重量=100g;体积=30立方
	 * </pre>
	 *
	 */
	private void getAttributeList(){
		//重量=100g;体积=30立方
		List<ItemAttribute> list = new ArrayList<ItemAttribute>();
		if((attibuteCus.indexOf("=")!=-1)){
			String [] str = attibuteCus.split("\\;");
			for(String s : str){
				if(s.indexOf("=")!=-1){
					String [] attrs = s.split("=");
					ItemAttribute a = new ItemAttribute();
					a.setAttrKey(attrs[0]);
					a.setAttrValue(attrs[1]);
					list.add(a);
				}
			}
		}
		itemAttributeList = list;
	}

	/**
	 * Getter method for property <tt>itemId</tt>.
	 * 
	 * @return property value of itemId
	 */
	public Long getItemId() {
		return itemId;
	}

	/**
	 * Setter method for property <tt>itemId</tt>.
	 * 
	 * @param itemId value to be assigned to property itemId
	 */
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	/**
	 * Getter method for property <tt>detailId</tt>.
	 * 
	 * @return property value of detailId
	 */
	public Long getDetailId() {
		return detailId;
	}

	/**
	 * Setter method for property <tt>detailId</tt>.
	 * 
	 * @param detailId value to be assigned to property detailId
	 */
	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	/**
	 * Getter method for property <tt>skuId</tt>.
	 * 
	 * @return property value of skuId
	 */
	public Long getSkuId() {
		return skuId;
	}

	/**
	 * Setter method for property <tt>skuId</tt>.
	 * 
	 * @param skuId value to be assigned to property skuId
	 */
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	/**
	 * @return the applyAge
	 */
	public String getApplyAge() {
		return applyAge;
	}

	/**
	 * @param applyAge the applyAge to set
	 */
	public void setApplyAge(String applyAge) {
		this.applyAge = applyAge;
	}

	/**
	 * @return the applyAgeId
	 */
	public Long getApplyAgeId() {
		return applyAgeId;
	}

	/**
	 * @param applyAgeId the applyAgeId to set
	 */
	public void setApplyAgeId(Long applyAgeId) {
		this.applyAgeId = applyAgeId;
	}

	public List<ItemAttribute> getItemAttributeList() {
		getAttributeList();
		return itemAttributeList;
	}

	public void setItemAttributeList(List<ItemAttribute> itemAttributeList) {
		this.itemAttributeList = itemAttributeList;
	}
	
	
	
}