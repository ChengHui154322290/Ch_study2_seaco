package com.tp.model.sch;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class SearchOperateLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1463740231274L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**操作类型 数据类型tinyint(255)*/
	private Integer operateType;
	
	/**内容 数据类型varchar(255)*/
	private String content;
	
	/** 数据类型varchar(50)*/
	private String createUser;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Integer getOperateType(){
		return operateType;
	}
	public String getContent(){
		return content;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setOperateType(Integer operateType){
		this.operateType=operateType;
	}
	public void setContent(String content){
		this.content=content;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
