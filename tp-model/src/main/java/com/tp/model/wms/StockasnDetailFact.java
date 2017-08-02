package com.tp.model.wms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class StockasnDetailFact extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1467702259988L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;

	private Long stockasnFactId;
	
	/**我方sku 数据类型varchar(45)*/
	private String sku;
	
	/**第三方sku 数据类型varchar(45)*/
	private String skuTp;
	
	/**实际入库数量 数据类型int(11)*/
	private Integer quantity;
	
	/** 数据类型datetime*/
	private Date createTime;

	public Long getStockasnFactId() {
		return stockasnFactId;
	}

	public void setStockasnFactId(Long stockasnFactId) {
		this.stockasnFactId = stockasnFactId;
	}

	public Long getId(){
		return id;
	}
	public String getSku(){
		return sku;
	}
	public String getSkuTp(){
		return skuTp;
	}
	public Integer getQuantity(){
		return quantity;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setSku(String sku){
		this.sku=sku;
	}
	public void setSkuTp(String skuTp){
		this.skuTp=skuTp;
	}
	public void setQuantity(Integer quantity){
		this.quantity=quantity;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
