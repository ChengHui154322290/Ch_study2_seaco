package com.tp.query.prd;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tp.model.BaseDO;

/**
 * 
 * <pre>
 * 		提供给外部系统调用的商品参数类
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class ItemQuery  extends BaseDO implements Serializable{
	
	private static final long serialVersionUID = -7910906221113682464L;
	/**商品ID*/
	private Long itemId ;
	/** 商品skuId*/
	private Long skuId;
	/** spu编号 */
	private String spu;
	/** prdid 编号 */
	private String prdid;
	/** sku编号 */
	private String sku;
	/** 名称 */
	private String name;
	/** 上下架状态 */
	private Integer status;
	/** 条码查询 */
	private String barcode;
	
	/** 创建起始时间 */
	private Date createBeginTime;
	/** 创建查询截止时间 */
	private Date createEndTime;
	/** 单位id */
	private Long unitId;
	/** 品牌名称 */
	private String brandName;
	/** 品牌id列表 */
	private Long brandId;
	/** 销售类型 */
	private Integer saleType;
	/** 商品类型 */
	private Integer itemType;
	
	/** 商品大类**/
	private Long largeId;
	/** 商品中类**/
	private Long mediumId;
	/** 商品小类**/
	private Long smallId;
	
	/**查询的商品分类编码*/
	private String categoryCode;
	
	/**供应商**/
	private Long supplierId;
	
	/**spu_name*/
	private String spuName;
	/**查询来源   1：来源于供应商系统查询  其他：backend查询*/
	private String SearchFrom;
	
	/**商品类型**/
	private Integer wavesSign;
	
	/** 备案编号 */
	private String articleNumber;
	
	private List<String> skuCodeList;
	
	private String bindLevel;
	public String getBindLevel() {
		return bindLevel;
	}
	public void setBindLevel(String bindLevel) {
		this.bindLevel = bindLevel;
	}
	public Long getMajorDetailId() {
		return majorDetailId;
	}
	public void setMajorDetailId(Long majorDetailId) {
		this.majorDetailId = majorDetailId;
	}
	public String getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	private Long majorDetailId;
	private String auditStatus;

	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreateBeginTime() {
		return createBeginTime;
	}
	public void setCreateBeginTime(Date createBeginTime) {
		this.createBeginTime = createBeginTime;
	}
	public Date getCreateEndTime() {
		return createEndTime;
	}
	public void setCreateEndTime(Date createEndTime) {
		this.createEndTime = createEndTime;
	}
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	public String getBrandName() {
		return brandName;
	}
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
	public Integer getSaleType() {
		return saleType;
	}
	public void setSaleType(Integer saleType) {
		this.saleType = saleType;
	}
	public Integer getItemType() {
		return itemType;
	}
	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	/**
	 * Getter method for property <tt>largeId</tt>.
	 * 
	 * @return property value of largeId
	 */
	public Long getLargeId() {
		return largeId;
	}
	/**
	 * Setter method for property <tt>largeId</tt>.
	 * 
	 * @param largeId value to be assigned to property largeId
	 */
	public void setLargeId(Long largeId) {
		this.largeId = largeId;
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
	 * @return the supplierId
	 */
	public Long getSupplierId() {
		return supplierId;
	}
	/**
	 * @param supplierId the supplierId to set
	 */
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public String getSpuName() {
		return spuName;
	}
	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}
	public Integer getWavesSign() {
		return wavesSign;
	}
	public void setWavesSign(Integer wavesSign) {
		this.wavesSign = wavesSign;
	}
	public String getSearchFrom() {
		return SearchFrom;
	}
	public void setSearchFrom(String searchFrom) {
		SearchFrom = searchFrom;
	}
	
	
	public String getArticleNumber() {
		return articleNumber;
	}
	public void setArticleNumber(String articleNumber) {
		this.articleNumber = articleNumber;
	}
	public List<String> getSkuCodeList() {
		return skuCodeList;
	}
	public void setSkuCodeList(List<String> skuCodeList) {
		this.skuCodeList = skuCodeList;
	}
}
