/**
 * 
 */
package com.tp.dto.ptm.storage;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class InventoryDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5096618825629080249L;
	private String sku;
	private String goodsCode;
	private Integer count;

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
}
