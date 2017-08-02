package com.tp.model.stg;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 库存分配记录表 可分配库存数量=真实库存数量-各业务系统分配的库存数量-占用库存数量，可销售库存=分配库存-已售出数量
  */
public class InventoryDistribute extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450403690113L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**对应的商品sku 数据类型varchar(20)*/
	private String sku;
	
	/**对应库存总记录的id 数据类型bigint(20)*/
	private Long inventoryId;
	
	/**分配库存的应用 数据类型varchar(15)*/
	private String app;
	
	/**业务id 数据类型varchar(20)*/
	private String bizId;
	
	/** 业务分配的库存,不会随售卖情况增减,业务结束后清零 */
	private Integer bizInventory;
	
	/**分配库存数量 数据类型int(11)*/
	private Integer inventory;
	
	/**占用库存数量（即冻结库存） 数据类型int(11)*/
	private Integer occupy;
	
	/**仓库id 数据类型bigint(20)*/
	private Long warehouseId;
	
	/**所在地区id 数据类型bigint(20)*/
	private Long districtId;
	
	/**是否已归还申请库存 数据类型tinyint(4)*/
	private Integer backed;
	
	/**记录创建时间 数据类型datetime*/
	private Date createTime;
	
	/**记录最后修改时间 数据类型datetime*/
	private Date modifyTime;
	
	
	public Long getId(){
		return id;
	}
	public String getSku(){
		return sku;
	}
	public Long getInventoryId(){
		return inventoryId;
	}
	public String getApp(){
		return app;
	}
	public String getBizId(){
		return bizId;
	}
	public Integer getInventory(){
		return inventory;
	}
	public Integer getOccupy(){
		return occupy;
	}
	public Long getWarehouseId(){
		return warehouseId;
	}
	public Long getDistrictId(){
		return districtId;
	}
	public Integer getBacked(){
		return backed;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setSku(String sku){
		this.sku=sku;
	}
	public void setInventoryId(Long inventoryId){
		this.inventoryId=inventoryId;
	}
	public void setApp(String app){
		this.app=app;
	}
	public void setBizId(String bizId){
		this.bizId=bizId;
	}
	public void setInventory(Integer inventory){
		this.inventory=inventory;
	}
	public void setOccupy(Integer occupy){
		this.occupy=occupy;
	}
	public void setWarehouseId(Long warehouseId){
		this.warehouseId=warehouseId;
	}
	public void setDistrictId(Long districtId){
		this.districtId=districtId;
	}
	public void setBacked(Integer backed){
		this.backed=backed;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public Integer getBizInventory() {
		return bizInventory;
	}
	public void setBizInventory(Integer bizInventory) {
		this.bizInventory = bizInventory;
	}
}
