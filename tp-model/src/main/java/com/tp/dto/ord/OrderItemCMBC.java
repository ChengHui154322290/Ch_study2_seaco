package com.tp.dto.ord;

import java.io.Serializable;

public class OrderItemCMBC implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7633573474718743159L;
	/**
	 * 
	 */
	
	// 子订单数据
	private Long orderId;	
	private Long orderCode;
	// 订单实付总价
	private Double total;
	////////////////////////////////////////////////////


	// OrderItem数据
	/**商品 item ID */
	private Long orderItemId;

	/**商品ID 数据类型bigint(20)*/
	private Long spuId;
	
	/**商品编号 数据类型varchar(32)*/
	private String spuCode;
	
	/**商品名称 数据类型varchar(100)*/
	private String spuName;

	/**实付行小计 数据类型double(10,2)*/
	private Double subTotal;
	
	/**SKU编号 数据类型varchar(50)*/
	private String skuCode;
	
	/**SKU条形码 数据类型varchar(50)*/
	private String barCode;

	
	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}
	
	
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(Long orderCode) {
		this.orderCode = orderCode;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public String getSpuCode() {
		return spuCode;
	}

	public void setSpuCode(String spuCode) {
		this.spuCode = spuCode;
	}

	public String getSpuName() {
		return spuName;
	}

	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

}
