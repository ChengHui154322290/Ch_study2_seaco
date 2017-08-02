package com.tp.dto.mmp;

import java.io.Serializable;

public class CouponOrderDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6068035391454332561L;

	/**商品的一级分类*/
	private Long firstCategoryId;
	
	/**商品的2级分类*/
	private Long secondCategoryId;
	
	/**商品的3级分类*/
	private Long thordCategoryId;
	
	/**商品的sku*/
	private String sku;
	
	/**商品的品牌id*/
	private Long  brandId;
	
	/** 商品类别  全网 自营 联营*/
	private Integer itemType;
	
	/** 单价 */
	private Double price;
	
	/** 购买的数量 */
	private Integer quantity;
	
	/**供应商Id */
	private Long supplierId;
	
	
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Long getFirstCategoryId() {
		return firstCategoryId;
	}
	public void setFirstCategoryId(Long firstCategoryId) {
		this.firstCategoryId = firstCategoryId;
	}
	public Long getSecondCategoryId() {
		return secondCategoryId;
	}
	public void setSecondCategoryId(Long secondCategoryId) {
		this.secondCategoryId = secondCategoryId;
	}
	public Long getThordCategoryId() {
		return thordCategoryId;
	}
	public void setThordCategoryId(Long thordCategoryId) {
		this.thordCategoryId = thordCategoryId;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public Integer getItemType() {
		return itemType;
	}
	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}
	

	
	
	
	
}
