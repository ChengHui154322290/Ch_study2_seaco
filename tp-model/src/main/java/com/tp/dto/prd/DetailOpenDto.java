package com.tp.dto.prd;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.tp.model.prd.ItemDetailSpec;

public class DetailOpenDto implements Serializable {

	private static final long serialVersionUID = 1206899244093632381L;

	/** 小类编号+5位流水码 冗余字段 */
	private String spu;

	/** 商品ID */
	private Long itemId;

	/** 编号 spu+3位随机码 */
	private String prdid;

	/** 产品前台展示名称 */
	private String mainTitle;

	/** 卖点描述 */
	private String subTitle;

	/** 商品名称 */
	private String itemTitle;
	/** 存储在仓库中的名称 */
	private String storageTitle;

	/** 条码 */
	private String barcode;

	/** 生产厂家 */
	private String manufacturer;

	/** 商品类型：1-正常商品，2-服务商品，3-二手商品,4-报废商品 默认1 */
	private Integer itemType;

	private String itemTypeName;

	/** 市场价 */
	private Double basicPrice;

	/** 关税税率 */
	private Long tarrifRate;

	private String tarrifRateName;

	/** 销售税率 */
	private Long saleRate;

	private String saleRateName;

	/** 采购税率 */
	private Long purRate;

	private String purRateName;

	/** 运费模板 */
	private Long freightTemplateId;

	private String freightTemplateName;

	/** 是否海淘商品,1-否，2-是，默认1 */
	private Integer wavesSign;

	/** 无理由退货期限 单位 天 */
	private Integer returnDays;

	/** 是否保质期管理:1 是，2-否 默认1 */
	private Integer expSign;

	/** 保质期天数 */
	private Integer expDays;

	/** 是否进口产品:1 是，2-否 默认2 */
	private Integer importedSign;

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

	/** 品牌ID */
	private Long brandId;
	/** 品类三级code */
	private String categoryCode;
	/** 状态 **/
	private Integer status;

	/** 单位id */
	private Long unitId;

	/** 规格 */
	private String specifications;
	/** 箱规 */
	private String cartonSpec;

	/** 规格组 id */
	private String specGroupIds;

	/** 规格id */
	private String specIds;

	/** 做 显示 时候 用 */
	private List<ItemDetailSpec> itemDetailSpecList;

	/** 做prdid纬度展现，不做新增，修改 */
	private String onSaleStr;

	/** 备注 */
	private String remark;

	/** 适用年龄 **/
	private Long applyAgeId;

	private String applyAgeName;

	/*** 海淘商品产地（国家ID） **/
	private Long countryId;

	private String countryName;

	/** 海淘商品配送方式 ***/
	private String sendType;

	/** spu_name **/
	private String spuName;

	private HashMap<String, String> specGroupAndSpec;

	private HashMap<String, String> customeAttr;

	private HashMap<String, List<String>> baseAttr;

	public String getSpu() {
		return spu;
	}

	public void setSpu(String spu) {
		this.spu = spu;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getPrdid() {
		return prdid;
	}

	public void setPrdid(String prdid) {
		this.prdid = prdid;
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

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public String getStorageTitle() {
		return storageTitle;
	}

	public void setStorageTitle(String storageTitle) {
		this.storageTitle = storageTitle;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Integer getItemType() {
		return itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}

	public String getItemTypeName() {
		return itemTypeName;
	}

	public void setItemTypeName(String itemTypeName) {
		this.itemTypeName = itemTypeName;
	}

	public Double getBasicPrice() {
		return basicPrice;
	}

	public void setBasicPrice(Double basicPrice) {
		this.basicPrice = basicPrice;
	}

	public Long getTarrifRate() {
		return tarrifRate;
	}

	public void setTarrifRate(Long tarrifRate) {
		this.tarrifRate = tarrifRate;
	}

	public String getTarrifRateName() {
		return tarrifRateName;
	}

	public void setTarrifRateName(String tarrifRateName) {
		this.tarrifRateName = tarrifRateName;
	}

	public Long getSaleRate() {
		return saleRate;
	}

	public void setSaleRate(Long saleRate) {
		this.saleRate = saleRate;
	}

	public String getSaleRateName() {
		return saleRateName;
	}

	public void setSaleRateName(String saleRateName) {
		this.saleRateName = saleRateName;
	}

	public Long getPurRate() {
		return purRate;
	}

	public void setPurRate(Long purRate) {
		this.purRate = purRate;
	}

	public String getPurRateName() {
		return purRateName;
	}

	public void setPurRateName(String purRateName) {
		this.purRateName = purRateName;
	}

	public Long getFreightTemplateId() {
		return freightTemplateId;
	}

	public void setFreightTemplateId(Long freightTemplateId) {
		this.freightTemplateId = freightTemplateId;
	}

	public String getFreightTemplateName() {
		return freightTemplateName;
	}

	public void setFreightTemplateName(String freightTemplateName) {
		this.freightTemplateName = freightTemplateName;
	}

	public Integer getWavesSign() {
		return wavesSign;
	}

	public void setWavesSign(Integer wavesSign) {
		this.wavesSign = wavesSign;
	}

	public Integer getReturnDays() {
		return returnDays;
	}

	public void setReturnDays(Integer returnDays) {
		this.returnDays = returnDays;
	}

	public Integer getExpSign() {
		return expSign;
	}

	public void setExpSign(Integer expSign) {
		this.expSign = expSign;
	}

	public Integer getExpDays() {
		return expDays;
	}

	public void setExpDays(Integer expDays) {
		this.expDays = expDays;
	}

	public Integer getImportedSign() {
		return importedSign;
	}

	public void setImportedSign(Integer importedSign) {
		this.importedSign = importedSign;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getWeightNet() {
		return weightNet;
	}

	public void setWeightNet(Double weightNet) {
		this.weightNet = weightNet;
	}

	public Integer getVolumeWidth() {
		return volumeWidth;
	}

	public void setVolumeWidth(Integer volumeWidth) {
		this.volumeWidth = volumeWidth;
	}

	public Integer getVolumeLength() {
		return volumeLength;
	}

	public void setVolumeLength(Integer volumeLength) {
		this.volumeLength = volumeLength;
	}

	public Integer getVolumeHigh() {
		return volumeHigh;
	}

	public void setVolumeHigh(Integer volumeHigh) {
		this.volumeHigh = volumeHigh;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public String getCartonSpec() {
		return cartonSpec;
	}

	public void setCartonSpec(String cartonSpec) {
		this.cartonSpec = cartonSpec;
	}

	public String getSpecGroupIds() {
		return specGroupIds;
	}

	public void setSpecGroupIds(String specGroupIds) {
		this.specGroupIds = specGroupIds;
	}

	public String getSpecIds() {
		return specIds;
	}

	public void setSpecIds(String specIds) {
		this.specIds = specIds;
	}

	public String getOnSaleStr() {
		return onSaleStr;
	}

	public void setOnSaleStr(String onSaleStr) {
		this.onSaleStr = onSaleStr;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getApplyAgeId() {
		return applyAgeId;
	}

	public void setApplyAgeId(Long applyAgeId) {
		this.applyAgeId = applyAgeId;
	}

	public String getApplyAgeName() {
		return applyAgeName;
	}

	public void setApplyAgeName(String applyAgeName) {
		this.applyAgeName = applyAgeName;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getSpuName() {
		return spuName;
	}

	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}

	public HashMap<String, String> getSpecGroupAndSpec() {
		return specGroupAndSpec;
	}

	public void setSpecGroupAndSpec(HashMap<String, String> specGroupAndSpec) {
		this.specGroupAndSpec = specGroupAndSpec;
	}

	public HashMap<String, String> getCustomeAttr() {
		return customeAttr;
	}

	public void setCustomeAttr(HashMap<String, String> customeAttr) {
		this.customeAttr = customeAttr;
	}

	public HashMap<String, List<String>> getBaseAttr() {
		return baseAttr;
	}

	public void setBaseAttr(HashMap<String, List<String>> baseAttr) {
		this.baseAttr = baseAttr;
	}

	public List<ItemDetailSpec> getItemDetailSpecList() {
		return itemDetailSpecList;
	}

	public void setItemDetailSpecList(List<ItemDetailSpec> itemDetailSpecList) {
		this.itemDetailSpecList = itemDetailSpecList;
	}
}
