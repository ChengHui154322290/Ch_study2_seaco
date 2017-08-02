package com.tp.model.pay;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class SeagoorPayRefundInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1480052311775L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/** 数据类型varchar(64)*/
	private String merchantId;
	
	/** 数据类型varchar(64)*/
	private String merTradeCode;
	
	/** 数据类型varchar(64)*/
	private String merRefundCode;
	
	/** 数据类型varchar(64)*/
	private String paymentCode;
	
	/** 数据类型bigint(20)*/
	private Long memberId;
	
	/** 数据类型varchar(64)*/
	private String refundCode;
	
	/** 数据类型tinyint(255)*/
	private Integer status;
	
	/** 数据类型int(255)*/
	private Integer totalFee;
	
	/** 数据类型int(255)*/
	private Integer refundFee;
	
	/** 数据类型varchar(32)*/
	private String ip;
	
	/** 数据类型varchar(32)*/
	private String deviceInfo;
	
	/** 数据类型varchar(32)*/
	private String operatorId;
	
	/** 数据类型varchar(255)*/
	private String createUser;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型varchar(255)*/
	private String updateUser;
	
	/** 数据类型datetime*/
	private Date updateTime;
	
	/** 数据类型varchar(255)*/
	private String remark;
	
	
	public Long getId(){
		return id;
	}
	public String getMerchantId(){
		return merchantId;
	}
	public String getMerTradeCode(){
		return merTradeCode;
	}
	public String getMerRefundCode(){
		return merRefundCode;
	}
	public String getPaymentCode(){
		return paymentCode;
	}
	public Long getMemberId(){
		return memberId;
	}
	public String getRefundCode(){
		return refundCode;
	}
	public Integer getStatus(){
		return status;
	}
	public Integer getTotalFee(){
		return totalFee;
	}
	public Integer getRefundFee(){
		return refundFee;
	}
	public String getIp(){
		return ip;
	}
	public String getDeviceInfo(){
		return deviceInfo;
	}
	public String getOperatorId(){
		return operatorId;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getRemark(){
		return remark;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setMerchantId(String merchantId){
		this.merchantId=merchantId;
	}
	public void setMerTradeCode(String merTradeCode){
		this.merTradeCode=merTradeCode;
	}
	public void setMerRefundCode(String merRefundCode){
		this.merRefundCode=merRefundCode;
	}
	public void setPaymentCode(String paymentCode){
		this.paymentCode=paymentCode;
	}
	public void setMemberId(Long memberId){
		this.memberId=memberId;
	}
	public void setRefundCode(String refundCode){
		this.refundCode=refundCode;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setTotalFee(Integer totalFee){
		this.totalFee=totalFee;
	}
	public void setRefundFee(Integer refundFee){
		this.refundFee=refundFee;
	}
	public void setIp(String ip){
		this.ip=ip;
	}
	public void setDeviceInfo(String deviceInfo){
		this.deviceInfo=deviceInfo;
	}
	public void setOperatorId(String operatorId){
		this.operatorId=operatorId;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
}
