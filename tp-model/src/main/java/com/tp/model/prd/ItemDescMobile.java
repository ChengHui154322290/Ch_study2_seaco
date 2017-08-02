package com.tp.model.prd;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 商品介绍手机版
  */
public class ItemDescMobile extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450873698777L;

	/**主键 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/** 数据类型bigint(18)*/
	private Long detailId;
	
	/**商品ID 数据类型bigint(18)*/
	private Long itemId;
	
	/**商品描述 数据类型text*/
	private String description;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改人 数据类型varchar(32)*/
	private String updateUser;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getDetailId(){
		return detailId;
	}
	public Long getItemId(){
		return itemId;
	}
	public String getDescription(){
		return description;
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
	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}
	public void setItemId(Long itemId){
		this.itemId=itemId;
	}
	public void setDescription(String description){
		this.description=description;
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
