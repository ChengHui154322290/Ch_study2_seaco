package com.tp.model.sup;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 合同变更-商品表
  */
public class ContractChangeProduct extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836274730L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/** 数据类型bigint(11)*/
	private Long contractId;
	
	/**变更单id 数据类型bigint(11)*/
	private Long changeId;
	
	/**品牌编号 数据类型bigint(11)*/
	private Long brandId;
	
	/**品牌名称 数据类型varchar(80)*/
	private String brandName;
	
	/**大类 数据类型bigint(11)*/
	private Long bigId;
	
	/**大类 数据类型varchar(80)*/
	private String bigName;
	
	/**中类 数据类型bigint(11)*/
	private Long midId;
	
	/**中类 数据类型varchar(80)*/
	private String midName;
	
	/**小类 数据类型bigint(11)*/
	private Long smallId;
	
	/**小类 数据类型varchar(80)*/
	private String smallName;
	
	/**佣金（%） 数据类型double*/
	private Double commission;
	
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
	public Long getChangeId(){
		return changeId;
	}
	public Long getBrandId(){
		return brandId;
	}
	public String getBrandName(){
		return brandName;
	}
	public Long getBigId(){
		return bigId;
	}
	public String getBigName(){
		return bigName;
	}
	public Long getMidId(){
		return midId;
	}
	public String getMidName(){
		return midName;
	}
	public Long getSmallId(){
		return smallId;
	}
	public String getSmallName(){
		return smallName;
	}
	public Double getCommission(){
		return commission;
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
	public void setChangeId(Long changeId){
		this.changeId=changeId;
	}
	public void setBrandId(Long brandId){
		this.brandId=brandId;
	}
	public void setBrandName(String brandName){
		this.brandName=brandName;
	}
	public void setBigId(Long bigId){
		this.bigId=bigId;
	}
	public void setBigName(String bigName){
		this.bigName=bigName;
	}
	public void setMidId(Long midId){
		this.midId=midId;
	}
	public void setMidName(String midName){
		this.midName=midName;
	}
	public void setSmallId(Long smallId){
		this.smallId=smallId;
	}
	public void setSmallName(String smallName){
		this.smallName=smallName;
	}
	public void setCommission(Double commission){
		this.commission=commission;
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
