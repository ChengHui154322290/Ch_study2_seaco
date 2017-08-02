/**
 * 
 */
package com.tp.dto.wms.logistics;

import java.io.Serializable;

/**
 * @author Administrator
 * 运单商品详情
 */
public class WaybillItemInfoDto implements Serializable{
	
	private static final long serialVersionUID = -5443005066963785577L;

	/** 商品ID **/
	private String itemSku;
	
	/** 商品名称 **/
	private String itemName;
	
	/** 商品数量 **/
	private Integer quantity;
	
	/** 销售价格 **/
	private Double salesprice;

	public String getItemSku() {
		return itemSku;
	}

	public void setItemSku(String itemSku) {
		this.itemSku = itemSku;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getSalesprice() {
		return salesprice;
	}

	public void setSalesprice(Double salesprice) {
		this.salesprice = salesprice;
	}
}
