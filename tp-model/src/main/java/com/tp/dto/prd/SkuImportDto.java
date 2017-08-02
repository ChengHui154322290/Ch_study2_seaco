package com.tp.dto.prd;

import java.io.Serializable;
import java.util.List;

import com.tp.model.prd.ItemAttribute;
import com.tp.model.prd.ItemSkuArt;

/**
 * 
 * <pre>
 * 	商品批量导入
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class SkuImportDto implements Serializable{

	
	
	private static final long serialVersionUID = -8260904302790689744L;

	/** 供应商Id */
	private Long supplierId;
	
	/** 供应商名称 */
	private String supplierName;

	/** 供应商编码 */
	private String supplierCode;
	
	/** 条形码 */
	private String barcode;
	
	/** SPU名称 */
	private String spuName;
	
	/** 打雷名称 */
	private String largeName;
	
	/** 大类Id */
	private Long largId;
	
	/** 中类名称 */
	private String mediumName;
	
	/** 中类ID */
	private Long mediumId;
	
	/** 小类名称 */
	private String smallName;
	
	/** 小类id */
	private Long smallId;
	
	/** 单位 */
	private Long unitId;
	
	/** 品牌名称 */
	private String brandName;
	
	/** 品牌ID */
	private Long brandId;
	
	/** SPU备注*/
	private String spuRemark;
	
	/** 商品厂库名称*/
	private String storageTitle;
	
	/** item名称*/
	private String itemTitle;
	
	/** detail 表的主标题*/
	private String mainTitle;
	
	/** SkU名称 */
	private String skuName;
	
	/** SkU备注 */
	private String skuRemark;
	
	/** 热点商品名称 */
	private String skuSubTitle;
	
	/** 商品类型 */
	private Integer itemType;
	
	/** 商品规格 */
	private String specifications;
	
	/** 无理由退货天数 */
	private int returnDays;
	
	/** 吊牌价*/
	private Double basicPrice;

	/** 西客商城价*/
	private Double xgPrice;
	
	/** 运费模板 */
	private Long freightTemplateId;

	/** 生产厂家*/
	
	private String manufacturer;
	/** 毛重 单位g */
	private Double weight;

	/** 净重 单位g */
	private Double weightNet;

	/** 体积-宽度 */
	private Integer volumeWidth;

	/** 体积-长度 */
	private Integer volumeLength;

	/** 体积-高度 */
	private Integer volumeHigh;
	//采购税率,,销售税率,,*箱规,,是否有效期管理,,有效期,,是否海淘商品,,关税,,自定义属性组
	/** 销售税率 */
	private Double saleRate;

	/** 采购税率 */
	private Double purRate;

	/** 箱规 */
	private String cartonSpec;
	
	/** 是否保质期管理:1 是，2-否 默认1 */
	private Integer expSign;

	/** 保质期天数 */
	private Integer expDays;
	
	/** 是否海淘商品,1-否，2-是，默认1 */
	private Integer wavesSign;
	
	/** 关税税率 */
	private Long tarrifRate;
	
	private Long customsRateId;
	private Long exciseRateId;
	private Long addedvalueRateId;
	
	private String attibuteCus;
	
	/** 小类编码 */
	private String categoryCode;
	
	/** 上下架*/
	private int status;
	
	/**销售状态*/
	private int saleType;
	
	/**导入操作产生的信息*/
	private String msg;
	/**导入是否成功: 1-成功，2-失败*/
	private int importStatus;
	
	
	/**商品属性*/
	private List<ItemAttribute> attriList; 
	
	/** 销售维度1Id	*销售维度1	*销售维度2Id	*销售维度2	*销售维度3Id	*销售维度3 */
	private String spec1;
	private Long spec1Id;
	
	private String spec2;
	private Long spec2Id;
	
	private String spec3;
	private Long spec3Id;
	
	/** 销售规格组维度1Id	*销售维度规格组2Id		*销售维度规格组3Id	*/
	private Long spec1GroupId;
	private Long spec2GroupId;
	private Long spec3GroupId;
	
	/** excel行号 */
	private Long excelIndex;
	
	/**校验存在的商品*/
	private Long itemId;
	private Long detailId;
	private Long skuId;
	
	/**适用年龄**/
	private Long applyAgeId;
	

	/***海淘商品产地（国家ID）**/
	private Long countryId;
	
	/**海淘商品配送方式***/
	private String sendType;
	
	/** 货号 */
	private String articleNumber;

	/** 保税区key */
	private String bondedArea;
	
	/**商品海关信息*/
	private List<ItemSkuArt> skuArtList;

	/**导入来源*/
	private String importFrom;

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
	 * Getter method for property <tt>msg</tt>.
	 * 
	 * @return property value of msg
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * Setter method for property <tt>msg</tt>.
	 * 
	 * @param msg value to be assigned to property msg
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	/**
	 * Getter method for property <tt>importStatus</tt>.
	 * 
	 * @return property value of importStatus
	 */
	public int getImportStatus() {
		return importStatus;
	}
	/**
	 * Setter method for property <tt>importStatus</tt>.
	 * 
	 * @param importStatus value to be assigned to property importStatus
	 */
	public void setImportStatus(int importStatus) {
		this.importStatus = importStatus;
	}
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
	 * Getter method for property <tt>supplierCode</tt>.
	 * 
	 * @return property value of supplierCode
	 */
	public String getSupplierCode() {
		return supplierCode;
	}
	/**
	 * Setter method for property <tt>supplierCode</tt>.
	 * 
	 * @param supplierCode value to be assigned to property supplierCode
	 */
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
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
	 * Getter method for property <tt>unitId</tt>.
	 * 
	 * @return property value of unit
	 */
	public Long getUnitId() {
		return unitId;
	}
	/**
	 * Setter method for property <tt>uniIdt</tt>.
	 * 
	 * @param unit value to be assigned to property unit
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
	public int getReturnDays() {
		return returnDays;
	}
	/**
	 * Setter method for property <tt>returnDays</tt>.
	 * 
	 * @param returnDays value to be assigned to property returnDays
	 */
	public void setReturnDays(int returnDays) {
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
	 * Getter method for property <tt>freightTemplet</tt>.
	 * 
	 * @return property value of freightTemplet
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
	
	public Long getTarrifRate() {
		return tarrifRate;
	}
	public void setTarrifRate(Long tarrifRate) {
		this.tarrifRate = tarrifRate;
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
	 * Getter method for property <tt>status</tt>.
	 * 
	 * @return property value of status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * Setter method for property <tt>status</tt>.
	 * 
	 * @param status value to be assigned to property status
	 */
	public void setStatus(int status) {
		this.status = status;
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
	/**
	 * Getter method for property <tt>mainTitle</tt>.
	 * 
	 * @return property value of mainTitle
	 */
	public String getMainTitle() {
		return mainTitle;
	}
	/**
	 * Setter method for property <tt>mainTitle</tt>.
	 * 
	 * @param mainTitle value to be assigned to property mainTitle
	 */
	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}
	/**
	 * Getter method for property <tt>attriList</tt>.
	 * 
	 * @return property value of attriList
	 */
	public List<ItemAttribute> getAttriList() {
		return attriList;
	}
	/**
	 * Setter method for property <tt>attriList</tt>.
	 * 
	 * @param attriList value to be assigned to property attriList
	 */
	public void setAttriList(List<ItemAttribute> attriList) {
		this.attriList = attriList;
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
	 * Getter method for property <tt>spec1GroupId</tt>.
	 * 
	 * @return property value of spec1GroupId
	 */
	public Long getSpec1GroupId() {
		return spec1GroupId;
	}
	/**
	 * Setter method for property <tt>spec1Group1Id</tt>.
	 * 
	 * @param spec1Group1Id value to be assigned to property spec1Group1Id
	 */
	public void setSpec1GroupId(Long spec1GroupId) {
		this.spec1GroupId = spec1GroupId;
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
	 * Getter method for property <tt>excelIndex</tt>.
	 * 
	 * @return property value of excelIndex
	 */
	public Long getExcelIndex() {
		return excelIndex;
	}
	/**
	 * Setter method for property <tt>excelIndex</tt>.
	 * 
	 * @param excelIndex value to be assigned to property excelIndex
	 */
	public void setExcelIndex(Long excelIndex) {
		this.excelIndex = excelIndex;
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
	/**
	 * @return the countryId
	 */
	public Long getCountryId() {
		return countryId;
	}
	/**
	 * @param countryId the countryId to set
	 */
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
	/**
	 * @return the sendType
	 */
	public String getSendType() {
		return sendType;
	}
	/**
	 * @param sendType the sendType to set
	 */
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	/**
	 * @return the articleNumber
	 */
	public String getArticleNumber() {
		return articleNumber;
	}
	/**
	 * @param articleNumber the articleNumber to set
	 */
	public void setArticleNumber(String articleNumber) {
		this.articleNumber = articleNumber;
	}
	/**
	 * @return the bondedArea
	 */
	public String getBondedArea() {
		return bondedArea;
	}
	/**
	 * @param bondedArea the bondedArea to set
	 */
	public void setBondedArea(String bondedArea) {
		this.bondedArea = bondedArea;
	}
	/**
	 * @return the skuArtList
	 */
	public List<ItemSkuArt> getSkuArtList() {
		return skuArtList;
	}
	/**
	 * @param skuArtList the skuArtList to set
	 */
	public void setSkuArtList(List<ItemSkuArt> skuArtList) {
		this.skuArtList = skuArtList;
	}
	public Double getXgPrice() {
		return xgPrice;
	}
	public void setXgPrice(Double xgPrice) {
		this.xgPrice = xgPrice;
	}
	public Long getCustomsRateId() {
		return customsRateId;
	}
	public void setCustomsRateId(Long customsRateId) {
		this.customsRateId = customsRateId;
	}
	public Long getExciseRateId() {
		return exciseRateId;
	}
	public void setExciseRateId(Long exciseRateId) {
		this.exciseRateId = exciseRateId;
	}
	public Long getAddedvalueRateId() {
		return addedvalueRateId;
	}
	public void setAddedvalueRateId(Long addedvalueRateId) {
		this.addedvalueRateId = addedvalueRateId;
	}
	public String getImportFrom() {
		return importFrom;
	}
	public void setImportFrom(String importFrom) {
		this.importFrom = importFrom;
	}
	
}
