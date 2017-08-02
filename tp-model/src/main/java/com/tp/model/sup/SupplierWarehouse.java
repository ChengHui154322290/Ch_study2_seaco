package com.tp.model.sup;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 供应商-仓库及配送信息表
  */
public class SupplierWarehouse extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836274738L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**供应商id 数据类型bigint(11)*/
	private Long supplierId;
	
	/**仓库id 数据类型bigint(11)*/
	private Long warehouseId;
	
	/**仓库名称 数据类型varchar(60)*/
	private String warehouseCode;
	
	/**仓库名称 数据类型varchar(80)*/
	private String warehouseName;
	
	/**状体（1：启用 0：禁用） 数据类型tinyint(1)*/
	private Integer status;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**创建操作者id 数据类型varchar(32)*/
	private String createUser;
	
	/**更新操作者id 数据类型varchar(32)*/
	private String updateUser;
	
	
	public Long getId(){
		return id;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public Long getWarehouseId(){
		return warehouseId;
	}
	public String getWarehouseCode(){
		return warehouseCode;
	}
	public String getWarehouseName(){
		return warehouseName;
	}
	public Integer getStatus(){
		return status;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setWarehouseId(Long warehouseId){
		this.warehouseId=warehouseId;
	}
	public void setWarehouseCode(String warehouseCode){
		this.warehouseCode=warehouseCode;
	}
	public void setWarehouseName(String warehouseName){
		this.warehouseName=warehouseName;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
}
