package com.tp.model.sch;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class SearchBlacklist extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1463641849139L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**黑名单类型1专场2sku 数据类型tinyint(4)*/
	private Integer type;
	
	/**专场Id或sku 数据类型varchar(100)*/
	private String value;
	
	/**专场或商品名称 数据类型varchar(255)*/
	private String name;
	
	/**是否删除0否1是 默认0 数据类型tinyint(4)*/
	private Integer isDeleted;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型varchar(100)*/
	private String createUser;
	
	
	public Long getId(){
		return id;
	}
	public Integer getType(){
		return type;
	}
	public String getValue(){
		return value;
	}
	public String getName(){
		return name;
	}
	public Integer getIsDeleted(){
		return isDeleted;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setValue(String value){
		this.value=value;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setIsDeleted(Integer isDeleted){
		this.isDeleted=isDeleted;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
}
