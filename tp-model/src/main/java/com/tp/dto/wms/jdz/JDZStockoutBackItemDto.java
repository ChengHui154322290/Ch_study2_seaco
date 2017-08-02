/**
 * 
 */
package com.tp.dto.wms.jdz;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class JDZStockoutBackItemDto implements Serializable{

	private static final long serialVersionUID = -7586337106259367059L;

	/** 商品SKU */
	private String sku;
	
	/** 商品数量 */
	private Integer qty;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}
}
