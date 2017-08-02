package com.tp.model.sup;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 合同-费用明细表
  */
public class ContractCost extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836274731L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/** 数据类型bigint(11)*/
	private Long contractId;
	
	/**费用类型（押金、平台使用费） 数据类型varchar(45)*/
	private String type;
	
	/**费用 数据类型double*/
	private Double value;
	
	/**币种 数据类型varchar(45)*/
	private String currency;
	
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
	public String getType(){
		return type;
	}
	public Double getValue(){
		return value;
	}
	public String getCurrency(){
		return currency;
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
	public void setType(String type){
		this.type=type;
	}
	public void setValue(Double value){
		this.value=value;
	}
	public void setCurrency(String currency){
		this.currency=currency;
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
