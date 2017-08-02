package com.tp.model.mmp;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class OlgbHsConfig extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1476865858234L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/** 数据类型bigint(20)*/
	private Long topicItemId;
	
	/** 数据类型bigint(20)*/
	private Long topicId;
	
	/** 数据类型varchar(128)*/
	private String topicName;
	
	/** 数据类型varchar(32)*/
	private String sku;
	
	/** 数据类型varchar(128)*/
	private String itemName;
	
	/** 数据类型int(255)*/
	private Integer sort;
	
	/** 数据类型tinyint(1)*/
	private Integer status;
	
	/** 数据类型varchar(128)*/
	private String createUser;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型varchar(128)*/
	private String updateUser;
	
	/** 数据类型datetime*/
	private Date updateTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getTopicItemId(){
		return topicItemId;
	}
	public Long getTopicId(){
		return topicId;
	}
	public String getTopicName(){
		return topicName;
	}
	public String getSku(){
		return sku;
	}
	public String getItemName(){
		return itemName;
	}
	public Integer getSort(){
		return sort;
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
	public void setTopicItemId(Long topicItemId){
		this.topicItemId=topicItemId;
	}
	public void setTopicId(Long topicId){
		this.topicId=topicId;
	}
	public void setTopicName(String topicName){
		this.topicName=topicName;
	}
	public void setSku(String sku){
		this.sku=sku;
	}
	public void setItemName(String itemName){
		this.itemName=itemName;
	}
	public void setSort(Integer sort){
		this.sort=sort;
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
