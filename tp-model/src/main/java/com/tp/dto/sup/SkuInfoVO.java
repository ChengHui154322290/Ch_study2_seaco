package com.tp.dto.sup;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <pre>
 * 商品基本信息
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class SkuInfoVO implements Serializable{
    /**
     * <pre>
     *
     * </pre>
     */
    private static final long serialVersionUID = 3515927359950380039L;

    /** 商品ID */
    private Long itemId;

    /** 对应商品详情编码 */
    private Long detailId;

    /** spu编码 冗余字段 */
    private String spu;

    /** prdid编码 */
    private String prdid;

    /** SPU+3位数流水码 */
    private String sku;

    /** skuId */
    private Long skuId;

    /** 商品名称 */
    private String skuName;

    /** 条形码 冗余字段 */
    private String barcode;

    /** 市场价 */
    private Double basicPrice;

    /** 西客商城价 */
    private Double xgPrice;

    /** 库存 */
    private Long quantity;

    /** 0-未上架 1-上架 2-作废 */
    private Integer status;

    /** 销售类型 0-自营 1-平台 */
    private Integer saleType;

    /** 供应商名称 */
    private String spName;
    /** 供应商id */
    private Long spId;

    /** 供应商商品编码 */
    private String spCode;

    /** 大类ID */
    private Long largeId;

    /** 中类ID */
    private Long mediumId;

    /** 小类ID */
    private Long smallId;

    /** 单位id 冗余字段 */
    private Long unitId;
    /** 品牌id 冗余字段 */
    private Long brandId;
    /** 商品类型 冗余字段 1-正常商品，2-服务商品，3-二手商品,4-报废商品 默认1 */
    private Integer itemType;
    /** 商品所属三级类别code 冗余字段 */
    private String categoryCode;
    /** 规格 */
    private String specifications;
    /** 箱规 */
    private String cartonSpec;

    /** 大类id */
    private Long bigCatId;

    /** 大类名称 */
    private String bigCatName;

    /** 中类id */
    private Long midCatId;

    /** 中类名称 */
    private String midCatName;

    /** 小类id */
    private Long smallCatId;

    /** 小类名称 */
    private String smallCatName;

    /** 品牌名称 */
    private String brandName;

    /** 规格属性1 */
    private String propValue1;

    /** 规格属性2 */
    private String propValue2;

    /** 规格属性3 */
    private String propValue3;

    /** 单位名称 */
    private String unitName;

    /** 市场价 */
    private BigDecimal marketPrice;

    /** 标准价 ： 默认从采购订单中取 */
    private BigDecimal starndardPrice;

    /** 供货价格 */
    private BigDecimal supplyPrice;

    /** 佣金比例 */
    private BigDecimal commissionPer;

    /** 粘贴报价单 1：成功 0 失败 */
    private int pasteSuccessOrFail;

    public int getPasteIndex() {
        return pasteIndex;
    }

    public void setPasteIndex(final int pasteIndex) {
        this.pasteIndex = pasteIndex;
    }

    /** 粘贴所在行 */
    private int pasteIndex;

    public int getPasteSuccessOrFail() {
        return pasteSuccessOrFail;
    }

    public void setPasteSuccessOrFail(final int pasteInfo) {
        this.pasteSuccessOrFail = pasteInfo;
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

    public BigDecimal getSupplyPrice() {
        return supplyPrice;
    }

    public void setSupplyPrice(final BigDecimal supplyPrice) {
        this.supplyPrice = supplyPrice;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(final Long itemId) {
        this.itemId = itemId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(final Long detailId) {
        this.detailId = detailId;
    }

    public String getSpu() {
        return spu;
    }

    public void setSpu(final String spu) {
        this.spu = spu;
    }

    public String getPrdid() {
        return prdid;
    }

    public void setPrdid(final String prdid) {
        this.prdid = prdid;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(final String sku) {
        this.sku = sku;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(final String skuName) {
        this.skuName = skuName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(final String barcode) {
        this.barcode = barcode;
    }

    public Double getBasicPrice() {
        return basicPrice;
    }

    public void setBasicPrice(final Double basicPrice) {
        this.basicPrice = basicPrice;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(final Long quantity) {
        this.quantity = quantity;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(final Integer status) {
        this.status = status;
    }

    public Integer getSaleType() {
        return saleType;
    }

    public void setSaleType(final Integer saleType) {
        this.saleType = saleType;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(final String spName) {
        this.spName = spName;
    }

    public Long getSpId() {
        return spId;
    }

    public void setSpId(final Long spId) {
        this.spId = spId;
    }

    public String getSpCode() {
        return spCode;
    }

    public void setSpCode(final String spCode) {
        this.spCode = spCode;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(final Long unitId) {
        this.unitId = unitId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(final Long brandId) {
        this.brandId = brandId;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(final Integer itemType) {
        this.itemType = itemType;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(final String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(final String specifications) {
        this.specifications = specifications;
    }

    public String getCartonSpec() {
        return cartonSpec;
    }

    public void setCartonSpec(final String cartonSpec) {
        this.cartonSpec = cartonSpec;
    }

    public Long getBigCatId() {
        return bigCatId;
    }

    public void setBigCatId(final Long bigCatId) {
        this.bigCatId = bigCatId;
    }

    public String getBigCatName() {
        return bigCatName;
    }

    public void setBigCatName(final String bigCatName) {
        this.bigCatName = bigCatName;
    }

    public Long getMidCatId() {
        return midCatId;
    }

    public void setMidCatId(final Long midCatId) {
        this.midCatId = midCatId;
    }

    public String getMidCatName() {
        return midCatName;
    }

    public void setMidCatName(final String midCatName) {
        this.midCatName = midCatName;
    }

    public Long getSmallCatId() {
        return smallCatId;
    }

    public void setSmallCatId(final Long smallCatId) {
        this.smallCatId = smallCatId;
    }

    public String getSmallCatName() {
        return smallCatName;
    }

    public void setSmallCatName(final String smallCatName) {
        this.smallCatName = smallCatName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(final String brandName) {
        this.brandName = brandName;
    }

    public String getPropValue1() {
        return propValue1;
    }

    public void setPropValue1(final String propValue1) {
        this.propValue1 = propValue1;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(final String unitName) {
        this.unitName = unitName;
    }

    public String getPropValue2() {
        return propValue2;
    }

    public void setPropValue2(final String propValue2) {
        this.propValue2 = propValue2;
    }

    public String getPropValue3() {
        return propValue3;
    }

    public void setPropValue3(final String propValue3) {
        this.propValue3 = propValue3;
    }

    public BigDecimal getStarndardPrice() {
        return starndardPrice;
    }

    public void setStarndardPrice(final BigDecimal starndardPrice) {
        this.starndardPrice = starndardPrice;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(final Long skuId) {
        this.skuId = skuId;
    }

    public BigDecimal getCommissionPer() {
        return commissionPer;
    }

    public void setCommissionPer(final BigDecimal commissionPer) {
        this.commissionPer = commissionPer;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(final BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

	public Double getXgPrice() {
		return xgPrice;
	}

	public void setXgPrice(Double xgPrice) {
		this.xgPrice = xgPrice;
	}

}
