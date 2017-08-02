package com.tp.model.app;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * app开关~~~
  */
public class SwitchInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1481792040209L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/** 数据类型varchar(32)*/
	private String name;
	
	/** 数据类型varchar(32)*/
	private String code;
	
	/** 数据类型tinyint(255)*/
	private Integer status;
	
	/** 数据类型varchar(32)*/
	private String createUser;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型varchar(255)*/
	private String updateUser;
	
	/** 数据类型datetime*/
	private Date updateTime;
	
	
	public Long getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public String getCode(){
		return code;
	}
	public Integer getStatus(){
		return status;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setCode(String code){
		this.code=code;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
}
