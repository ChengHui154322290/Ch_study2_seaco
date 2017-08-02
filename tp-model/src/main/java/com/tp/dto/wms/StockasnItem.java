/**
 * 
 */
package com.tp.dto.wms;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class StockasnItem implements Serializable {
	
	private static final long serialVersionUID = 5735788906236439317L;

	/** 子订单ID **/
	private Long orderItemId;
	
	/** 卖家ID **/
	private String sellerUserId;
	
	/** 商品ID ：平台商sku唯一标识**/
	private String itemSKU;
	
	/** 库存类型:1 可销售库存 101 残次**/
	private Integer inventoryType;
	
	/** 商品名称 **/
	private String itemName;
	
	/** 商品条形码 **/
	private String barcode;
	
	/** 商家的商品编码 **/
	private String productNo;
	
	/** 商品数量 **/
	private String quantity;
	
	/** 商品实际价格 **/
	private String actualPrice;
	
	/** 销售价格 **/
	private String price;
	
	/** 原产国Code **/
	private String countryCode;
	
	/** 原产国名字 **/
	private String countryName;

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

	public String getItemSKU() {
		return itemSKU;
	}

	public void setItemSKU(String itemSKU) {
		this.itemSKU = itemSKU;
	}

	public Integer getInventoryType() {
		return inventoryType;
	}

	public void setInventoryType(Integer inventoryType) {
		this.inventoryType = inventoryType;
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

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(String actualPrice) {
		this.actualPrice = actualPrice;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
}
