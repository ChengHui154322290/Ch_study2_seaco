/**
 * 
 */
package com.tp.dto.wms.sto;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class STOStocksyncInfoItemDto implements Serializable{

	private static final long serialVersionUID = -7021723974842861703L;

	/** 仓库商品SKU */
	private String itemsku;
	
	/** 仓库商品名称 */
	private String itemName;
	
	/** 库存 */
	private Integer surplusInventory;

	public String getItemsku() {
		return itemsku;
	}

	public void setItemsku(String itemsku) {
		this.itemsku = itemsku;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getSurplusInventory() {
		return surplusInventory;
	}

	public void setSurplusInventory(Integer surplusInventory) {
		this.surplusInventory = surplusInventory;
	}
}
