package com.tp.dto.prd;

import java.io.Serializable;

/***
 * 
 * <pre>
 * 	 Sku列表导出
 *   大类名称	中类名称	 小类名称	品牌	单位	SPU	 SPU名称	PRDID	网站显示名称	 SKU	条形码	供应商名称	供应商类型
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class SkuExportDto implements Serializable{
	private static final long serialVersionUID = 1236720876008581788L;
	
	/**商品Id**/
	private Long itemId;
	/**商品prd Id**/
	private Long detailId;
	/**商品sku Id**/
	private Long skuId;
	/**大类名称**/
	private String largeName;
	/**中类名称**/
	private String mediumName;
	/**小类名称**/
	private String smallName;
	/**品牌名称**/
	private String brandName;
	/**单位名称**/
	private String unitName;
	/**SPU编码**/
	private String spu;
	/**SPU名称**/
	private String spuName;
	/**prdid**/
	private String prdid;
	/**网站显示名称**/
	private String mainTitle;
	/**SKU编码**/
	private String sku;
	/**条形码**/
	private String barcode;
	
	/**供应商类别 即saleType*/
	private String saleType;
	
	/**供应商名称**/
	private String spName;
	/**供应商类型**/
	private String spType;
	
	/**商品原价**/
	private String basicPrice;
	
	/**商品售价**/
	private String salePrice;
	
	/**库存**/
	private String inventory;
		
	/**品牌国家Id**/
	private String brandCountryId;
	
	/**品牌国家名**/
	private String brandCountryName;
	
	/**商品产地**/
	private String countryName;
	
	/**商品产地Id**/
	private String countryId;
	
	/**商品状态**/
	private String status;
	
	/**商品供货价**/
	private String supplyPrice;
	
	/**来源于供应商系统导入的供应商Id**/
    private Long  supplierId;
	
	
	public String getSupplyPrice() {
		return supplyPrice;
	}
	public void setSupplyPrice(String supplyPrice) {
		this.supplyPrice = supplyPrice;
	}
	
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the countryId
	 */
	public String getCountryId() {
		return countryId;
	}
	/**
	 * @param countryId the countryId to set
	 */
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	/**
	 * @return the brandCountryId
	 */
	public String getBrandCountryId() {
		return brandCountryId;
	}
	/**
	 * @param brandCountryId the brandCountryId to set
	 */
	public void setBrandCountryId(String brandCountryId) {
		this.brandCountryId = brandCountryId;
	}
	/**
	 * @return the countryName
	 */
	public String getCountryName() {
		return countryName;
	}
	/**
	 * @param countryName the countryName to set
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	/**
	 * @return the basicPrice
	 */
	public String getBasicPrice() {
		return basicPrice;
	}
	/**
	 * @param basicPrice the basicPrice to set
	 */
	public void setBasicPrice(String basicPrice) {
		this.basicPrice = basicPrice;
	}
	/**
	 * @return the salePrice
	 */
	public String getSalePrice() {
		return salePrice;
	}
	/**
	 * @param salePrice the salePrice to set
	 */
	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}
	/**
	 * @return the inventory
	 */
	public String getInventory() {
		return inventory;
	}
	/**
	 * @param inventory the inventory to set
	 */
	public void setInventory(String inventory) {
		this.inventory = inventory;
	}
	/**
	 * @return the brandCountryName
	 */
	public String getBrandCountryName() {
		return brandCountryName;
	}
	/**
	 * @param brandCountryName the brandCountryName to set
	 */
	public void setBrandCountryName(String brandCountryName) {
		this.brandCountryName = brandCountryName;
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
	 * Getter method for property <tt>unitName</tt>.
	 * 
	 * @return property value of unitName
	 */
	public String getUnitName() {
		return unitName;
	}
	/**
	 * Setter method for property <tt>unitName</tt>.
	 * 
	 * @param unitName value to be assigned to property unitName
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	/**
	 * Getter method for property <tt>spu</tt>.
	 * 
	 * @return property value of spu
	 */
	public String getSpu() {
		return spu;
	}
	/**
	 * Setter method for property <tt>spu</tt>.
	 * 
	 * @param spu value to be assigned to property spu
	 */
	public void setSpu(String spu) {
		this.spu = spu;
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
	 * Getter method for property <tt>prdid</tt>.
	 * 
	 * @return property value of prdid
	 */
	public String getPrdid() {
		return prdid;
	}
	/**
	 * Setter method for property <tt>prdid</tt>.
	 * 
	 * @param prdid value to be assigned to property prdid
	 */
	public void setPrdid(String prdid) {
		this.prdid = prdid;
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
	 * Getter method for property <tt>sku</tt>.
	 * 
	 * @return property value of sku
	 */
	public String getSku() {
		return sku;
	}
	/**
	 * Setter method for property <tt>sku</tt>.
	 * 
	 * @param sku value to be assigned to property sku
	 */
	public void setSku(String sku) {
		this.sku = sku;
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
	 * Getter method for property <tt>spName</tt>.
	 * 
	 * @return property value of spName
	 */
	public String getSpName() {
		return spName;
	}
	/**
	 * Setter method for property <tt>spName</tt>.
	 * 
	 * @param spName value to be assigned to property spName
	 */
	public void setSpName(String spName) {
		this.spName = spName;
	}
	
	/**
	 * Getter method for property <tt>spType</tt>.
	 * 
	 * @return property value of spType
	 */
	public String getSpType() {
		return spType;
	}
	/**
	 * Setter method for property <tt>spType</tt>.
	 * 
	 * @param spType value to be assigned to property spType
	 */
	public void setSpType(String spType) {
		this.spType = spType;
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
	 * Getter method for property <tt>saleType</tt>.
	 * 
	 * @return property value of saleType
	 */
	public String getSaleType() {
		return saleType;
	}
	/**
	 * Setter method for property <tt>saleType</tt>.
	 * 
	 * @param saleType value to be assigned to property saleType
	 */
	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}
	
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	
	@Override
	public String toString() {
		return "SkuExportDto [itemId=" + itemId + ", detailId=" + detailId
				+ ", skuId=" + skuId + ", largeName=" + largeName
				+ ", mediumName=" + mediumName + ", smallName=" + smallName
				+ ", brandName=" + brandName + ", unitName=" + unitName
				+ ", spu=" + spu + ", spuName=" + spuName + ", prdid=" + prdid
				+ ", mainTitle=" + mainTitle + ", sku=" + sku + ", barcode="
				+ barcode + ", saleType=" + saleType + ", spName=" + spName
				+ ", spType=" + spType + "]";
	}
}
