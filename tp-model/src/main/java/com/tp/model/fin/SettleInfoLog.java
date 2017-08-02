package com.tp.model.fin;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 结算日志表
  */
public class SettleInfoLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450405991706L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**供应商ID 数据类型bigint(20)*/
	private Long supplierId;
	
	/**结算编号 数据类型bigint(20)*/
	private Long settleNo;
	
	/**结算子项编号 数据类型bigint(20)*/
	private Long settleSubNo;
	
	/**日志类型 数据类型tinyint(2)*/
	private Integer logType;
	
	/**操作动作 数据类型tinyint(2)*/
	private Integer actionType;
	
	/**以前状态 数据类型int(2)*/
	private Integer oldStatus;
	
	/**操作后状态 数据类型int(2)*/
	private Integer currentStatus;
	
	/**日志内容 数据类型varchar(2000)*/
	private String context;
	
	/**操作者类型 数据类型tinyint(2)*/
	private Integer userType;
	
	/**操作者 数据类型varchar(32)*/
	private String createUser;
	
	/**记录时间 数据类型datetime*/
	private Date createDate;
	
	
	public Long getId(){
		return id;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public Long getSettleNo(){
		return settleNo;
	}
	public Long getSettleSubNo(){
		return settleSubNo;
	}
	public Integer getLogType(){
		return logType;
	}
	public Integer getActionType(){
		return actionType;
	}
	public Integer getOldStatus(){
		return oldStatus;
	}
	public Integer getCurrentStatus(){
		return currentStatus;
	}
	public String getContext(){
		return context;
	}
	public Integer getUserType(){
		return userType;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getCreateDate(){
		return createDate;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setSettleNo(Long settleNo){
		this.settleNo=settleNo;
	}
	public void setSettleSubNo(Long settleSubNo){
		this.settleSubNo=settleSubNo;
	}
	public void setLogType(Integer logType){
		this.logType=logType;
	}
	public void setActionType(Integer actionType){
		this.actionType=actionType;
	}
	public void setOldStatus(Integer oldStatus){
		this.oldStatus=oldStatus;
	}
	public void setCurrentStatus(Integer currentStatus){
		this.currentStatus=currentStatus;
	}
	public void setContext(String context){
		this.context=context;
	}
	public void setUserType(Integer userType){
		this.userType=userType;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}
}
