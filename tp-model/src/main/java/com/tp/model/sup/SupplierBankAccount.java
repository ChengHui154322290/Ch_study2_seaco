package com.tp.model.sup;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 供应商-银行帐号表
  */
public class SupplierBankAccount extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450403175068L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**供应商id 数据类型bigint(11)*/
	private Long supplierId;
	
	/**帐号类型：付款/收款 数据类型varchar(60)*/
	private String accountType;
	
	/**开户银行名称 数据类型varchar(80)*/
	private String bankName;
	
	/**银行账户 数据类型varchar(100)*/
	private String bankAccount;
	
	/**银行币种（人民币、美元之类的code） 数据类型varchar(100)*/
	private String bankCurrency;
	
	/**银行开户人姓名 数据类型varchar(80)*/
	private String bankAccName;
	
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
	public String getAccountType(){
		return accountType;
	}
	public String getBankName(){
		return bankName;
	}
	public String getBankAccount(){
		return bankAccount;
	}
	public String getBankCurrency(){
		return bankCurrency;
	}
	public String getBankAccName(){
		return bankAccName;
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
	public void setAccountType(String accountType){
		this.accountType=accountType;
	}
	public void setBankName(String bankName){
		this.bankName=bankName;
	}
	public void setBankAccount(String bankAccount){
		this.bankAccount=bankAccount;
	}
	public void setBankCurrency(String bankCurrency){
		this.bankCurrency=bankCurrency;
	}
	public void setBankAccName(String bankAccName){
		this.bankAccName=bankAccName;
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
