package com.tp.dto.stg.BML;

public class OrderDetails {
	/**
	 * SKU
	 */
	private String SkuCode;
	/**
	 * 发货数量
	 */
	private String QtyShipped;
	/**
	 * 明细金额
	 */
	private String Price;

	public String getSkuCode() {
		return SkuCode;
	}

	public void setSkuCode(String skuCode) {
		SkuCode = skuCode;
	}

	public String getQtyShipped() {
		return QtyShipped;
	}

	public void setQtyShipped(String qtyShipped) {
		QtyShipped = qtyShipped;
	}

	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		Price = price;
	}

}
