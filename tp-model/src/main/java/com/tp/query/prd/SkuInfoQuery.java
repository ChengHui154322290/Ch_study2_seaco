package com.tp.query.prd;

import java.io.Serializable;

/**
 * 
 * <pre>
 *	sku信息查询  
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class SkuInfoQuery implements Serializable {

	private static final long serialVersionUID = -5983340979607044646L;
	/**sku编码 */
	private String sku;
	/**供应商id */
	private Long supplierId;
	/**条码  */
	private String barcode;
	/** SEAGOOR("西客商城" ,0),SELLER("商家",1); */
	private Integer  saleType;
	
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
	 * Getter method for property <tt>supplierId</tt>.
	 * 
	 * @return property value of supplierId
	 */
	public Long getSupplierId() {
		return supplierId;
	}
	/**
	 * Setter method for property <tt>supplierId</tt>.
	 * 
	 * @param supplierId value to be assigned to property supplierId
	 */
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
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
	
	public Integer getSaleType() {
		return saleType;
	}
	public void setSaleType(Integer saleType) {
		this.saleType = saleType;
	}
	/** 
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SkuInfoQuery [sku=" + sku + ", supplierId=" + supplierId
				+ ", barcode=" + barcode + "]";
	}
}
