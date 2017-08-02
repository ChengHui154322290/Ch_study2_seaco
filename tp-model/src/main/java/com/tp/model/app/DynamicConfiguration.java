package com.tp.model.app;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class DynamicConfiguration extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1481186498451L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**配置名称 数据类型varchar(255)*/
	private String name;
	
	/**配置内容 数据类型varchar(5000)*/
	private String content;

	private Integer versionFrom;

	private Integer versionTo;
	
	/**状态0未启用1启用2删除 数据类型tinyint(1)*/
	private Integer status;
	
	/** 数据类型varchar(32)*/
	private String createUser;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型varchar(32)*/
	private String updateUser;
	
	/** 数据类型datetime*/
	private Date updateTime;

	public Integer getVersionFrom() {
		return versionFrom;
	}

	public void setVersionFrom(Integer versionFrom) {
		this.versionFrom = versionFrom;
	}

	public Integer getVersionTo() {
		return versionTo;
	}

	public void setVersionTo(Integer versionTo) {
		this.versionTo = versionTo;
	}

	public Long getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public String getContent(){
		return content;
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
	public void setContent(String content){
		this.content=content;
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
