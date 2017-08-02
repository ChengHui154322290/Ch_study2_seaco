package com.tp.model.sup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 报价单主表
  */
public class QuotationInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836567624L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**报价单编号 数据类型varchar(100)*/
	private String quotationCode;
	
	/**报价单名称 数据类型varchar(80)*/
	private String quotationName;
	
	/** 数据类型bigint(11)*/
	private Long supplierId;
	
	/**供应商名称 数据类型varchar(80)*/
	private String supplierName;
	
	/**是否海淘 数据类型tinyint(1)*/
	private Integer isSea;
	
	/**合同编号 数据类型bigint(11)*/
	private Long contractId;
	
	/**合同编号 数据类型varchar(80)*/
	private String contractCode;
	
	/**合同名称 数据类型varchar(80)*/
	private String contractName;
	
	/**合同类型 数据类型varchar(45)*/
	private String contractType;
	
	/**报价单类型 数据类型varchar(45)*/
	private String quotationType;
	
	/**有效期-开始日期 数据类型datetime*/
	private Date startDate;
	
	/**有效期-结束日期 数据类型datetime*/
	private Date endDate;
	
	/**是否长期报价(0：短期；1：长期)，默认短期 数据类型tinyint(1)*/
	private Integer isForever;
	
	/**报价单备注 数据类型varchar(255)*/
	private String quotationDesc;
	
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
	
	@Virtual
	private List<QuotationProduct> quotationProductList = new ArrayList<QuotationProduct>();
	@Virtual
	/**供应商类型（自营/代销/联营/主供应商） 数据类型varchar(60)*/
	private String supplierType;
	
	public Long getId(){
		return id;
	}
	public String getQuotationCode(){
		return quotationCode;
	}
	public String getQuotationName(){
		return quotationName;
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
	public Long getContractId(){
		return contractId;
	}
	public String getContractCode(){
		return contractCode;
	}
	public String getContractName(){
		return contractName;
	}
	public String getContractType(){
		return contractType;
	}
	public String getQuotationType(){
		return quotationType;
	}
	public Date getStartDate(){
		return startDate;
	}
	public Date getEndDate(){
		return endDate;
	}
	public Integer getIsForever(){
		return isForever;
	}
	public String getQuotationDesc(){
		return quotationDesc;
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
	public void setQuotationCode(String quotationCode){
		this.quotationCode=quotationCode;
	}
	public void setQuotationName(String quotationName){
		this.quotationName=quotationName;
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
	public void setContractId(Long contractId){
		this.contractId=contractId;
	}
	public void setContractCode(String contractCode){
		this.contractCode=contractCode;
	}
	public void setContractName(String contractName){
		this.contractName=contractName;
	}
	public void setContractType(String contractType){
		this.contractType=contractType;
	}
	public void setQuotationType(String quotationType){
		this.quotationType=quotationType;
	}
	public void setStartDate(Date startDate){
		this.startDate=startDate;
	}
	public void setEndDate(Date endDate){
		this.endDate=endDate;
	}
	public void setIsForever(Integer isForever){
		this.isForever=isForever;
	}
	public void setQuotationDesc(String quotationDesc){
		this.quotationDesc=quotationDesc;
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
	public List<QuotationProduct> getQuotationProductList() {
		return quotationProductList;
	}
	public void setQuotationProductList(List<QuotationProduct> quotationProductList) {
		this.quotationProductList = quotationProductList;
	}
	public String getSupplierType() {
		return supplierType;
	}
	public void setSupplierType(String supplierType) {
		this.supplierType = supplierType;
	}
}
