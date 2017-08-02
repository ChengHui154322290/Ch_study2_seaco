package com.tp.model.sup;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 审批流设置主表
  */
public class Audit extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836274727L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**编号（预留） 数据类型varchar(100)*/
	private String auditCode;
	
	/**版本号 数据类型varchar(100)*/
	private String auditVersion;
	
	/**单据类型(供应商列表、合同、报价单、采购订单、采购退货单、代销订单、代销退货单、预约单) 数据类型varchar(60)*/
	private String billType;
	
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
	
	@Virtual
	private String keyWords;
	
	public Long getId(){
		return id;
	}
	public String getAuditCode(){
		return auditCode;
	}
	public String getAuditVersion(){
		return auditVersion;
	}
	public String getBillType(){
		return billType;
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
	public void setAuditCode(String auditCode){
		this.auditCode=auditCode;
	}
	public void setAuditVersion(String auditVersion){
		this.auditVersion=auditVersion;
	}
	public void setBillType(String billType){
		this.billType=billType;
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
	public String getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
}
