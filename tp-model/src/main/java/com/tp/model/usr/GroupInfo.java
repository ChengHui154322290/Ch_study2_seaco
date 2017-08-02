package com.tp.model.usr;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 资源组信息表
  */
public class GroupInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1441690271330L;

	 /**组ID 数据类型int(8)*/
	@Id
	private Integer groupId;
	
	 /**组名称 数据类型varchar(32)*/
	private String groupName;
	
	 /**备注 数据类型varchar(256)*/
	private String remark;
	
	 /**排序 数据类型int(8)*/
	private Integer sortNo;
	
	 /**创建时间 数据类型datetime*/
	private Date createTime;
	
	 /**创建者 数据类型varchar(32)*/
	private String createUser;
	
	 /**更新时间 数据类型datetime*/
	private Date updateTime;
	
	 /**更新者 数据类型varchar(32)*/
	private String updateUser;
	
	
	public Integer getGroupId(){
		return groupId;
	}
	public String getGroupName(){
		return groupName;
	}
	public String getRemark(){
		return remark;
	}
	public Integer getSortNo(){
		return sortNo;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public void setGroupId(Integer groupId){
		this.groupId=groupId;
	}
	public void setGroupName(String groupName){
		this.groupName=groupName;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setSortNo(Integer sortNo){
		this.sortNo=sortNo;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
}
