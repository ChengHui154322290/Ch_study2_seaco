package com.tp.model.sup;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 合同-结算规则表
  */
public class ContractSettlementRule extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836274732L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/** 数据类型bigint(11)*/
	private Long contractId;
	
	/**结算规则类型(按活动周期结算等) 数据类型varchar(45)*/
	private String ruleType;
	
	/** 数据类型varchar(45)*/
	private String frequence;
	
	/** 数据类型varchar(45)*/
	private String day;
	
	/**工作日/自然日 数据类型varchar(45)*/
	private String dayType;
	
	/** 数据类型varchar(45)*/
	private String percent;
	
	/**状体（1：启用 0：禁用） 数据类型tinyint(1)*/
	private Integer status;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**创建操作者id 数据类型varchar(32)*/
	private String createUser;
	
	/**更新操作者id 数据类型varchar(32)*/
	private String updateUser;
	
	
	public Long getId(){
		return id;
	}
	public Long getContractId(){
		return contractId;
	}
	public String getRuleType(){
		return ruleType;
	}
	public String getFrequence(){
		return frequence;
	}
	public String getDay(){
		return day;
	}
	public String getDayType(){
		return dayType;
	}
	public String getPercent(){
		return percent;
	}
	public Integer getStatus(){
		return status;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setContractId(Long contractId){
		this.contractId=contractId;
	}
	public void setRuleType(String ruleType){
		this.ruleType=ruleType;
	}
	public void setFrequence(String frequence){
		this.frequence=frequence;
	}
	public void setDay(String day){
		this.day=day;
	}
	public void setDayType(String dayType){
		this.dayType=dayType;
	}
	public void setPercent(String percent){
		this.percent=percent;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
}
