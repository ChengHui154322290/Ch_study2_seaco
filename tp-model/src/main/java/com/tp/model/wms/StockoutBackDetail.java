package com.tp.model.wms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 出库单回执明细
  */
public class StockoutBackDetail extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1467251183573L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**出库单回执ID 数据类型bigint(20)*/
	private Long stockoutBackId;
	
	/**SKU编码(平台方SKU) 数据类型varchar(50)*/
	private String itemSku;
	
	/**仓库方SKU 数据类型varchar(50)*/
	private String stockSku;
	
	/**商品编码 数据类型varchar(50)*/
	private String productNo;
	
	/**数量 数据类型int(11)*/
	private Integer quantity;
	
	/**单个商品重量 单位g 数据类型double(10,2)*/
	private Double weight;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getStockoutBackId(){
		return stockoutBackId;
	}
	public String getItemSku(){
		return itemSku;
	}
	public String getStockSku(){
		return stockSku;
	}
	public String getProductNo(){
		return productNo;
	}
	public Integer getQuantity(){
		return quantity;
	}
	public Double getWeight(){
		return weight;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setStockoutBackId(Long stockoutBackId){
		this.stockoutBackId=stockoutBackId;
	}
	public void setItemSku(String itemSku){
		this.itemSku=itemSku;
	}
	public void setStockSku(String stockSku){
		this.stockSku=stockSku;
	}
	public void setProductNo(String productNo){
		this.productNo=productNo;
	}
	public void setQuantity(Integer quantity){
		this.quantity=quantity;
	}
	public void setWeight(Double weight){
		this.weight=weight;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
