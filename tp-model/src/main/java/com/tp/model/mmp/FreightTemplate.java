package com.tp.model.mmp;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class FreightTemplate extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579107L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**计算方式 0-按全场 1-按单品 数据类型int(10)*/
	private Integer calculateMode;
	
	/** 数据类型bigint(20)*/
	private Long creatorId;
	
	/**模板名称 数据类型varchar(50)*/
	private String name;
	
	/**类型 0-国内 1-海淘 数据类型int(10)*/
	private Integer freightType;
	
	/**邮费 数据类型float(10,0)*/
	private Float postage;
	
	/**满x元包邮 数据类型float(10,0)*/
	private Float freePostage;
	
	/**满减后邮费 数据类型float(10,0)*/
	private Float aftPostage;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型datetime*/
	private Date modifyTime;
	
	/** 数据类型datetime*/
	private Date deleteTime;
	
	/**是否删除 数据类型tinyint(1)*/
	private Integer isDelete;
	
	
	public Long getId(){
		return id;
	}
	public Integer getCalculateMode(){
		return calculateMode;
	}
	public Long getCreatorId(){
		return creatorId;
	}
	public String getName(){
		return name;
	}
	public Integer getFreightType(){
		return freightType;
	}
	public Float getPostage(){
		return postage;
	}
	public Float getFreePostage(){
		return freePostage;
	}
	public Float getAftPostage(){
		return aftPostage;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public Date getDeleteTime(){
		return deleteTime;
	}
	public Integer getIsDelete(){
		return isDelete;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setCalculateMode(Integer calculateMode){
		this.calculateMode=calculateMode;
	}
	public void setCreatorId(Long creatorId){
		this.creatorId=creatorId;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setFreightType(Integer freightType){
		this.freightType=freightType;
	}
	public void setPostage(Float postage){
		this.postage=postage;
	}
	public void setFreePostage(Float freePostage){
		this.freePostage=freePostage;
	}
	public void setAftPostage(Float aftPostage){
		this.aftPostage=aftPostage;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public void setDeleteTime(Date deleteTime){
		this.deleteTime=deleteTime;
	}
	public void setIsDelete(Integer isDelete){
		this.isDelete=isDelete;
	}
}
