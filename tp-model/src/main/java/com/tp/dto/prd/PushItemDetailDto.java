package com.tp.dto.prd;

import java.io.Serializable;


public class PushItemDetailDto implements Serializable {
    
	private static final long serialVersionUID = -5362257266266518387L;

	/** 产品前台展示名称 */
	private String mainTitle;

	/** 卖点描述 */
	private String subTitle;
	
	/**存储在仓库中的名称*/
	private String storageTitle;

	/** 条码 */
	private String barcode;

	/** 生产厂家 */
	private String manufacturer;

	/** 市场价 */
	private Double basicPrice;

	/** 关税税率 */
	private Double tarrifRate;
	
	/** 关税税率Id */
	private Long tarrifRateId;
	
	/**增值税率*/
	private Long addValueRateId;
	/**消费税率*/
	private Long exciseRateId;
	/**关税率*/
	private Long customsRateId;

	/** 销售税率 */
	private Long saleRate;

	/** 采购税率 */
	private Long purRate;

	/** 运费模板 */
	private Long freightTemplateId;

	/** 是否海淘商品,1-否，2-是，默认1 */
	private Integer wavesSign;

	/** 无理由退货期限 单位 天 */
	private Integer returnDays;

	/** 是否保质期管理:1 是，2-否 默认1 */
	private Integer expSign;
	
	/** 保质期月数 */
	private Integer expDays;

	/** 是否优质商品*/
	private Integer qualityGoods;


	/** 毛重 单位g */
	private Double weight;

	/** 净重 单位g */
	private Double weightNet;

	/** 体积-长度 */
	private Double volumeLength;

	/** 体积-高度 */
	private Double volumeHigh;	

	/** 体积-宽度 */
	private Double volumeWidth;
	/** 备注 */
	private String remark;
	/** 商家库存 */
	private Integer initStock;
	
	
	/***海淘商品产地（国家ID）**/
	private Long countryId;

	
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

	public Double getBasicPrice() {
		return basicPrice;
	}

	public void setBasicPrice(Double basicPrice) {
		this.basicPrice = basicPrice;
	}

	public void setTarrifRate(Double tarrifRate) {
		this.tarrifRate = tarrifRate;
	}
	public Long getSaleRate() {
		return saleRate;
	}

	public void setSaleRate(Long saleRate) {
		this.saleRate = saleRate;
	}

	public Long getPurRate() {
		return purRate;
	}

	public void setPurRate(Long purRate) {
		this.purRate = purRate;
	}

	public Long getFreightTemplateId() {
		return freightTemplateId;
	}

	public void setFreightTemplateId(Long freightTemplateId) {
		this.freightTemplateId = freightTemplateId;
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

	public Integer getQualityGoods() {
		return qualityGoods;
	}

	public void setQualityGoods(Integer qualityGoods) {
		this.qualityGoods = qualityGoods;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public Double getTarrifRate() {
		return tarrifRate;
	}

	public Long getTarrifRateId() {
		return tarrifRateId;
	}

	public void setTarrifRateId(Long tarrifRateId) {
		this.tarrifRateId = tarrifRateId;
	}

	public Double getVolumeLength() {
		return volumeLength;
	}

	public void setVolumeLength(Double volumeLength) {
		this.volumeLength = volumeLength;
	}

	public Double getVolumeHigh() {
		return volumeHigh;
	}

	public void setVolumeHigh(Double volumeHigh) {
		this.volumeHigh = volumeHigh;
	}

	public Double getVolumeWidth() {
		return volumeWidth;
	}

	public void setVolumeWidth(Double volumeWidth) {
		this.volumeWidth = volumeWidth;
	}

	public Integer getInitStock() {
		return initStock;
	}

	public void setInitStock(Integer initStock) {
		this.initStock = initStock;
	}

	public Long getAddValueRateId() {
		return addValueRateId;
	}

	public void setAddValueRateId(Long addValueRateId) {
		this.addValueRateId = addValueRateId;
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
}
