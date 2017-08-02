package com.tp.model.sup;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 供应商海关备案表
  */
public class SupplierCustomsRecordation extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836274736L;

	/**供应商海关备案表 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/** 数据类型bigint(11)*/
	private Long supplierId;
	
	/**通关渠道id 数据类型varchar(45)*/
	private Long customsChannelId;
	
	/**通关渠道名称 数据类型varchar(45)*/
	private String customsChannelName;
	
	/**备案名称 数据类型varchar(45)*/
	private String recordationName;
	
	/**备案编号 数据类型varchar(45)*/
	private String recordationNum;
	
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
	public Long getCustomsChannelId(){
		return customsChannelId;
	}
	public String getCustomsChannelName(){
		return customsChannelName;
	}
	public String getRecordationName(){
		return recordationName;
	}
	public String getRecordationNum(){
		return recordationNum;
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
	public void setCustomsChannelId(Long customsChannelId){
		this.customsChannelId=customsChannelId;
	}
	public void setCustomsChannelName(String customsChannelName){
		this.customsChannelName=customsChannelName;
	}
	public void setRecordationName(String recordationName){
		this.recordationName=recordationName;
	}
	public void setRecordationNum(String recordationNum){
		this.recordationNum=recordationNum;
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
