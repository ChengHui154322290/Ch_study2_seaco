package com.tp.model.sup;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 合同主表
  */
public class Contract extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836274729L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**供应商编号 数据类型bigint(11)*/
	private Long supplierId;
	
	/**供应商名称 数据类型varchar(80)*/
	private String supplierName;
	
	/**是否海淘 数据类型tinyint(1)*/
	private Integer isSea;
	
	/**合同类型（同供应商类型） 数据类型varchar(45)*/
	private String contractType;
	
	/**合同编号(预留) 数据类型varchar(100)*/
	private String contractCode;
	
	/**合同名称 数据类型varchar(80)*/
	private String contractName;
	
	/**合同备注 数据类型varchar(255)*/
	private String contractDesc;
	
	/**签约日期 数据类型datetime*/
	private Date signingDate;
	
	/**有效期-开始日期 数据类型datetime*/
	private Date startDate;
	
	/**有效期-结束日期 数据类型datetime*/
	private Date endDate;
	
	/**合同主体(甲方) 数据类型varchar(45)*/
	private String contractXg;
	
	/**销售渠道 数据类型varchar(45)*/
	private String salesChannels;
	
	/**合同模板类型 数据类型varchar(45)*/
	private String templateType;
	
	/**合同模板名称 数据类型varchar(45)*/
	private String templateName;
	
	/**保证金 数据类型double*/
	private Double cash;
	
	/**币种 数据类型varchar(45)*/
	private String currency;
	
	/**是否协议合同（1：是 0：否） 数据类型tinyint(1)*/
	private Integer isAgreementContract;
	
	/**协议合同名称 数据类型varchar(80)*/
	private String agreementContractName;
	
	/**协议合同地址 数据类型varchar(100)*/
	private String agreementContractUrl;
	
	/**签约人id 数据类型varchar(80)*/
	private Long contractorId;
	
	/**签约人姓名 数据类型varchar(100)*/
	private String contractorName;
	
	/**签约人部门id 数据类型varchar(80)*/
	private String contractorDeptId;
	
	/**签约人部门名称 数据类型varchar(100)*/
	private String contractorDeptName;
	
	/**签约人邮箱地址 数据类型varchar(80)*/
	private String contractorEmail;
	
	/**签约人联系电话 数据类型varchar(20)*/
	private String contractorPhone;
	
	/**审核状态 数据类型int(8)*/
	private Integer auditStatus;
	
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
	
	public String getContractTypeName(){
		if(contractType!=null){
			return SupplierConstant.SUPPLIER_TYPES.get(contractType);
		}
		return null;
	}
	
	public Long getId(){
		return id;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public String getSupplierName(){
		return supplierName;
	}
	public Integer getIsSea(){
		return isSea;
	}
	public String getContractType(){
		return contractType;
	}
	public String getContractCode(){
		return contractCode;
	}
	public String getContractName(){
		return contractName;
	}
	public String getContractDesc(){
		return contractDesc;
	}
	public Date getSigningDate(){
		return signingDate;
	}
	public Date getStartDate(){
		return startDate;
	}
	public Date getEndDate(){
		return endDate;
	}
	public String getContractXg(){
		return contractXg;
	}
	public String getSalesChannels(){
		return salesChannels;
	}
	public String getTemplateType(){
		return templateType;
	}
	public String getTemplateName(){
		return templateName;
	}
	public Double getCash(){
		return cash;
	}
	public String getCurrency(){
		return currency;
	}
	public Integer getIsAgreementContract(){
		return isAgreementContract;
	}
	public String getAgreementContractName(){
		return agreementContractName;
	}
	public String getAgreementContractUrl(){
		return agreementContractUrl;
	}
	public Long getContractorId(){
		return contractorId;
	}
	public String getContractorName(){
		return contractorName;
	}
	public String getContractorDeptId(){
		return contractorDeptId;
	}
	public String getContractorDeptName(){
		return contractorDeptName;
	}
	public String getContractorEmail(){
		return contractorEmail;
	}
	public String getContractorPhone(){
		return contractorPhone;
	}
	public Integer getAuditStatus(){
		return auditStatus;
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
	public void setSupplierName(String supplierName){
		this.supplierName=supplierName;
	}
	public void setIsSea(Integer isSea){
		this.isSea=isSea;
	}
	public void setContractType(String contractType){
		this.contractType=contractType;
	}
	public void setContractCode(String contractCode){
		this.contractCode=contractCode;
	}
	public void setContractName(String contractName){
		this.contractName=contractName;
	}
	public void setContractDesc(String contractDesc){
		this.contractDesc=contractDesc;
	}
	public void setSigningDate(Date signingDate){
		this.signingDate=signingDate;
	}
	public void setStartDate(Date startDate){
		this.startDate=startDate;
	}
	public void setEndDate(Date endDate){
		this.endDate=endDate;
	}
	public void setContractXg(String contractXg){
		this.contractXg=contractXg;
	}
	public void setSalesChannels(String salesChannels){
		this.salesChannels=salesChannels;
	}
	public void setTemplateType(String templateType){
		this.templateType=templateType;
	}
	public void setTemplateName(String templateName){
		this.templateName=templateName;
	}
	public void setCash(Double cash){
		this.cash=cash;
	}
	public void setCurrency(String currency){
		this.currency=currency;
	}
	public void setIsAgreementContract(Integer isAgreementContract){
		this.isAgreementContract=isAgreementContract;
	}
	public void setAgreementContractName(String agreementContractName){
		this.agreementContractName=agreementContractName;
	}
	public void setAgreementContractUrl(String agreementContractUrl){
		this.agreementContractUrl=agreementContractUrl;
	}
	public void setContractorId(Long contractorId){
		this.contractorId=contractorId;
	}
	public void setContractorName(String contractorName){
		this.contractorName=contractorName;
	}
	public void setContractorDeptId(String contractorDeptId){
		this.contractorDeptId=contractorDeptId;
	}
	public void setContractorDeptName(String contractorDeptName){
		this.contractorDeptName=contractorDeptName;
	}
	public void setContractorEmail(String contractorEmail){
		this.contractorEmail=contractorEmail;
	}
	public void setContractorPhone(String contractorPhone){
		this.contractorPhone=contractorPhone;
	}
	public void setAuditStatus(Integer auditStatus){
		this.auditStatus=auditStatus;
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
