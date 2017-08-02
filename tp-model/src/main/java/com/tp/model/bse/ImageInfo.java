package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class ImageInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1481612047339L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/** 数据类型varchar(32)*/
	private String name;
	
	/** 数据类型varchar(64)*/
	private String image;
	
	/** 数据类型varchar(32)*/
	private String bucket;
	
	/** 数据类型varchar(32)*/
	private String code;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型varchar(32)*/
	private String createUser;
	
	
	public Long getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public String getImage(){
		return image;
	}
	public String getBucket(){
		return bucket;
	}
	public String getCode(){
		return code;
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
	public void setName(String name){
		this.name=name;
	}
	public void setImage(String image){
		this.image=image;
	}
	public void setBucket(String bucket){
		this.bucket=bucket;
	}
	public void setCode(String code){
		this.code=code;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
}
