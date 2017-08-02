package com.tp.model.pay;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class MerchantInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1480572451015L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/** 数据类型varchar(64)*/
	private String merchantName;
	
	/** 数据类型varchar(64)*/
	private String merchantId;
	
	/** 数据类型varchar(128)*/
	private String merchantKey;
	
	/** 数据类型tinyint(255)*/
	private Integer status;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public String getMerchantName(){
		return merchantName;
	}
	public String getMerchantId(){
		return merchantId;
	}
	public String getMerchantKey(){
		return merchantKey;
	}
	public Integer getStatus(){
		return status;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setMerchantName(String merchantName){
		this.merchantName=merchantName;
	}
	public void setMerchantId(String merchantId){
		this.merchantId=merchantId;
	}
	public void setMerchantKey(String merchantKey){
		this.merchantKey=merchantKey;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
