package com.tp.model.prd;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 商品供应商信息，仅用于自营商品
  */
public class ItemSkuSupplier extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450873698779L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**商品SKUid 数据类型bigint(20)*/
	private Long skuId;
	
	/** 数据类型bigint(20)*/
	private Long supplierId;
	
	/**供应商名称 数据类型varchar(255)*/
	private String supplierName;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getSkuId(){
		return skuId;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public String getSupplierName(){
		return supplierName;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setSkuId(Long skuId){
		this.skuId=skuId;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setSupplierName(String supplierName){
		this.supplierName=supplierName;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
