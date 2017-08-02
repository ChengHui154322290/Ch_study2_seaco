package com.tp.model.wms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 库存同步信息表
  */
public class StocksyncInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1472103343441L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**仓库ID 数据类型bigint(20)*/
	private Long whId;
	
	/**仓库WMS编号 数据类型varchar(50)*/
	private String wmsCode;
	
	/**商品名称 数据类型varchar(50)*/
	private String skuName;
	
	/**仓库SKU 数据类型varchar(50)*/
	private String stockSku;
	
	/**商品SKU 数据类型varchar(50)*/
	private String sku;
	
	/**仓库返回实际库存 数据类型bigint(20)*/
	private Long stockInventory;
	
	/**系统库存 数据类型bigint(20)*/
	private Long inventory;
	
	/**同步时间 数据类型datetime*/
	private Date syncTime;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getWhId(){
		return whId;
	}
	public String getWmsCode(){
		return wmsCode;
	}
	public String getSkuName(){
		return skuName;
	}
	public String getStockSku(){
		return stockSku;
	}
	public String getSku(){
		return sku;
	}
	public Long getStockInventory(){
		return stockInventory;
	}
	public Long getInventory(){
		return inventory;
	}
	public Date getSyncTime(){
		return syncTime;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setWhId(Long whId){
		this.whId=whId;
	}
	public void setWmsCode(String wmsCode){
		this.wmsCode=wmsCode;
	}
	public void setSkuName(String skuName){
		this.skuName=skuName;
	}
	public void setStockSku(String stockSku){
		this.stockSku=stockSku;
	}
	public void setSku(String sku){
		this.sku=sku;
	}
	public void setStockInventory(Long stockInventory){
		this.stockInventory=stockInventory;
	}
	public void setInventory(Long inventory){
		this.inventory=inventory;
	}
	public void setSyncTime(Date syncTime){
		this.syncTime=syncTime;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
