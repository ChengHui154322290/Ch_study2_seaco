package com.tp.model.sup;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 审核记录表(采购管理模块中所有单据的审核记录信息)
  */
public class AuditRecords extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450836274729L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**供应商/合同/采购订单 id 数据类型bigint(11)*/
	private Long auditId;
	
	/**单据类型 数据类型varchar(45)*/
	private String billType;
	
	/**审核结果 数据类型varchar(100)*/
	private String operate;
	
	/**审核结果标题 数据类型varchar(255)*/
	private String title;
	
	/**审核意见 数据类型text*/
	private String content;
	
	/**审核状态 数据类型int(8)*/
	private Integer auditStatus;
	
	/**创建该备注的用户（审核人）id 数据类型bigint(11)*/
	private Long userId;
	
	/**创建该备注的用户名 数据类型varchar(80)*/
	private String userName;
	
	/**创建该备注的角色id 数据类型bigint(11)*/
	private Long roleId;
	
	/**创建该备注的角色名称 数据类型varchar(80)*/
	private String roleName;
	
	/**状体（1：启用 0：禁用） 数据类型tinyint(1)*/
	private Integer status;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型datetime*/
	private Date updateTime;
	
	/**创建人id 数据类型varchar(32)*/
	private String createUser;
	
	/**更新人id 数据类型varchar(32)*/
	private String updateUser;
	
	
	public Long getId(){
		return id;
	}
	public Long getAuditId(){
		return auditId;
	}
	public String getBillType(){
		return billType;
	}
	public String getOperate(){
		return operate;
	}
	public String getTitle(){
		return title;
	}
	public String getContent(){
		return content;
	}
	public Integer getAuditStatus(){
		return auditStatus;
	}
	public Long getUserId(){
		return userId;
	}
	public String getUserName(){
		return userName;
	}
	public Long getRoleId(){
		return roleId;
	}
	public String getRoleName(){
		return roleName;
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
	public void setBillType(String billType){
		this.billType=billType;
	}
	public void setOperate(String operate){
		this.operate=operate;
	}
	public void setTitle(String title){
		this.title=title;
	}
	public void setContent(String content){
		this.content=content;
	}
	public void setAuditStatus(Integer auditStatus){
		this.auditStatus=auditStatus;
	}
	public void setUserId(Long userId){
		this.userId=userId;
	}
	public void setUserName(String userName){
		this.userName=userName;
	}
	public void setRoleId(Long roleId){
		this.roleId=roleId;
	}
	public void setRoleName(String roleName){
		this.roleName=roleName;
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
