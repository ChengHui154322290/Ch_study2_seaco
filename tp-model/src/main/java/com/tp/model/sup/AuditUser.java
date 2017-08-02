package com.tp.model.sup;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 审批流设置-用户表
  */
public class AuditUser extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836274729L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/** 数据类型bigint(11)*/
	private Long auditId;
	
	/** 数据类型bigint(11)*/
	private Long userId;
	
	/** 数据类型varchar(80)*/
	private String userName;
	
	/**提交人/审批人 数据类型varchar(45)*/
	private String roleType;
	
	/**审批人级别（如果该用户角色为审批人） 数据类型varchar(45)*/
	private String auditLevel;
	
	/**审核状态 数据类型int(8)*/
	private Integer auditStatus;
	
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
	public Long getAuditId(){
		return auditId;
	}
	public Long getUserId(){
		return userId;
	}
	public String getUserName(){
		return userName;
	}
	public String getRoleType(){
		return roleType;
	}
	public String getAuditLevel(){
		return auditLevel;
	}
	public Integer getAuditStatus(){
		return auditStatus;
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
	public void setAuditId(Long auditId){
		this.auditId=auditId;
	}
	public void setUserId(Long userId){
		this.userId=userId;
	}
	public void setUserName(String userName){
		this.userName=userName;
	}
	public void setRoleType(String roleType){
		this.roleType=roleType;
	}
	public void setAuditLevel(String auditLevel){
		this.auditLevel=auditLevel;
	}
	public void setAuditStatus(Integer auditStatus){
		this.auditStatus=auditStatus;
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
