package com.tp.result.stg;

import java.io.Serializable;

/**
 * 
 * <pre>
 *  入库明细信息类中的OrderDetailsResult (详细信息)
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class OrderDetailsResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
