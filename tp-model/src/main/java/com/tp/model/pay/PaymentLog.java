package com.tp.model.pay;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 支付业务日志
  */
public class PaymentLog extends BaseDO implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7646464632635624152L;

	/**日志ID 数据类型bigint(20)*/
	@Id
	private Long logId;
	
	/**对象ID 数据类型bigint(20)*/
	private Long logObjectId;
	
	/**对象类型 数据类型int(11)*/
	private Integer logObjectType;
	
	/**操作名称 数据类型varchar(32)*/
	private String actionName;
	
	/**日志内容 数据类型varchar(4000)*/
	private String content;
	
	/**创建IP 数据类型varchar(32)*/
	private String createIp;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/** 用于分表(DATE(yy)) */
	@Virtual
	private String partTable;
	
	public String getPartTable() {
		return partTable;
	}

	public void setPartTable(String partTable) {
		this.partTable = partTable;
	}

	public PaymentLog(){	
	}
	
	public PaymentLog(Long logObjectId, Integer logObjectType,
			String actionName, String content, String createIp,
			Date createTime, String createUser) {
		super();
		this.logObjectId = logObjectId;
		this.logObjectType = logObjectType;
		this.actionName = actionName;
		this.content = content;
		this.createIp = createIp;
		this.createTime = createTime;
		this.createUser = createUser;
	}
	
	public Long getLogId(){
		return logId;
	}
	public Long getLogObjectId(){
		return logObjectId;
	}
	public Integer getLogObjectType(){
		return logObjectType;
	}
	public String getActionName(){
		return actionName;
	}
	public String getContent(){
		return content;
	}
	public String getCreateIp(){
		return createIp;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public void setLogId(Long logId){
		this.logId=logId;
	}
	public void setLogObjectId(Long logObjectId){
		this.logObjectId=logObjectId;
	}
	public void setLogObjectType(Integer logObjectType){
		this.logObjectType=logObjectType;
	}
	public void setActionName(String actionName){
		this.actionName=actionName;
	}
	public void setContent(String content){
		this.content=content;
	}
	public void setCreateIp(String createIp){
		this.createIp=createIp;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
}
