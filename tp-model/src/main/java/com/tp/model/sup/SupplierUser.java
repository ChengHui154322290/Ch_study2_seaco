package com.tp.model.sup;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 商家平台用户主表
  */
public class SupplierUser extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836274737L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/** 数据类型bigint(11)*/
	private Long supplierId;
	
	/**登录名 数据类型varchar(60)*/
	private String loginName;
	
	/** 数据类型varchar(100)*/
	private String password;
	
	/**密码更新时间 数据类型datetime*/
	private Date passwordUpdateTime;
	
	/** 数据类型varchar(100)*/
	private String saltKey;
	
	/**姓名 数据类型varchar(100)*/
	private String userName;
	
	/** 数据类型tinyint(1)*/
	private Integer sex;
	
	/** 数据类型varchar(100)*/
	private String email;
	
	/** 数据类型varchar(45)*/
	private String phone;
	
	/** 数据类型varchar(45)*/
	private String telphone;
	
	/** 数据类型varchar(100)*/
	private String address;
	
	/** 数据类型text*/
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
	public String getLoginName(){
		return loginName;
	}
	public String getPassword(){
		return password;
	}
	public Date getPasswordUpdateTime(){
		return passwordUpdateTime;
	}
	public String getSaltKey(){
		return saltKey;
	}
	public String getUserName(){
		return userName;
	}
	public Integer getSex(){
		return sex;
	}
	public String getEmail(){
		return email;
	}
	public String getPhone(){
		return phone;
	}
	public String getTelphone(){
		return telphone;
	}
	public String getAddress(){
		return address;
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
	public void setLoginName(String loginName){
		this.loginName=loginName;
	}
	public void setPassword(String password){
		this.password=password;
	}
	public void setPasswordUpdateTime(Date passwordUpdateTime){
		this.passwordUpdateTime=passwordUpdateTime;
	}
	public void setSaltKey(String saltKey){
		this.saltKey=saltKey;
	}
	public void setUserName(String userName){
		this.userName=userName;
	}
	public void setSex(Integer sex){
		this.sex=sex;
	}
	public void setEmail(String email){
		this.email=email;
	}
	public void setPhone(String phone){
		this.phone=phone;
	}
	public void setTelphone(String telphone){
		this.telphone=telphone;
	}
	public void setAddress(String address){
		this.address=address;
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
