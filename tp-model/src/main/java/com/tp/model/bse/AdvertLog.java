package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author zhouguofeng
  * 广告链接操作日志
  */
public class AdvertLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1483430529514L;

	/**日志ID 数据类型bigint(20)*/
	@Id
	private Long logId;
	
	/**广告平台编码 数据类型varchar(64)*/
	private String advertPlatCode;
	
	/**广告平台名称 数据类型varchar(128)*/
	private String advertPlatName;
	
	/**访问客户IP 数据类型varchar(32)*/
	private String remoteAddr;
	/**页面URL*/
	private String url;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**更新人 数据类型varchar(32)*/
	private String updateUser;
	
	
	public Long getLogId(){
		return logId;
	}
	public String getAdvertPlatCode(){
		return advertPlatCode;
	}
	public String getAdvertPlatName(){
		return advertPlatName;
	}
	public String getRemoteAddr(){
		return remoteAddr;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public void setLogId(Long logId){
		this.logId=logId;
	}
	public void setAdvertPlatCode(String advertPlatCode){
		this.advertPlatCode=advertPlatCode;
	}
	public void setAdvertPlatName(String advertPlatName){
		this.advertPlatName=advertPlatName;
	}
	public void setRemoteAddr(String remoteAddr){
		this.remoteAddr=remoteAddr;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
