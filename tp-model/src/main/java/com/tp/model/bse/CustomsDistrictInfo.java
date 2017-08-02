package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 对接海关数据地区信息表
  */
public class CustomsDistrictInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1460530107718L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**对接海关标识 数据类型varchar(20)*/
	private String customs;
	
	/**对应代码 数据类型varchar(20)*/
	private String code;
	
	/**地区类型：0大洲1国家2其他 数据类型tinyint(1)*/
	private Integer type;
	
	/**地区或国家名称 数据类型varchar(255)*/
	private String name;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间 数据类型datetime*/
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
	public Integer getType(){
		return type;
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
	public void setType(Integer type){
		this.type=type;
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
