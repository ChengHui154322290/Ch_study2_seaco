package com.tp.model.sup;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 供应商-开票信息表
  */
public class SupplierInvoice extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836274737L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**供应商id 数据类型bigint(11)*/
	private Long supplierId;
	
	/**开票名称 数据类型varchar(80)*/
	private String name;
	
	/**开户银行 数据类型varchar(80)*/
	private String bankName;
	
	/**开户银行帐号 数据类型varchar(60)*/
	private String bankAccount;
	
	/**联系地址 数据类型varchar(100)*/
	private String linkAddr;
	
	/**纳税人识别码 数据类型varchar(100)*/
	private String taxpayerCode;
	
	/**开户名 数据类型varchar(80)*/
	private String bankAccName;
	
	/**联系电话 数据类型varchar(60)*/
	private String linkPhone;
	
	/**是否为一般纳税人 数据类型tinyint(1)*/
	private Integer isTaxpayer;
	
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
	public String getName(){
		return name;
	}
	public String getBankName(){
		return bankName;
	}
	public String getBankAccount(){
		return bankAccount;
	}
	public String getLinkAddr(){
		return linkAddr;
	}
	public String getTaxpayerCode(){
		return taxpayerCode;
	}
	public String getBankAccName(){
		return bankAccName;
	}
	public String getLinkPhone(){
		return linkPhone;
	}
	public Integer getIsTaxpayer(){
		return isTaxpayer;
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
	public void setName(String name){
		this.name=name;
	}
	public void setBankName(String bankName){
		this.bankName=bankName;
	}
	public void setBankAccount(String bankAccount){
		this.bankAccount=bankAccount;
	}
	public void setLinkAddr(String linkAddr){
		this.linkAddr=linkAddr;
	}
	public void setTaxpayerCode(String taxpayerCode){
		this.taxpayerCode=taxpayerCode;
	}
	public void setBankAccName(String bankAccName){
		this.bankAccName=bankAccName;
	}
	public void setLinkPhone(String linkPhone){
		this.linkPhone=linkPhone;
	}
	public void setIsTaxpayer(Integer isTaxpayer){
		this.isTaxpayer=isTaxpayer;
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
