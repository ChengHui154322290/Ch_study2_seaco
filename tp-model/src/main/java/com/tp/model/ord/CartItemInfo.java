package com.tp.model.ord;

import java.util.List;

import com.tp.dto.common.FailInfo;
import com.tp.dto.mmp.TopicItemInfoDTO;
import com.tp.dto.ord.SalePropertyDTO;
import com.tp.model.mmp.FreightTemplate;
import com.tp.model.mmp.TopicItem;
/**
  * @author szy
  * 购物车商品信息表
  */
public class CartItemInfo extends CartItem{

	private static final long serialVersionUID = 1453717135403L;
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
	/**商品类型：1-正常商品，2-服务商品，3-二手商品,默认1*/
	private Integer itemType;
	/**商品条形码 */
	private String barcode;

	/** area id */
	private Long areaId;
	/** platform id */
	private Integer platformId;
	/** 库存 */
	private Integer stock;
	/** 售价(西狗销售价) */
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
	/** 仓库地址*/
	private String warehouseAddress;
	/** 仓库经纬度*/
	private String lngLat;
	
	/** 主仓库ID:实际货物所在仓，用于拆单 */
	private Long mainWarehouseId;
	/** 主仓库名称 */
	private String mainWarehouseName;
	/** 主仓库所属供应商 */
	private Long mainWhSupplierId;
	/** 主仓库所属供应商名称 */
	private String mainWhSupplierName;
	/** 主仓库所属供应商简称 */
	private String mainWhSupplierAlias;
	
	
	
	/** 仓库类型*/
	private Integer whType;
	/** 推送标识*/
	private Integer putSign;
	/**配送地区  以,分割*/
	private String deliverAddr;
	
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
	/**行邮税率 */
	private Double tarrifRate;
	/**增值税率*/
	private Double addedValueRate;
	/**消费税率*/
	private Double exciseRate;
	/**关税率*/
	private Double customsRate;
	
	/** 税费 */
	private Double taxfFee;
	
	/**分销提佣比率*/
	private Double commisionRate;
	
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
	/** 行小计 */
	private Double subTotal;
	/** 折扣行小计 */
	private Double realSubTotal;
	/** 商品状态  "0"-有效 "1"-库存不足 "2"-已下架 */
	private Integer status;
	
	//新增
	/** 单位ID **/
	private Long unitId; 
	
	/** 产地ID **/
	private Long countryId;
	
	/** 净数量 **/
	private Integer unitQuantity;
	
	/** 独立包装数量 */
	private Integer wrapQuantity;
	
	/** 商品成本价 */
	private Double costPrice;
	
	/* ------------------------------------------ 促销信息 ------------------------------------------ */
	/** 促销信息DTO */
	private TopicItemInfoDTO topicItemInfo;
	private Integer topicInventoryFlag; //活动是否预占库存：0否1是
	/**商品类型名称*/
	private Integer skuType;
	private Integer disabled;
	private String token;
	private FailInfo failInfo;
	private OrderItem orderItem;
	private TopicItem topicItem;
	private Double freight = 0d;
	private FreightTemplate freightTemplate;
	private String ip;
	private Long groupId;
	private Long shopPromoterId;
	private String channelCode;
	public transient Double couponOrgTotalAmount;

	public Integer getTopicInventoryFlag() {
		return topicInventoryFlag;
	}
	public void setTopicInventoryFlag(Integer topicInventoryFlag) {
		this.topicInventoryFlag = topicInventoryFlag;
	}
	public Integer getSkuStatus() {
		return skuStatus;
	}
	public void setSkuStatus(Integer skuStatus) {
		this.skuStatus = skuStatus;
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
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getItemPic() {
		return itemPic;
	}
	public void setItemPic(String itemPic) {
		this.itemPic = itemPic;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
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
	public Integer getSaleType() {
		return saleType;
	}
	public void setSaleType(Integer saleType) {
		this.saleType = saleType;
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
	public String getSupplierAlias() {
		return supplierAlias;
	}
	public void setSupplierAlias(String supplierAlias) {
		this.supplierAlias = supplierAlias;
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
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Double getTarrifRate() {
		return tarrifRate;
	}
	public void setTarrifRate(Double tarrifRate) {
		this.tarrifRate = tarrifRate;
	}
	public Double getTaxfFee() {
		return taxfFee;
	}
	public void setTaxfFee(Double taxfFee) {
		this.taxfFee = taxfFee;
	}
	public Integer getWavesSign() {
		return wavesSign;
	}
	public void setWavesSign(Integer wavesSign) {
		this.wavesSign = wavesSign;
	}
	public Integer getStorageType() {
		return storageType;
	}
	public void setStorageType(Integer storageType) {
		this.storageType = storageType;
	}
	public Long getSeaChannel() {
		return seaChannel;
	}
	public void setSeaChannel(Long seaChannel) {
		this.seaChannel = seaChannel;
	}
	public String getSeaChannelName() {
		return seaChannelName;
	}
	public void setSeaChannelName(String seaChannelName) {
		this.seaChannelName = seaChannelName;
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
	public Double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}
	public Double getRealSubTotal() {
		return realSubTotal;
	}
	public void setRealSubTotal(Double realSubTotal) {
		this.realSubTotal = realSubTotal;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public TopicItemInfoDTO getTopicItemInfo() {
		return topicItemInfo;
	}
	public void setTopicItemInfo(TopicItemInfoDTO topicItemInfo) {
		this.topicItemInfo = topicItemInfo;
	}
	public Integer getSkuType() {
		return skuType;
	}
	public void setSkuType(Integer skuType) {
		this.skuType = skuType;
	}
	public Integer getDisabled() {
		return disabled;
	}
	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public FailInfo getFailInfo() {
		return failInfo;
	}
	public void setFailInfo(FailInfo failInfo) {
		this.failInfo = failInfo;
	}
	public OrderItem getOrderItem() {
		return orderItem;
	}
	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}
	public TopicItem getTopicItem() {
		return topicItem;
	}
	public void setTopicItem(TopicItem topicItem) {
		this.topicItem = topicItem;
	}
	public Double getFreight() {
		return freight;
	}
	public void setFreight(Double freight) {
		this.freight = freight;
	}
	public FreightTemplate getFreightTemplate() {
		return freightTemplate;
	}
	public void setFreightTemplate(FreightTemplate freightTemplate) {
		this.freightTemplate = freightTemplate;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
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
	public Integer getWhType() {
		return whType;
	}
	public void setWhType(Integer whType) {
		this.whType = whType;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public Long getShopPromoterId() {
		return shopPromoterId;
	}
	public void setShopPromoterId(Long shopPromoterId) {
		this.shopPromoterId = shopPromoterId;
	}
	public Double getCommisionRate() {
		return commisionRate;
	}
	public void setCommisionRate(Double commisionRate) {
		this.commisionRate = commisionRate;
	}
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	public Long getCountryId() {
		return countryId;
	}
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
	public Integer getUnitQuantity() {
		return unitQuantity;
	}
	public void setUnitQuantity(Integer unitQuantity) {
		this.unitQuantity = unitQuantity;
	}
	public Integer getPutSign() {
		return putSign;
	}
	public void setPutSign(Integer putSign) {
		this.putSign = putSign;
	}
	public Integer getWrapQuantity() {
		return wrapQuantity;
	}
	public void setWrapQuantity(Integer wrapQuantity) {
		this.wrapQuantity = wrapQuantity;
	}
	public String getDeliverAddr() {
		return deliverAddr;
	}
	public void setDeliverAddr(String deliverAddr) {
		this.deliverAddr = deliverAddr;
	}
	public Integer getItemType() {
		return itemType;
	}
	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}
	public Long getMainWarehouseId() {
		return mainWarehouseId;
	}
	public void setMainWarehouseId(Long mainWarehouseId) {
		this.mainWarehouseId = mainWarehouseId;
	}
	public String getMainWarehouseName() {
		return mainWarehouseName;
	}
	public void setMainWarehouseName(String mainWarehouseName) {
		this.mainWarehouseName = mainWarehouseName;
	}
	public Long getMainWhSupplierId() {
		return mainWhSupplierId;
	}
	public void setMainWhSupplierId(Long mainWhSupplierId) {
		this.mainWhSupplierId = mainWhSupplierId;
	}
	public String getMainWhSupplierName() {
		return mainWhSupplierName;
	}
	public void setMainWhSupplierName(String mainWhSupplierName) {
		this.mainWhSupplierName = mainWhSupplierName;
	}
	public String getMainWhSupplierAlias() {
		return mainWhSupplierAlias;
	}
	public void setMainWhSupplierAlias(String mainWhSupplierAlias) {
		this.mainWhSupplierAlias = mainWhSupplierAlias;
	}
	public String getWarehouseAddress() {
		return warehouseAddress;
	}
	public void setWarehouseAddress(String warehouseAddress) {
		this.warehouseAddress = warehouseAddress;
	}
	public String getLngLat() {
		return lngLat;
	}
	public void setLngLat(String lngLat) {
		this.lngLat = lngLat;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public Double getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}
	
}
