package com.tp.model.pay;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class SeagoorPayInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1479955771557L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/** 数据类型varchar(64)*/
	private String paymentCode;
	
	/** 数据类型tinyint(255)*/
	private Integer bizType;
	
	/** 数据类型tinyint(255)*/
	private Integer status;
	
	/** 数据类型bigint(20)*/
	private Long memberId;
	
	/** 数据类型varchar(64)*/
	private String merchantId;
	
	/** 数据类型varchar(64)*/
	private String merTradeCode;
	
	/** 数据类型varchar(64)*/
	private String deviceInfo;
	
	/** 数据类型int(255)*/
	private Integer totalFee;
	
	/** 数据类型varchar(128)*/
	private String itemDesc;
	
	/** 数据类型varchar(500)*/
	private String itemDetail;
	
	/** 数据类型varchar(32)*/
	private String itemTag;
	
	/** 数据类型varchar(128)*/
	private String attach;
	
	/** 数据类型varchar(16)*/
	private String ip;
	
	/** 数据类型varchar(32)*/
	private String payCode;
	
	/** 数据类型varchar(64)*/
	private String randStr;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型datetime*/
	private Date updateTime;
	
	/** 数据类型varchar(32)*/
	private String createUser;
	
	/** 数据类型varchar(32)*/
	private String updateUser;
	
	
	public Long getId(){
		return id;
	}
	public String getPaymentCode(){
		return paymentCode;
	}
	public Integer getBizType(){
		return bizType;
	}
	public Integer getStatus(){
		return status;
	}
	public Long getMemberId(){
		return memberId;
	}
	public String getMerchantId(){
		return merchantId;
	}
	public String getMerTradeCode(){
		return merTradeCode;
	}
	public String getDeviceInfo(){
		return deviceInfo;
	}
	public Integer getTotalFee(){
		return totalFee;
	}
	public String getItemDesc(){
		return itemDesc;
	}
	public String getItemDetail(){
		return itemDetail;
	}
	public String getItemTag(){
		return itemTag;
	}
	public String getAttach(){
		return attach;
	}
	public String getIp(){
		return ip;
	}
	public String getPayCode(){
		return payCode;
	}
	public String getRandStr(){
		return randStr;
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
	public void setPaymentCode(String paymentCode){
		this.paymentCode=paymentCode;
	}
	public void setBizType(Integer bizType){
		this.bizType=bizType;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setMemberId(Long memberId){
		this.memberId=memberId;
	}
	public void setMerchantId(String merchantId){
		this.merchantId=merchantId;
	}
	public void setMerTradeCode(String merTradeCode){
		this.merTradeCode=merTradeCode;
	}
	public void setDeviceInfo(String deviceInfo){
		this.deviceInfo=deviceInfo;
	}
	public void setTotalFee(Integer totalFee){
		this.totalFee=totalFee;
	}
	public void setItemDesc(String itemDesc){
		this.itemDesc=itemDesc;
	}
	public void setItemDetail(String itemDetail){
		this.itemDetail=itemDetail;
	}
	public void setItemTag(String itemTag){
		this.itemTag=itemTag;
	}
	public void setAttach(String attach){
		this.attach=attach;
	}
	public void setIp(String ip){
		this.ip=ip;
	}
	public void setPayCode(String payCode){
		this.payCode=payCode;
	}
	public void setRandStr(String randStr){
		this.randStr=randStr;
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
