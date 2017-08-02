package com.tp.model.sup;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 供应商-西客对接人信息表
  */
public class SupplierXgLink extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836567625L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**供应商id 数据类型bigint(11)*/
	private Long supplierId;
	
	/**西客联系人类型 数据类型varchar(60)*/
	private String linkType;
	
	/**部门id 数据类型varchar(45)*/
	private String deptId;
	
	/**部门名称 数据类型varchar(45)*/
	private String deptName;
	
	/**用户id 数据类型varchar(45)*/
	private String userId;
	
	/**姓名 数据类型varchar(80)*/
	private String userName;
	
	/**移动电话 数据类型varchar(60)*/
	private String mobilePhone;
	
	/**固定电话 数据类型varchar(60)*/
	private String telephone;
	
	/**电子邮箱 数据类型varchar(60)*/
	private String email;
	
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
	public String getLinkType(){
		return linkType;
	}
	public String getDeptId(){
		return deptId;
	}
	public String getDeptName(){
		return deptName;
	}
	public String getUserId(){
		return userId;
	}
	public String getUserName(){
		return userName;
	}
	public String getMobilePhone(){
		return mobilePhone;
	}
	public String getTelephone(){
		return telephone;
	}
	public String getEmail(){
		return email;
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
	public void setLinkType(String linkType){
		this.linkType=linkType;
	}
	public void setDeptId(String deptId){
		this.deptId=deptId;
	}
	public void setDeptName(String deptName){
		this.deptName=deptName;
	}
	public void setUserId(String userId){
		this.userId=userId;
	}
	public void setUserName(String userName){
		this.userName=userName;
	}
	public void setMobilePhone(String mobilePhone){
		this.mobilePhone=mobilePhone;
	}
	public void setTelephone(String telephone){
		this.telephone=telephone;
	}
	public void setEmail(String email){
		this.email=email;
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
