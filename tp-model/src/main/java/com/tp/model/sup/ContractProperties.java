package com.tp.model.sup;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 合同关联供应商的附件属性表
  */
public class ContractProperties extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836274731L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/** 数据类型bigint(11)*/
	private Long contractId;
	
	/**(乙方)联系人姓名 数据类型varchar(80)*/
	private String spLinkName;
	
	/**(乙方)联系人类型 数据类型varchar(20)*/
	private String spLinkType;
	
	/**(乙方)移动电话 数据类型varchar(20)*/
	private String spMobilePhone;
	
	/**(乙方)固定电话 数据类型varchar(20)*/
	private String spTelephone;
	
	/**(乙方)联系人地址 数据类型varchar(100)*/
	private String spLinkAddress;
	
	/**(乙方)电子邮箱 数据类型varchar(60)*/
	private String spEmail;
	
	/**(乙方)传真 数据类型varchar(20)*/
	private String spFax;
	
	/**(乙方)QQ 数据类型varchar(20)*/
	private String spQq;
	
	/**(甲方)部门id 数据类型varchar(45)*/
	private String xgDeptId;
	
	/**(甲方)部门名称 数据类型varchar(45)*/
	private String xgDeptName;
	
	/**(甲方)用户id 数据类型varchar(45)*/
	private String xgUserId;
	
	/**(甲方)姓名 数据类型varchar(80)*/
	private String xgUserName;
	
	/**(乙方)移动电话 数据类型varchar(20)*/
	private String xgMobilePhone;
	
	/**(乙方)固定电话 数据类型varchar(20)*/
	private String xgTelephone;
	
	/**(乙方)联系人地址 数据类型varchar(100)*/
	private String xgLinkAddress;
	
	/**(乙方)电子邮箱 数据类型varchar(60)*/
	private String xgEmail;
	
	/**(乙方)传真 数据类型varchar(20)*/
	private String xgFax;
	
	/**(乙方)QQ 数据类型varchar(20)*/
	private String xgQq;
	
	/**开户银行名称 数据类型varchar(80)*/
	private String bankName;
	
	/**银行账户 数据类型varchar(100)*/
	private String bankAccount;
	
	/**银行币种（人民币、美元之类的code） 数据类型varchar(20)*/
	private String bankCurrency;
	
	/**银行开户人姓名 数据类型varchar(80)*/
	private String bankAccName;
	
	/**开票名称 数据类型varchar(80)*/
	private String spInvoiceName;
	
	/**开户银行 数据类型varchar(80)*/
	private String spBankName;
	
	/**开户银行帐号 数据类型varchar(60)*/
	private String spBankAccount;
	
	/**开票信息-联系地址 数据类型varchar(100)*/
	private String spInvoiceLinkAddress;
	
	/**纳税人识别码 数据类型varchar(60)*/
	private String spTaxpayerCode;
	
	/**开户名称 数据类型varchar(80)*/
	private String spBankAccName;
	
	/**联系电话 数据类型varchar(20)*/
	private String spLinkPhone;
	
	/**公司法人 数据类型varchar(60)*/
	private String baseLegalPerson;
	
	/**联系人 数据类型varchar(60)*/
	private String baseLinkName;
	
	/**联系地址 数据类型varchar(100)*/
	private String baseLinkAddress;
	
	/**电子邮箱 数据类型varchar(60)*/
	private String baseEmail;
	
	/**联系电话 数据类型varchar(20)*/
	private String baseLinkPhone;
	
	/**传真 数据类型varchar(20)*/
	private String baseFax;
	
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
	public String getSpLinkName(){
		return spLinkName;
	}
	public String getSpLinkType(){
		return spLinkType;
	}
	public String getSpMobilePhone(){
		return spMobilePhone;
	}
	public String getSpTelephone(){
		return spTelephone;
	}
	public String getSpLinkAddress(){
		return spLinkAddress;
	}
	public String getSpEmail(){
		return spEmail;
	}
	public String getSpFax(){
		return spFax;
	}
	public String getSpQq(){
		return spQq;
	}
	public String getXgDeptId(){
		return xgDeptId;
	}
	public String getXgDeptName(){
		return xgDeptName;
	}
	public String getXgUserId(){
		return xgUserId;
	}
	public String getXgUserName(){
		return xgUserName;
	}
	public String getXgMobilePhone(){
		return xgMobilePhone;
	}
	public String getXgTelephone(){
		return xgTelephone;
	}
	public String getXgLinkAddress(){
		return xgLinkAddress;
	}
	public String getXgEmail(){
		return xgEmail;
	}
	public String getXgFax(){
		return xgFax;
	}
	public String getXgQq(){
		return xgQq;
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
	public String getSpInvoiceName(){
		return spInvoiceName;
	}
	public String getSpBankName(){
		return spBankName;
	}
	public String getSpBankAccount(){
		return spBankAccount;
	}
	public String getSpInvoiceLinkAddress(){
		return spInvoiceLinkAddress;
	}
	public String getSpTaxpayerCode(){
		return spTaxpayerCode;
	}
	public String getSpBankAccName(){
		return spBankAccName;
	}
	public String getSpLinkPhone(){
		return spLinkPhone;
	}
	public String getBaseLegalPerson(){
		return baseLegalPerson;
	}
	public String getBaseLinkName(){
		return baseLinkName;
	}
	public String getBaseLinkAddress(){
		return baseLinkAddress;
	}
	public String getBaseEmail(){
		return baseEmail;
	}
	public String getBaseLinkPhone(){
		return baseLinkPhone;
	}
	public String getBaseFax(){
		return baseFax;
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
	public void setSpLinkName(String spLinkName){
		this.spLinkName=spLinkName;
	}
	public void setSpLinkType(String spLinkType){
		this.spLinkType=spLinkType;
	}
	public void setSpMobilePhone(String spMobilePhone){
		this.spMobilePhone=spMobilePhone;
	}
	public void setSpTelephone(String spTelephone){
		this.spTelephone=spTelephone;
	}
	public void setSpLinkAddress(String spLinkAddress){
		this.spLinkAddress=spLinkAddress;
	}
	public void setSpEmail(String spEmail){
		this.spEmail=spEmail;
	}
	public void setSpFax(String spFax){
		this.spFax=spFax;
	}
	public void setSpQq(String spQq){
		this.spQq=spQq;
	}
	public void setXgDeptId(String xgDeptId){
		this.xgDeptId=xgDeptId;
	}
	public void setXgDeptName(String xgDeptName){
		this.xgDeptName=xgDeptName;
	}
	public void setXgUserId(String xgUserId){
		this.xgUserId=xgUserId;
	}
	public void setXgUserName(String xgUserName){
		this.xgUserName=xgUserName;
	}
	public void setXgMobilePhone(String xgMobilePhone){
		this.xgMobilePhone=xgMobilePhone;
	}
	public void setXgTelephone(String xgTelephone){
		this.xgTelephone=xgTelephone;
	}
	public void setXgLinkAddress(String xgLinkAddress){
		this.xgLinkAddress=xgLinkAddress;
	}
	public void setXgEmail(String xgEmail){
		this.xgEmail=xgEmail;
	}
	public void setXgFax(String xgFax){
		this.xgFax=xgFax;
	}
	public void setXgQq(String xgQq){
		this.xgQq=xgQq;
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
	public void setSpInvoiceName(String spInvoiceName){
		this.spInvoiceName=spInvoiceName;
	}
	public void setSpBankName(String spBankName){
		this.spBankName=spBankName;
	}
	public void setSpBankAccount(String spBankAccount){
		this.spBankAccount=spBankAccount;
	}
	public void setSpInvoiceLinkAddress(String spInvoiceLinkAddress){
		this.spInvoiceLinkAddress=spInvoiceLinkAddress;
	}
	public void setSpTaxpayerCode(String spTaxpayerCode){
		this.spTaxpayerCode=spTaxpayerCode;
	}
	public void setSpBankAccName(String spBankAccName){
		this.spBankAccName=spBankAccName;
	}
	public void setSpLinkPhone(String spLinkPhone){
		this.spLinkPhone=spLinkPhone;
	}
	public void setBaseLegalPerson(String baseLegalPerson){
		this.baseLegalPerson=baseLegalPerson;
	}
	public void setBaseLinkName(String baseLinkName){
		this.baseLinkName=baseLinkName;
	}
	public void setBaseLinkAddress(String baseLinkAddress){
		this.baseLinkAddress=baseLinkAddress;
	}
	public void setBaseEmail(String baseEmail){
		this.baseEmail=baseEmail;
	}
	public void setBaseLinkPhone(String baseLinkPhone){
		this.baseLinkPhone=baseLinkPhone;
	}
	public void setBaseFax(String baseFax){
		this.baseFax=baseFax;
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
