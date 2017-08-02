package com.tp.model.mem;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class ModifyMobileLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1481011737430L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**更换前的手机号码 数据类型varchar(50)*/
	private String oldTel;
	
	/**更换后的手机号码 数据类型varchar(50)*/
	private String newTel;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	
	public Long getId(){
		return id;
	}
	public String getOldTel(){
		return oldTel;
	}
	public String getNewTel(){
		return newTel;
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
	public void setOldTel(String oldTel){
		this.oldTel=oldTel;
	}
	public void setNewTel(String newTel){
		this.newTel=newTel;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
}
