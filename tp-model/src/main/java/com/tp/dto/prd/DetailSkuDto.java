package com.tp.dto.prd;

import java.io.Serializable;

/**
 * 
 * <pre>
 * 	 prdid纬度下的sku信息 
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class DetailSkuDto  implements Serializable {
	
	private static final long serialVersionUID = 1286063723109903513L;

	/** 主键 */
	private Long id;

	/** 商品ID */
	private Long itemId;

	/** 对应商品详情编码 */
	private Long detailId;
	
	/** prdid编码 */
	private String prdid;
	
	/** SPU+3位数流水码 */
	private String sku;
	
	/** 0-未上架 1-上架 2-作废 */
	private Integer status;

	/** 销售类型 0-自营 1-平台 */
	private Integer saleType;
	
	/**供应商类型*/
	private String supplierType;

	/** 供应商名称 */
	private String spName;
	/** 供应商id*/
	private Long spId;

	/** 供应商商品编码 */
	private String spCode;

	/** 市场价 */
	private Double basicPrice;

	/** 条码 */
	private String barcode;
	
	/***排序 */
	private Integer sort	;
	
	private String  skuSupplierList;
	
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

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
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

	public String getSupplierType() {
		return supplierType;
	}

	public void setSupplierType(String supplierType) {
		this.supplierType = supplierType;
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

	public Double getBasicPrice() {
		return basicPrice;
	}

	public void setBasicPrice(Double basicPrice) {
		this.basicPrice = basicPrice;
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
	 * Getter method for property <tt>sort</tt>.
	 * 
	 * @return property value of sort
	 */
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getSkuSupplierList() {
		return skuSupplierList;
	}

	public void setSkuSupplierList(String skuSupplierList) {
		this.skuSupplierList = skuSupplierList;
	}

}
