package com.tp.model.sup;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 合同-国内联系方表
  */
public class ContractOffice extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836274731L;

	/**主键信息 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/** 数据类型bigint(11)*/
	private Long contractId;
	
	/**所属国家 数据类型varchar(45)*/
	private String country;
	
	/**发货地（多个用逗号分隔） 数据类型varchar(450)*/
	private String deliveryPlace;
	
	/**联系人 数据类型varchar(30)*/
	private String linkName;
	
	/**移动电话 数据类型varchar(20)*/
	private String mobilePhone;
	
	/**固定电话 数据类型varchar(20)*/
	private String telephone;
	
	/**地址 数据类型varchar(100)*/
	private String linkAddress;
	
	/**退换货地址 数据类型varchar(100)*/
	private String linkAddressReturn;
	
	/** 数据类型varchar(60)*/
	private String email;
	
	/** 数据类型varchar(60)*/
	private String fax;
	
	/** 数据类型varchar(20)*/
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
	public Long getContractId(){
		return contractId;
	}
	public String getCountry(){
		return country;
	}
	public String getDeliveryPlace(){
		return deliveryPlace;
	}
	public String getLinkName(){
		return linkName;
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
	public String getLinkAddressReturn(){
		return linkAddressReturn;
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
	public void setContractId(Long contractId){
		this.contractId=contractId;
	}
	public void setCountry(String country){
		this.country=country;
	}
	public void setDeliveryPlace(String deliveryPlace){
		this.deliveryPlace=deliveryPlace;
	}
	public void setLinkName(String linkName){
		this.linkName=linkName;
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
	public void setLinkAddressReturn(String linkAddressReturn){
		this.linkAddressReturn=linkAddressReturn;
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
