/**
 * 
 */
package com.tp.dto.wms;

import java.io.Serializable;

/**
 * @author Administrator
 * 
 * 出库单对应商品信息
 */
public class StockoutItem implements Serializable{

	private static final long serialVersionUID = 4492380157448027392L;

	/** 子订单ID **/
	private Long orderItemId;
	
	/** 卖家ID **/
	private String sellerUserId;
	
	/** 商品ID **/
	private String itemSku;
	
	/** 商品名称 **/
	private String itemName;
	
	/** 商家的商品编码 **/
	private String productNo;
	
	/** 商品条形码 **/
	private String barCode;
	
	/** 商品数量 **/
	private Integer quantity;
	
	/** 商品实际价格 **/
	private Double actualPrice;
	
	/** 销售价格 **/
	private Double salesprice;
	
	/** 优惠金额 **/
	private Double discountAmount;

	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public String getSellerUserId() {
		return sellerUserId;
	}

	public void setSellerUserId(String sellerUserId) {
		this.sellerUserId = sellerUserId;
	}

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

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(Double actualPrice) {
		this.actualPrice = actualPrice;
	}

	public Double getSalesprice() {
		return salesprice;
	}

	public void setSalesprice(Double salesprice) {
		this.salesprice = salesprice;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	
}
