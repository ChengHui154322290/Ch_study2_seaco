package com.tp.model.fin;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 风险管理结算间隔天数配置日志表
  */
public class SettleIntervalLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450405991707L;

	/**日志主键 数据类型bigint(20)*/
	@Id
	private Long logId;
	
	/**配置ID 数据类型bigint(20)*/
	private Long intervalId;
	
	/**供应商ID 数据类型bigint(20)*/
	private Long supplierId;
	
	/**日志内容 数据类型varchar(1000)*/
	private String context;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**创建时间 数据类型datetime*/
	private Date createDate;
	
	
	public Long getLogId(){
		return logId;
	}
	public Long getIntervalId(){
		return intervalId;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public String getContext(){
		return context;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getCreateDate(){
		return createDate;
	}
	public void setLogId(Long logId){
		this.logId=logId;
	}
	public void setIntervalId(Long intervalId){
		this.intervalId=intervalId;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setContext(String context){
		this.context=context;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}
}
