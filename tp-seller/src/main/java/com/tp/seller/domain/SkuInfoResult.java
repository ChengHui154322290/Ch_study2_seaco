package com.tp.seller.domain;

import java.io.Serializable;
import java.util.List;

import com.tp.model.prd.ItemDetailSpec;

/**
 * <pre>
 * 提供SKU查询接口给外部调用
 * 1 供应商
 * </pre>
 *
 * @author qunxi.shao
 * @version $Id: SkuInfoResult.java, v 0.1 2015年1月7日 上午10:48:57 qunxi.shao Exp $
 */
public class SkuInfoResult implements Serializable {

    private static final long serialVersionUID = -1379162674989256083L;

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
    /** 商品名称 */
    private String skuName;

    /** 条形码 冗余字段 */
    private String barcode;

    /** 市场价 */
    private Double basicPrice;

    /** 西客商城价 */
    private Double  xgPrice;

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

    /** 规格 */
    private List<ItemDetailSpec> specList;

    /** 大类ID */
    private Long largeId;

    /** 中类ID */
    private Long mediumId;

    /** 小类ID */
    private Long smallId;

    /** 是否海淘商品,1-否，2-是，默认1 */
    private Integer wavesSign;

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

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Double getBasicPrice() {
		return basicPrice;
	}

	public void setBasicPrice(Double basicPrice) {
		this.basicPrice = basicPrice;
	}

	public Double getXgPrice() {
		return xgPrice;
	}

	public void setXgPrice(Double xgPrice) {
		this.xgPrice = xgPrice;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSaleType() {
		return saleType;
	}

	public void setSaleType(Integer saleType) {
		this.saleType = saleType;
	}

	public String getSpName() {
		return spName;
	}

	public void setSpName(String spName) {
		this.spName = spName;
	}

	public Long getSpId() {
		return spId;
	}

	public void setSpId(Long spId) {
		this.spId = spId;
	}

	public String getSpCode() {
		return spCode;
	}

	public void setSpCode(String spCode) {
		this.spCode = spCode;
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

	public Integer getItemType() {
		return itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
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

	public List<ItemDetailSpec> getSpecList() {
		return specList;
	}

	public void setSpecList(List<ItemDetailSpec> specList) {
		this.specList = specList;
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

	public Integer getWavesSign() {
		return wavesSign;
	}

	public void setWavesSign(Integer wavesSign) {
		this.wavesSign = wavesSign;
	}
    
}
