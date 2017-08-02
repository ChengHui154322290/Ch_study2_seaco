package com.tp.dto.ord;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 订购商品行信息
 *
 * @author szy
 */
public class OrderItemLineDTO implements Serializable {

    public static final String SUB_ORDER_TAG_COMMON = "common_item";

    public static final String SPLIT_TAG = "_";

    private static final long serialVersionUID = 6181049201229752930L;

    // 子订单标记
    private String subOrderTag;

    /** 数量 */
    private Integer quantity;

    /** 加入购物车时间 */
    private Date inCartTime;

    /** 行小计 */
    private Double subTotal = 0.00;

    /** 折扣行小计 */
    private Double realSubTotal = 0.00;

    // ------------ 购物商品行信息--------商品信息--------------
    /** 商品id **/
    private Long itemId;

    /** 商品编号 */
    private String itemCode;

    /** 商品名称 */
    private String itemName;

    /** 关联的品牌 */
    private Long brandId;

    /** 品牌名称 */
    private String brandName;

    /** 商品图片 */
    private String itemPic;

    /** sku code */
    private String skuCode;

    /** 商品条形码 */
    private String barcode;

    /** 单位 */
    private String unit;

    /** 货号 */
    private String productCode;

    /** topic id */
    private Long topicId;

    /** topic name */
    private String topicName;

    /** area id */
    private Long areaId;

    /** platform id */
    private Integer platformId;

    /** 售价(西客销售价) */
    private Double salePrice = 0.00;

    /** 吊牌价 */
    private Double listPrice = 0.00;

    /** 销售属性 */
    private List<SalePropertyDTO> salePropertyList;

    /** 销售模式 */
    private Integer saleType;

    /** 供应商id */
    private Long supplierId;

    /** 供应商名称 */
    private String supplierName;

    /** 供应商别名 */
    private String supplierAlias;

    /** 仓库类型 */
    private Integer storageType;

    /** 仓库id */
    private Long warehouseId;

    /** 仓库名称 */
    private String warehouseName;

    /** 大类ID */
    private Long largeId;

    /** 中类ID */
    private Long mediumId;

    /** 小类ID */
    private Long smallId;

    /** 小类Code */
    private String categoryCode;

    /** 小类名称 */
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
    private Double taxfFee = 0.00;

    /** 原始税费 */
    private Double originalTaxfFee = 0.00;

    /** 是否海淘商品,HitaoOrderEnum.IS_HITAO_ITEM枚举值 */
    private Integer wavesSign;

    /** 海淘渠道 */
    private Long seaChannelId;

    /** 海淘渠道编号 */
    private String seaChannelCode;

    /** 海淘渠道名称 */
    private String seaChannelName;

    /** 行邮税号 */
    private String parcelTaxId;

    /** 退货限制天数 */
    private Integer refundDays;

    // --------------------- 促销信息--------------------
    /** 团购号 */
    private Long groupId = 0L;

    /** 所属的满减ID */
    private Long fullDiscountId;

    /** 满减使用金额 */
    private Double fullDiscountUse = 0.00;

    /** 折扣价 */
    private Double postDiscount;

    /** 优惠券使用金额 */
    private Double couponUse = 0.00;

    /** 红包使用金额 */
    private Double redPacketUse = 0.00;

    /** 实际支付运费 */
    private Double actualFreight = 0.00;
    
    /**
     * 原始的运费
     */
    private Double originalFreight = 0.00;
    
    /**
     * 优惠券和红包id
     */
    private Set<Long> couponIdList;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

    public Date getInCartTime() {
        return inCartTime;
    }

    public void setInCartTime(final Date inCartTime) {
        this.inCartTime = inCartTime;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(final Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getRealSubTotal() {
        return realSubTotal;
    }

    public void setRealSubTotal(final Double realSubTotal) {
        this.realSubTotal = realSubTotal;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(final Long itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(final String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(final String itemName) {
        this.itemName = itemName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(final String brandName) {
        this.brandName = brandName;
    }

    public String getItemPic() {
        return itemPic;
    }

    public void setItemPic(final String itemPic) {
        this.itemPic = itemPic;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(final String skuCode) {
        this.skuCode = skuCode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(final String barcode) {
        this.barcode = barcode;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(final String unit) {
        this.unit = unit;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(final String productCode) {
        this.productCode = productCode;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(final Long topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(final String topicName) {
        this.topicName = topicName;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(final Long areaId) {
        this.areaId = areaId;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(final Integer platformId) {
        this.platformId = platformId;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(final Double salePrice) {
        this.salePrice = salePrice;
    }

    public Double getListPrice() {
        return listPrice;
    }

    public void setListPrice(final Double listPrice) {
        this.listPrice = listPrice;
    }

    public List<SalePropertyDTO> getSalePropertyList() {
        return salePropertyList;
    }

    public void setSalePropertyList(final List<SalePropertyDTO> salePropertyList) {
        this.salePropertyList = salePropertyList;
    }

    public Integer getSaleType() {
        return saleType;
    }

    public void setSaleType(final Integer saleType) {
        this.saleType = saleType;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(final Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(final String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierAlias() {
        return supplierAlias;
    }

    public void setSupplierAlias(final String supplierAlias) {
        this.supplierAlias = supplierAlias;
    }

    public Integer getStorageType() {
        return storageType;
    }

    public void setStorageType(final Integer storageType) {
        this.storageType = storageType;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(final Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(final String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(final Long brandId) {
        this.brandId = brandId;
    }

    public Long getLargeId() {
        return largeId;
    }

    public void setLargeId(final Long largeId) {
        this.largeId = largeId;
    }

    public Long getMediumId() {
        return mediumId;
    }

    public void setMediumId(final Long mediumId) {
        this.mediumId = mediumId;
    }

    public Long getSmallId() {
        return smallId;
    }

    public void setSmallId(final Long smallId) {
        this.smallId = smallId;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(final String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(final String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getFreightTemplateId() {
        return freightTemplateId;
    }

    public void setFreightTemplateId(final Long freightTemplateId) {
        this.freightTemplateId = freightTemplateId;
    }

    public Double getWeightNet() {
        return weightNet;
    }

    public void setWeightNet(final Double weightNet) {
        this.weightNet = weightNet;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(final Double weight) {
        this.weight = weight;
    }

    public Double getTarrifRate() {
        return tarrifRate;
    }

    public void setTarrifRate(final Double tarrifRate) {
        this.tarrifRate = tarrifRate;
    }

    public Double getTaxfFee() {
        return taxfFee;
    }

    public void setTaxfFee(final Double taxfFee) {
        this.taxfFee = taxfFee;
    }

    public Integer getWavesSign() {
        return wavesSign;
    }

    public void setWavesSign(final Integer wavesSign) {
        this.wavesSign = wavesSign;
    }

    public Long getSeaChannelId() {
        return seaChannelId;
    }

    public void setSeaChannelId(final Long seaChannelId) {
        this.seaChannelId = seaChannelId;
    }

    public String getSeaChannelCode() {
        return seaChannelCode;
    }

    public void setSeaChannelCode(final String seaChannelCode) {
        this.seaChannelCode = seaChannelCode;
    }

    public String getSeaChannelName() {
        return seaChannelName;
    }

    public void setSeaChannelName(final String seaChannelName) {
        this.seaChannelName = seaChannelName;
    }

    public String getParcelTaxId() {
        return parcelTaxId;
    }

    public void setParcelTaxId(final String parcelTaxId) {
        this.parcelTaxId = parcelTaxId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(final Long groupId) {
        this.groupId = groupId;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Long getFullDiscountId() {
        return fullDiscountId;
    }

    public void setFullDiscountId(final Long fullDiscountId) {
        this.fullDiscountId = fullDiscountId;
    }

    public Integer getRefundDays() {
        return refundDays;
    }

    public void setRefundDays(final Integer refundDays) {
        this.refundDays = refundDays;
    }

    public Double getPostDiscount() {
        return postDiscount;
    }

    public void setPostDiscount(final Double postDiscount) {
        this.postDiscount = postDiscount;
    }

    public String getSubOrderTag() {
        return subOrderTag;
    }

    public void setSubOrderTag(final String subOrderTag) {
        this.subOrderTag = subOrderTag;
    }

    public void setCusSubOrderTag(final String cus) {
        this.subOrderTag = getSubOrderTagDefault() + SPLIT_TAG + cus;
    }

    /**
     * 获取默认的所属子订单标记
     *
     * @return
     */
    public String getSubOrderTagDefault() {
        return getWavesSign() + SPLIT_TAG + getSupplierId() + SPLIT_TAG + getWarehouseId();
    }

    public Double getCouponUse() {
        return couponUse;
    }

    public void setCouponUse(final Double couponUse) {
        this.couponUse = couponUse;
    }

    public Double getRedPacketUse() {
        return redPacketUse;
    }

    public void setRedPacketUse(final Double redPacketUse) {
        this.redPacketUse = redPacketUse;
    }

    public Double getOriginalTaxfFee() {
        return originalTaxfFee;
    }

    public void setOriginalTaxfFee(final Double originalTaxfFee) {
        this.originalTaxfFee = originalTaxfFee;
    }

    public Double getFullDiscountUse() {
        return fullDiscountUse;
    }

    public void setFullDiscountUse(final Double fullDiscountUse) {
        this.fullDiscountUse = fullDiscountUse;
    }

    public Double getActualFreight() {
        return actualFreight;
    }

    public void setActualFreight(final Double actualFreight) {
        this.actualFreight = actualFreight;
    }

	public Double getOriginalFreight() {
		return originalFreight;
	}

	public void setOriginalFreight(Double originalFreight) {
		this.originalFreight = originalFreight;
	}

	public Set<Long> getCouponIdList() {
		return couponIdList;
	}

	public void setCouponIdList(Set<Long> couponIdList) {
		this.couponIdList = couponIdList;
	}

}
