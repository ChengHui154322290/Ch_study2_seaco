/**
 * 
 */
package com.tp.dto.stg.BML;

/**
 * @author szy
 * 出库单商品信息
 */
public class RequestOrderItem {
	/** sku*/
	private String spuCode;
	/** 商品名称*/
	private String itemName;
	/** 供应商*/
	private String vender;
	/** 商品数量*/
	private String itemCount;
	/** 商品金额*/
	private String itemValue;
	
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
	public String getVender() {
		return vender;
	}
	public void setVender(String vender) {
		this.vender = vender;
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
	
}
