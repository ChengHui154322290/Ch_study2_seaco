package com.tp.model.sup;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 供应商-(证件)图片信息表
  */
public class SupplierImage extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836274736L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**供应商id 数据类型bigint(11)*/
	private Long supplierId;
	
	/**图片类型 数据类型varchar(100)*/
	private String imageType;
	
	/**图片地址 数据类型varchar(100)*/
	private String imageUrl;
	
	/**图片名称 数据类型varchar(100)*/
	private String name;
	
	/**图片描述 数据类型text*/
	private String description;
	
	/**说明字段（先预留） 数据类型text*/
	private String remark;
	
	/**品牌id（可先预留） 数据类型bigint(11)*/
	private Long brandId;
	
	/**品牌名称 数据类型varchar(80)*/
	private String brandName;
	
	/**状体（1：启用 0：禁用） 数据类型tinyint(1)*/
	private Integer status;
	
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
	public Long getSupplierId(){
		return supplierId;
	}
	public String getImageType(){
		return imageType;
	}
	public String getImageUrl(){
		return imageUrl;
	}
	public String getName(){
		return name;
	}
	public String getDescription(){
		return description;
	}
	public String getRemark(){
		return remark;
	}
	public Long getBrandId(){
		return brandId;
	}
	public String getBrandName(){
		return brandName;
	}
	public Integer getStatus(){
		return status;
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
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setImageType(String imageType){
		this.imageType=imageType;
	}
	public void setImageUrl(String imageUrl){
		this.imageUrl=imageUrl;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setDescription(String description){
		this.description=description;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setBrandId(Long brandId){
		this.brandId=brandId;
	}
	public void setBrandName(String brandName){
		this.brandName=brandName;
	}
	public void setStatus(Integer status){
		this.status=status;
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
