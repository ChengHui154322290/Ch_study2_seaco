/**
 * 
 */
package com.tp.dto.stg.BML;

/**
 * @author szy
 *
 */
public class ProductInfo {
	/** 条形码 */
	private String spuCode;
	/** sku名称*/
	private String itemName;
	/** 商品数量*/
	private String itemCount;
	/** 商品金额*/
	private String itemValue;
	/** 备注*/
	private String remark;
	public String getSpuCode() {
		return spuCode;
	}
	public void setSpuCode(String spuCode) {
		this.spuCode = spuCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemCount() {
		return itemCount;
	}
	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}
	public String getItemValue() {
		return itemValue;
	}
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
