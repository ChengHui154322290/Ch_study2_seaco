package com.tp.model.ord;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.vo.OrderConstant.ORDER_STATUS;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 海外直邮订单操作日志表
  */
public class DerictOperatLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1494380584649L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**子订单编号 数据类型bigint(20)*/
	private Long orderCode;
	
	/**操作类型 推送-1，查询-2 数据类型tinyint(4)*/
	private Integer operatType;
	
	/**是否成功 成功-1，失败-0 数据类型tinyint(4)*/
	private Integer isSuccess;
	
	/**操作信息记录 数据类型varchar(255)*/
	private String operatMessage;
	
	/** 数据类型varchar(255)*/
	private String createUser;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	private String originalResult;
	
	/** 数据类型varchar(255)*/
	private String modifyUser;
	
	/** 数据类型datetime*/
	private Date modifyTime;
	
	public String getOperatTypeStr() {
		if(getOperatType()==1){
			return "推送订单";
		}else if(getOperatType()==2){
			return "获取运单信息";
		}else if(getOperatType()==3){
			return "取消订单";
		}
		return "";
	}
	
	public String getIsSuccessStr() {
		if(getIsSuccess()==0){
			return "失败";
		}else if(getIsSuccess()==1){
			return "成功";
		}
		return "";
	}
	
	public Long getId(){
		return id;
	}
	public Long getOrderCode(){
		return orderCode;
	}
	public Integer getOperatType(){
		return operatType;
	}
	public Integer getIsSuccess(){
		return isSuccess;
	}
	public String getOperatMessage(){
		return operatMessage;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setOrderCode(Long orderCode){
		this.orderCode=orderCode;
	}
	public void setOperatType(Integer operatType){
		this.operatType=operatType;
	}
	public void setIsSuccess(Integer isSuccess){
		this.isSuccess=isSuccess;
	}
	public void setOperatMessage(String operatMessage){
		this.operatMessage=operatMessage;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}

	public String getOriginalResult() {
		return originalResult;
	}

	public void setOriginalResult(String originalResult) {
		this.originalResult = originalResult;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
}
