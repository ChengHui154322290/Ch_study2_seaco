package com.tp.model.sup;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class Sequence extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836274735L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**Sequence名称 数据类型varchar(100)*/
	private String keyName;
	
	/**Sequence最大值 数据类型bigint(20)*/
	private Long keyValue;
	
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
	public String getKeyName(){
		return keyName;
	}
	public Long getKeyValue(){
		return keyValue;
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
	public void setKeyName(String keyName){
		this.keyName=keyName;
	}
	public void setKeyValue(Long keyValue){
		this.keyValue=keyValue;
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
