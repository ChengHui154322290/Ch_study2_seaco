package com.tp.model.sup;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 合同变更主表
  */
public class ContractChange extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836274730L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**合同id 数据类型bigint(11)*/
	private Long contractId;
	
	/**合同编号(预留) 数据类型varchar(100)*/
	private String contractCode;
	
	/**合同名称 数据类型varchar(80)*/
	private String contractName;
	
	/**变更类型 数据类型varchar(45)*/
	private String changeType;
	
	/**有效期-开始日期 数据类型datetime*/
	private Date startDate;
	
	/**有效期-结束日期 数据类型datetime*/
	private Date endDate;
	
	/**供应商编号 数据类型bigint(11)*/
	private Long supplierId;
	
	/**供应商名称 数据类型varchar(80)*/
	private String supplierName;
	
	/**审核状态 数据类型tinyint(4)*/
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
	
	
	public Long getId(){
		return id;
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
	public String getChangeType(){
		return changeType;
	}
	public Date getStartDate(){
		return startDate;
	}
	public Date getEndDate(){
		return endDate;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public String getSupplierName(){
		return supplierName;
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
	public void setContractId(Long contractId){
		this.contractId=contractId;
	}
	public void setContractCode(String contractCode){
		this.contractCode=contractCode;
	}
	public void setContractName(String contractName){
		this.contractName=contractName;
	}
	public void setChangeType(String changeType){
		this.changeType=changeType;
	}
	public void setStartDate(Date startDate){
		this.startDate=startDate;
	}
	public void setEndDate(Date endDate){
		this.endDate=endDate;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setSupplierName(String supplierName){
		this.supplierName=supplierName;
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
