package com.tp.model.prd;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 商品图片信息表
  */
public class ItemPictures extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450873698779L;

	/**主键 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/** 数据类型bigint(18)*/
	private Long detailId;
	
	/**商品id 外键 数据类型bigint(18)*/
	private Long itemId;
	
	/**图片路径 数据类型varchar(255)*/
	private String picture;
	
	/**是否主图 默认为0-否 1-是 数据类型tinyint(4)*/
	private Integer main;
	
	/**记录创建时间 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型varchar(32)*/
	private String createUser;
	
	
	public Long getId(){
		return id;
	}
	public Long getDetailId(){
		return detailId;
	}
	public Long getItemId(){
		return itemId;
	}
	public String getPicture(){
		return picture;
	}
	public Integer getMain(){
		return main;
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
	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}
	public void setItemId(Long itemId){
		this.itemId=itemId;
	}
	public void setPicture(String picture){
		this.picture=picture;
	}
	public void setMain(Integer main){
		this.main=main;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
}
