package com.tp.model.sup;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 供应商-旗舰店设置
  */
public class FlagshipInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836567623L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**供应商id 数据类型bigint(11)*/
	private Long supplierId;
	
	/**供应商或商家名称 数据类型varchar(80)*/
	private String supplierName;
	
	/**旗舰店名称 数据类型varchar(80)*/
	private String flagshipName;
	
	/**旗舰店类型(官方旗舰店,品牌小家) 数据类型varchar(45)*/
	private String flagshipType;
	
	/**授权书地址 数据类型varchar(100)*/
	private String certificateUrl;
	
	/**logo地址 数据类型varchar(100)*/
	private String logoUrl;
	
	/** 数据类型datetime*/
	private Date startTime;
	
	/** 数据类型datetime*/
	private Date endTime;
	
	/**备注 数据类型varchar(255)*/
	private String remark;
	
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
	
	
	public Long getId(){
		return id;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public String getSupplierName(){
		return supplierName;
	}
	public String getFlagshipName(){
		return flagshipName;
	}
	public String getFlagshipType(){
		return flagshipType;
	}
	public String getCertificateUrl(){
		return certificateUrl;
	}
	public String getLogoUrl(){
		return logoUrl;
	}
	public Date getStartTime(){
		return startTime;
	}
	public Date getEndTime(){
		return endTime;
	}
	public String getRemark(){
		return remark;
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
	public void setFlagshipName(String flagshipName){
		this.flagshipName=flagshipName;
	}
	public void setFlagshipType(String flagshipType){
		this.flagshipType=flagshipType;
	}
	public void setCertificateUrl(String certificateUrl){
		this.certificateUrl=certificateUrl;
	}
	public void setLogoUrl(String logoUrl){
		this.logoUrl=logoUrl;
	}
	public void setStartTime(Date startTime){
		this.startTime=startTime;
	}
	public void setEndTime(Date endTime){
		this.endTime=endTime;
	}
	public void setRemark(String remark){
		this.remark=remark;
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
