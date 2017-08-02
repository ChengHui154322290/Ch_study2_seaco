package com.tp.model.wms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 出库订单明细
  */
public class StockoutDetail extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1464588984275L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**出库单ID 数据类型bigint(20)*/
	private Long stockoutId;
	
	/**SKU编码 数据类型varchar(50)*/
	private String itemSku;
	
	/**SKU名称 数据类型varchar(200)*/
	private String itemName;
	
	/**商品条形码 数据类型varchar(50)*/
	private String itemBarcode;
	
	/**数量 数据类型int(11)*/
	private Integer quantity;
	
	/**实际价格 数据类型double(12,2)*/
	private Double actualPrice;
	
	/**销售价格 数据类型double(12,2)*/
	private Double salesPrice;
	
	/**优惠金额 数据类型double(12,2)*/
	private Double discountAmount;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getStockoutId(){
		return stockoutId;
	}
	public String getItemSku(){
		return itemSku;
	}
	public String getItemName(){
		return itemName;
	}
	public String getItemBarcode(){
		return itemBarcode;
	}
	public Integer getQuantity(){
		return quantity;
	}
	public Double getActualPrice(){
		return actualPrice;
	}
	public Double getSalesPrice(){
		return salesPrice;
	}
	public Double getDiscountAmount(){
		return discountAmount;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setStockoutId(Long stockoutId){
		this.stockoutId=stockoutId;
	}
	public void setItemSku(String itemSku){
		this.itemSku=itemSku;
	}
	public void setItemName(String itemName){
		this.itemName=itemName;
	}
	public void setItemBarcode(String itemBarcode){
		this.itemBarcode=itemBarcode;
	}
	public void setQuantity(Integer quantity){
		this.quantity=quantity;
	}
	public void setActualPrice(Double actualPrice){
		this.actualPrice=actualPrice;
	}
	public void setSalesPrice(Double salesPrice){
		this.salesPrice=salesPrice;
	}
	public void setDiscountAmount(Double discountAmount){
		this.discountAmount=discountAmount;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
