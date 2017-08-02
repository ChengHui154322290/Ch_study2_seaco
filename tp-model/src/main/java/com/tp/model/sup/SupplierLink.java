package com.tp.model.sup;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 供应商-联系人表
  */
public class SupplierLink extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836274737L;

	/**主键信息 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/** 数据类型varchar(100)*/
	private Long supplierId;
	
	/**联系人姓名 数据类型varchar(80)*/
	private String linkName;
	
	/**联系人类型 数据类型varchar(60)*/
	private String linkType;
	
	/**移动电话 数据类型varchar(60)*/
	private String mobilePhone;
	
	/**固定电话 数据类型varchar(60)*/
	private String telephone;
	
	/** 数据类型varchar(100)*/
	private String linkAddress;
	
	/** 数据类型varchar(60)*/
	private String email;
	
	/** 数据类型varchar(60)*/
	private String fax;
	
	/** 数据类型varchar(60)*/
	private String qq;
	
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
	public String getLinkName(){
		return linkName;
	}
	public String getLinkType(){
		return linkType;
	}
	public String getMobilePhone(){
		return mobilePhone;
	}
	public String getTelephone(){
		return telephone;
	}
	public String getLinkAddress(){
		return linkAddress;
	}
	public String getEmail(){
		return email;
	}
	public String getFax(){
		return fax;
	}
	public String getQq(){
		return qq;
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
	public void setLinkName(String linkName){
		this.linkName=linkName;
	}
	public void setLinkType(String linkType){
		this.linkType=linkType;
	}
	public void setMobilePhone(String mobilePhone){
		this.mobilePhone=mobilePhone;
	}
	public void setTelephone(String telephone){
		this.telephone=telephone;
	}
	public void setLinkAddress(String linkAddress){
		this.linkAddress=linkAddress;
	}
	public void setEmail(String email){
		this.email=email;
	}
	public void setFax(String fax){
		this.fax=fax;
	}
	public void setQq(String qq){
		this.qq=qq;
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
