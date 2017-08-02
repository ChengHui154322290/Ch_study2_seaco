package com.tp.model.app;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class StatisticNewversion extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450431557815L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**最新版本号 数据类型varchar(20)*/
	private String newVersion;
	
	/** 数据类型datetime*/
	private Date modifyTime;
	
	/** 数据类型varchar(255)*/
	private String remark;
	
	
	public Long getId(){
		return id;
	}
	public String getNewVersion(){
		return newVersion;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public String getRemark(){
		return remark;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setNewVersion(String newVersion){
		this.newVersion=newVersion;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
}
