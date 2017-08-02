/**
 * 
 */
package com.tp.dto.ord;

import java.util.Date;
import java.util.List;

import com.tp.dto.mmp.TopicItemInfoDTO;
/**
 * 购物车行DTO
 * 
 * @author szy
 * @version 0.0.1
 * 
 */
public class CartLineDTO implements BaseDTO {

	private static final long serialVersionUID = -6436223080230518751L;

	private Long id;
	
	/* ------------------------------------------ 商品元信息 ------------------------------------------ */
	/** sku级别的状态，只有item与sku都上架才算上架 */
	private Integer skuStatus;
	/** 商品id **/
	private Long itemId;
	/** 商品编号 */
	private String itemCode;
	/** 商品名称 */
	private String itemName;
	/** 品牌名称 */
	private String brandName;
	/** 商品图片 */
	private String itemPic;
	/**商品SKU 数据类型varchar(20)*/
	private String skuCode;
	/**商品条形码 */
	private String barcode;
	/**活动编号 数据类型bigint(14)*/
	private Long topicId;
	/** area id */
	private Long areaId;
	/** platform id */
	private Integer platformId;
	/** 库存 */
	private Integer stock;
	/** 售价(西客销售价) */
	private Double salePrice;
	/** 吊牌价 */
	private Double listPrice;
	/** 销售属性 */
	private List<SalePropertyDTO> salePropertyList;
	/** 销售模式：1-买断，2-代销，3-平台,4-海淘， 默认1 */
	private Integer saleType;
	/** 供应商id */
	private Long supplierId;
	/**供应商名称*/
	private String supplierName;
	/**供应商别名*/
	private String supplierAlias;
	/** 仓库id */
	private Long warehouseId;
	/** 仓库名称 */
	private String warehouseName;
	
	/**关联的品牌*/
	private Long brandId;
	/**大类ID*/
	private Long largeId;
	/**中类ID*/
	private Long mediumId;
	/**小类ID*/
	private Long smallId;
	/**小类Code*/
	private String categoryCode;
	/**小类名称*/
	private String categoryName;
	
	/** 运费模板ID */
	private Long freightTemplateId;
	/** 净重 */
	private Double weightNet;
	/** 毛重 */
	private Double weight;
	/** 关税税率 */
	private Double tarrifRate;
	
	/** 关税税费 */
	private Double taxfFee;
	
	/**是否海淘商品,1-否，2-是，默认1 */
	private Integer wavesSign ;
	
	/** 仓库类型*/
	private Integer storageType;
	
	/** 海淘渠道 */
	private Long seaChannel;
	/** 海淘渠道名称 */
	private String seaChannelName;
	
	/** 单位*/
	private String  unit;	
	
	/** 退货限制天数 */
	private Integer refundDays;
	
	/** 货号 */
	private String productCode;

	/**
	 * 销售模式
	 */
	private Integer salesPattern;

	public Integer getSalesPattern() {
		return salesPattern;
	}

	public void setSalesPattern(Integer salesPattern) {
		this.salesPattern = salesPattern;
	}

	/* ------------------------------------------ 购物车行信息 ------------------------------------------ */
	/**购买商品数量 数据类型int(5)*/
	private Integer quantity;
	/**会员ID 数据类型bigint(14)*/
	private Long memberId;
	/** 加入购物车时间 */
	private Date createTime;
	/** 是否选中 */
	private Boolean selected = true;
	/** 行小计 */
	private Double subTotal;
	/** 折扣行小计 */
	private Double realSubTotal;
	/** 商品状态  "0"-有效 "1"-库存不足 "2"-已下架 */
	private Integer status;
	
	/**增值税率*/
	private Double addedValueRate;
	/**消费税率*/
	private Double exciseRate;
	/**关税率*/
	private Double customsRate;
	
	/* ------------------------------------------ 促销信息 ------------------------------------------ */
	/** 促销信息DTO */
	private TopicItemInfoDTO topicItemInfo;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemPic() {
		return itemPic;
	}
	public void setItemPic(String itemPic) {
		this.itemPic = itemPic;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public Long getTopicId() {
		return topicId;
	}
	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	public Integer getPlatformId() {
		return platformId;
	}
	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	public Double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}
	public Double getListPrice() {
		return listPrice;
	}
	public void setListPrice(Double listPrice) {
		this.listPrice = listPrice;
	}
	public List<SalePropertyDTO> getSalePropertyList() {
		return salePropertyList;
	}
	public void setSalePropertyList(List<SalePropertyDTO> salePropertyList) {
		this.salePropertyList = salePropertyList;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierName() {
        return supplierName;
    }
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
    public Long getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}
	
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	public Double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}
	public Integer getSaleType() {
		return saleType;
	}
	public void setSaleType(Integer saleType) {
		this.saleType = saleType;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Double getRealSubTotal() {
		return realSubTotal;
	}
	public void setRealSubTotal(Double realSubTotal) {
		this.realSubTotal = realSubTotal;
	}
	public TopicItemInfoDTO getTopicItemInfo() {
		return topicItemInfo;
	}
	public void setTopicItemInfo(TopicItemInfoDTO topicItemInfo) {
		this.topicItemInfo = topicItemInfo;
	}
	public Integer getSkuStatus() {
		return skuStatus;
	}
	public void setSkuStatus(Integer skuStatus) {
		this.skuStatus = skuStatus;
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
	public Long getFreightTemplateId() {
		return freightTemplateId;
	}
	public void setFreightTemplateId(Long freightTemplateId) {
		this.freightTemplateId = freightTemplateId;
	}
	public Double getWeightNet() {
		return weightNet;
	}
	public void setWeightNet(Double weightNet) {
		this.weightNet = weightNet;
	}
	public Double getTarrifRate() {
		return tarrifRate;
	}
	public void setTarrifRate(Double tarrifRate) {
		this.tarrifRate = tarrifRate;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Integer getWavesSign() {
		return wavesSign;
	}
	public void setWavesSign(Integer wavesSign) {
		this.wavesSign = wavesSign;
	}
	public Long getSeaChannel() {
		return seaChannel;
	}
	public void setSeaChannel(Long seaChannel) {
		this.seaChannel = seaChannel;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Integer getRefundDays() {
		return refundDays;
	}
	public void setRefundDays(Integer refundDays) {
		this.refundDays = refundDays;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public Double getTaxfFee() {
		return taxfFee; 
	}
	public void setTaxfFee(Double taxfFee) {
		this.taxfFee = taxfFee;
	}
	public String getSeaChannelName() {
		return seaChannelName;
	}
	public void setSeaChannelName(String seaChannelName) {
		this.seaChannelName = seaChannelName;
	}
	public Integer getStorageType() {
		return storageType;
	}
	public void setStorageType(Integer storageType) {
		this.storageType = storageType;
	}

	public String getSupplierAlias() {
		return supplierAlias;
	}
	public void setSupplierAlias(String supplierAlias) {
		this.supplierAlias = supplierAlias;
	}

	public Double getAddedValueRate() {
		if(null==addedValueRate){return 0.00d;}
		return addedValueRate;
	}

	public void setAddedValueRate(Double addedValueRate) {
		this.addedValueRate = addedValueRate;
	}

	public Double getExciseRate() {
		if(null==exciseRate){return 0.00d;}
		return exciseRate;
	}

	public void setExciseRate(Double exciseRate) {
		this.exciseRate = exciseRate;
	}

	public Double getCustomsRate() {
		if(null==customsRate){return 0.00d;}
		return customsRate;
	}

	public void setCustomsRate(Double customsRate) {
		this.customsRate = customsRate;
	}
}
