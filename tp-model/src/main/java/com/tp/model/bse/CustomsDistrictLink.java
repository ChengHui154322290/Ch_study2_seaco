package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 对接海关数据地区信息-系统地区信息关联表
  */
public class CustomsDistrictLink extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1460530107719L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**系统中地区编码ID 数据类型bigint(20)*/
	private Long districtId;
	
	/**对接海关地区编号ID 数据类型bigint(20)*/
	private Long customsDistrictId;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getDistrictId(){
		return districtId;
	}
	public Long getCustomsDistrictId(){
		return customsDistrictId;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setDistrictId(Long districtId){
		this.districtId=districtId;
	}
	public void setCustomsDistrictId(Long customsDistrictId){
		this.customsDistrictId=customsDistrictId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
