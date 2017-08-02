package com.tp.model.app;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 统计顶部推荐用户点击量
  */
public class StatisticTopRecommend extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450431557816L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**应用程序版本号 数据类型varchar(45)*/
	private String appVersion;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/**公共参数表 id 数据类型bigint(20)*/
	private Long staBaseId;
	
	
	public Long getId(){
		return id;
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
