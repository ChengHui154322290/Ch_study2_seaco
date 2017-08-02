package com.tp.model.sup;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 供应商-品类表
  */
public class SupplierCategory extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836274736L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**供应商id 数据类型bigint(11)*/
	private Long supplierId;
	
	/**经营品牌id 数据类型bigint(11)*/
	private Long brandId;
	
	/**品牌名称 数据类型varchar(80)*/
	private String brandName;
	
	/**大类id 数据类型bigint(11)*/
	private Long categoryBigId;
	
	/**大类名称 数据类型varchar(80)*/
	private String categoryBigName;
	
	/**中类id 数据类型bigint(11)*/
	private Long categoryMidId;
	
	/**中类名称 数据类型varchar(80)*/
	private String categoryMidName;
	
	/**小类id 数据类型bigint(11)*/
	private Long categorySmallId;
	
	/**小类名称 数据类型varchar(80)*/
	private String categorySmallName;
	
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
	public Long getBrandId(){
		return brandId;
	}
	public String getBrandName(){
		return brandName;
	}
	public Long getCategoryBigId(){
		return categoryBigId;
	}
	public String getCategoryBigName(){
		return categoryBigName;
	}
	public Long getCategoryMidId(){
		return categoryMidId;
	}
	public String getCategoryMidName(){
		return categoryMidName;
	}
	public Long getCategorySmallId(){
		return categorySmallId;
	}
	public String getCategorySmallName(){
		return categorySmallName;
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
	public void setBrandId(Long brandId){
		this.brandId=brandId;
	}
	public void setBrandName(String brandName){
		this.brandName=brandName;
	}
	public void setCategoryBigId(Long categoryBigId){
		this.categoryBigId=categoryBigId;
	}
	public void setCategoryBigName(String categoryBigName){
		this.categoryBigName=categoryBigName;
	}
	public void setCategoryMidId(Long categoryMidId){
		this.categoryMidId=categoryMidId;
	}
	public void setCategoryMidName(String categoryMidName){
		this.categoryMidName=categoryMidName;
	}
	public void setCategorySmallId(Long categorySmallId){
		this.categorySmallId=categorySmallId;
	}
	public void setCategorySmallName(String categorySmallName){
		this.categorySmallName=categorySmallName;
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
