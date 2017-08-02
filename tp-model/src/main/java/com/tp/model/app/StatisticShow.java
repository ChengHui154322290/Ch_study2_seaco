package com.tp.model.app;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 展示量统计 用于统计移动端应用上用户点击某个模块或者进入某个页面的频次，各终端可以根据需要定义cli参数的值，如”首页“表示用户进入了首页，”搜索“表示用户进入了搜索页
  */
public class StatisticShow extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450431557816L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**cli 用户点击的页面标签字符串 如：“首页”、“搜索”等 数据类型varchar(45)*/
	private String pageTabName;
	
	/**平台类型 iOS和Android的老版本投递，区别终端类型， 数据类型varchar(100)*/
	private String platform;
	
	/**唯一设备表示符  iOS老版本特有投递字段，不再使用 数据类型varchar(32)*/
	private String uuid;
	
	/**MAC地址  iOS老版本单独投递，和公共参数字段相同 数据类型varchar(16)*/
	private String macAddress;
	
	/**应用程序版本号 iOS老版本单独投递，和公共参数字段相同 数据类型varchar(45)*/
	private String appVersion;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/**公共参数表 id 数据类型bigint(20)*/
	private Long staBaseId;
	
	
	public Long getId(){
		return id;
	}
	public String getPageTabName(){
		return pageTabName;
	}
	public String getPlatform(){
		return platform;
	}
	public String getUuid(){
		return uuid;
	}
	public String getMacAddress(){
		return macAddress;
	}
	public String getAppVersion(){
		return appVersion;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Long getStaBaseId(){
		return staBaseId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setPageTabName(String pageTabName){
		this.pageTabName=pageTabName;
	}
	public void setPlatform(String platform){
		this.platform=platform;
	}
	public void setUuid(String uuid){
		this.uuid=uuid;
	}
	public void setMacAddress(String macAddress){
		this.macAddress=macAddress;
	}
	public void setAppVersion(String appVersion){
		this.appVersion=appVersion;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setStaBaseId(Long staBaseId){
		this.staBaseId=staBaseId;
	}
}
