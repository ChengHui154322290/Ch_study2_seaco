package com.tp.dto.sup;

import java.io.Serializable;
import java.util.Date;

import com.tp.model.BaseDO;
/**
  * @author zhouguofeng
  * 商家平台店铺
  */
public class SupplierShopDto extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1474515516675L;

	/** 数据类型bigint(11)*/
	private Long id;
	
	/** 数据类型bigint(11)*/
	private Long supplierId;
	
	/**店铺名 数据类型varchar(60)*/
	private String shopName;
	
	/**logo图片地址 数据类型varchar(100)*/
	private String logoPath;
	
	/**移动首页图片 数据类型varchar(255)*/
	private String mobileImage;
	
	/**店铺介绍 数据类型text*/
	private String introMobile;
	
	/**搜索标签1 数据类型varchar(100)*/
	private String searchTitle1;
	
	/**搜索标签2 数据类型varchar(100)*/
	private String searchTitle2;
	
	/**搜索标签3 数据类型varchar(100)*/
	private String searchTitle3;
	
	/**搜索标签4 数据类型varchar(100)*/
	private String searchTitle4;
	
	/**备注 数据类型text*/
	private String description;
	
	/**状体（1：启用 0：禁用） 数据类型tinyint(1)*/
	private Integer status;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**创建操作者id 数据类型varchar(60)*/
	private String createUser;
	
	/**更新操作者id 数据类型varchar(60)*/
	private String updateUser;
	
	
	public Long getId(){
		return id;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public String getShopName(){
		return shopName;
	}
	public String getLogoPath(){
		return logoPath;
	}
	public String getMobileImage(){
		return mobileImage;
	}
	public String getIntroMobile(){
		return introMobile;
	}
	public String getSearchTitle1(){
		return searchTitle1;
	}
	public String getSearchTitle2(){
		return searchTitle2;
	}
	public String getSearchTitle3(){
		return searchTitle3;
	}
	public String getSearchTitle4(){
		return searchTitle4;
	}
	public String getDescription(){
		return description;
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
	public void setShopName(String shopName){
		this.shopName=shopName;
	}
	public void setLogoPath(String logoPath){
		this.logoPath=logoPath;
	}
	public void setMobileImage(String mobileImage){
		this.mobileImage=mobileImage;
	}
	public void setIntroMobile(String introMobile){
		this.introMobile=introMobile;
	}
	public void setSearchTitle1(String searchTitle1){
		this.searchTitle1=searchTitle1;
	}
	public void setSearchTitle2(String searchTitle2){
		this.searchTitle2=searchTitle2;
	}
	public void setSearchTitle3(String searchTitle3){
		this.searchTitle3=searchTitle3;
	}
	public void setSearchTitle4(String searchTitle4){
		this.searchTitle4=searchTitle4;
	}
	public void setDescription(String description){
		this.description=description;
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
