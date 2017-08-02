package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 对接海关数据计量单位信息表
  */
public class CustomsUnitInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1460530107719L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**海关类型 数据类型varchar(255)*/
	private String customs;
	
	/**编码 数据类型varchar(30)*/
	private String code;
	
	/**中文名称 数据类型varchar(50)*/
	private String name;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型datetime*/
	private Date modifyTime;
	
	
	public Long getId(){
		return id;
	}
	public String getCustoms(){
		return customs;
	}
	public String getCode(){
		return code;
	}
	public String getName(){
		return name;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setCustoms(String customs){
		this.customs=customs;
	}
	public void setCode(String code){
		this.code=code;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
}
