package com.tp.model.stg;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 下单完成后库存占用（冻结）及取消（解冻）记录
  */
public class InventoryOccupy extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450403690113L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/** 数据类型bigint(20)*/
	private Long inventoryDistId;
	
	/** 总库存ID */
	private Long inventoryId;
	
	/** 是否冻结预占库存:0否1是*/
	private Integer occupyDistribute;
	
	/**应用 数据类型varchar(50)*/
	private String app;
	
	/**活动id 数据类型varchar(20)*/
	private String bizId;
	
	/**商品id 数据类型varchar(20)*/
	private String sku;
	
	/**占用库存 数据类型int(11)*/
	private Integer inventory;
	
	/**订单编号 数据类型varchar(50)*/
	private String orderNo;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getInventoryDistId(){
		return inventoryDistId;
	}
	public String getApp(){
		return app;
	}
	public String getBizId(){
		return bizId;
	}
	public String getSku(){
		return sku;
	}
	public Integer getInventory(){
		return inventory;
	}
	public String getOrderNo(){
		return orderNo;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setInventoryDistId(Long inventoryDistId){
		this.inventoryDistId=inventoryDistId;
	}
	public void setApp(String app){
		this.app=app;
	}
	public void setBizId(String bizId){
		this.bizId=bizId;
	}
	public void setSku(String sku){
		this.sku=sku;
	}
	public void setInventory(Integer inventory){
		this.inventory=inventory;
	}
	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public Long getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
	}
	public Integer getOccupyDistribute() {
		return occupyDistribute;
	}
	public void setOccupyDistribute(Integer occupyDistribute) {
		this.occupyDistribute = occupyDistribute;
	}
}
