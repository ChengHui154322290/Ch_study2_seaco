package com.tp.dto.prd;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.tp.model.prd.ItemSku;
import com.tp.model.prd.ItemSkuArt;


public class PushItemCostpriceDto  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4377255748912870473L;
	//sku
	private String sku;
	//成本价
	private Double costPrice;
	
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public Double getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}
	
	
}
