package com.tp.model.app;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 各终端APP每次启动时投递一次，投递数据用于统计装机量数据，启动相关数据等
  */
public class StatisticStart extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450431557816L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**启动时的时间（时间戳）	时间戳，转换成秒为单位 数据类型datetime*/
	private Date startTime;
	
	/**0，正常退出；1，异常退出 数据类型smallint(2)*/
	private Integer exitTag;
	
	/**最后一次退出时的版本号 数据类型varchar(45)*/
	private String exitVersion;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/**公共参数表 id 数据类型bigint(20)*/
	private Long staBaseId;
	
	
	public Long getId(){
		return id;
	}
	public Date getStartTime(){
		return startTime;
	}
	public Integer getExitTag(){
		return exitTag;
	}
	public String getExitVersion(){
		return exitVersion;
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
	public void setStartTime(Date startTime){
		this.startTime=startTime;
	}
	public void setExitTag(Integer exitTag){
		this.exitTag=exitTag;
	}
	public void setExitVersion(String exitVersion){
		this.exitVersion=exitVersion;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setStaBaseId(Long staBaseId){
		this.staBaseId=staBaseId;
	}
}
