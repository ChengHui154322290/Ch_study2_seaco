package com.tp.query.prd;

import java.io.Serializable;
import java.util.Date;

import com.tp.model.BaseDO;
/**
 * prdid纬度信息查询条件
 * @author szy
 * 2015年1月3日 下午9:00:39
 *
 */
public class DetailQuery extends BaseDO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -636433478768987714L;
	/** spu编号 */
	private String spu;
	/** prdid 编号 */
	private String prdid;
	/** 名称 */
	private String name;
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
	/** 商品类型 */
	private Integer itemType;
	/** barcode*/
	private String barcode;
	
	/**查询的商品分类编码*/
	private String categoryCode;
	
	/**状态**/
	private Integer status;
	
	/**spu_name*/
	private String spuName;
	/**商品类型**/
	private Integer wavesSign;
	/**供应商	ID*/
	private Long supplierId;
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Integer getStatus() {
		return status;
	}
	/**
	 * Setter method for property <tt>status</tt>.
	 * 
	 * @param status value to be assigned to property status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * @return the spuName
	 */
	public String getSpuName() {
		return spuName;
	}
	/**
	 * @param spuName the spuName to set
	 */
	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}
	public Integer getWavesSign() {
		return wavesSign;
	}
	public void setWavesSign(Integer wavesSign) {
		this.wavesSign = wavesSign;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	
	
}
