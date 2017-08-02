package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 对接海关数据计量单位信息-系统计量单位关联表
  */
public class CustomsUnitLink extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1460530107719L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**海关数据计量单位id 数据类型bigint(20)*/
	private Long customsUnitId;
	
	/**系统内计量单位id 数据类型bigint(20)*/
	private Long unitId;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getCustomsUnitId(){
		return customsUnitId;
	}
	public Long getUnitId(){
		return unitId;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setCustomsUnitId(Long customsUnitId){
		this.customsUnitId=customsUnitId;
	}
	public void setUnitId(Long unitId){
		this.unitId=unitId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
