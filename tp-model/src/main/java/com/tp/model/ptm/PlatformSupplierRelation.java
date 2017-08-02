package com.tp.model.ptm;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 开放平台供应商关联表
  */
public class PlatformSupplierRelation extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450411493008L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**平台账号ID 数据类型bigint(20)*/
	private Long accountId;
	
	/**供应商ID 数据类型bigint(20)*/
	private Long supplierId;
	
	/**供应商编号 数据类型varchar(45)*/
	private String supplierCode;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型bigint(20)*/
	private Long createUserId;
	
	
	public Long getId(){
		return id;
	}
	public Long getAccountId(){
		return accountId;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public String getSupplierCode(){
		return supplierCode;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Long getCreateUserId(){
		return createUserId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setAccountId(Long accountId){
		this.accountId=accountId;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setSupplierCode(String supplierCode){
		this.supplierCode=supplierCode;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUserId(Long createUserId){
		this.createUserId=createUserId;
	}
}
