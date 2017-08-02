/**
 * 
 */
package com.tp.dto.stg;

import java.io.Serializable;

/**
 * @author szy
 * 入库商品明细
 */
public class InputOrderDetailDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3973800463399745358L;

	/** 即sku 必传*/
	private String sku;
	
	/** 商品名称 必传 */
	private String itemName;
	
	/** 商品数量 必传 */
	private Integer itemCount;
	
	/** 条形码  */
	private String barcode;
	
	/** 商品金额 */
	private Double itemValue;
	
	/** 备注 */
	private String remark;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getItemCount() {
		return itemCount;
	}

	public void setItemCount(Integer itemCount) {
		this.itemCount = itemCount;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Double getItemValue() {
		return itemValue;
	}

	public void setItemValue(Double itemValue) {
		this.itemValue = itemValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
