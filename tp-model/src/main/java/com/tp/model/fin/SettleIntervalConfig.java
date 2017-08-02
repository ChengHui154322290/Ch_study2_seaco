package com.tp.model.fin;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 风险管理结算间隔天数配置表
  */
public class SettleIntervalConfig extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450405991706L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**供应商ID 数据类型bigint(20)*/
	private Long supplierId;
	
	/**供应商名称（冗余） 数据类型varchar(100)*/
	private String supplierName;
	
	/**结算间隔天数（活动下线后或结算周期到期后间隔几天才能结算） 数据类型int(4)*/
	private Integer settleIntervalDays;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**创建时间 数据类型datetime*/
	private Date createDate;
	
	/**修改人 数据类型varchar(32)*/
	private String modifyUser;
	
	/**修改时间 数据类型datetime*/
	private Date modifyDate;
	
	
	public Long getId(){
		return id;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public String getSupplierName(){
		return supplierName;
	}
	public Integer getSettleIntervalDays(){
		return settleIntervalDays;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getCreateDate(){
		return createDate;
	}
	public String getModifyUser(){
		return modifyUser;
	}
	public Date getModifyDate(){
		return modifyDate;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setSupplierName(String supplierName){
		this.supplierName=supplierName;
	}
	public void setSettleIntervalDays(Integer settleIntervalDays){
		this.settleIntervalDays=settleIntervalDays;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}
	public void setModifyUser(String modifyUser){
		this.modifyUser=modifyUser;
	}
	public void setModifyDate(Date modifyDate){
		this.modifyDate=modifyDate;
	}
}
