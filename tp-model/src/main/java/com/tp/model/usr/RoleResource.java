package com.tp.model.usr;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 角色权限表
  */
public class RoleResource extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1441690271331L;

	 /**角色权限ID 数据类型int(8)*/
	@Id
	private Long roleResourceId;
	
	 /**角色ID 数据类型int(8)*/
	private Long roleId;
	
	 /**权限ID 数据类型int(8)*/
	private Long resourceId;
	
	 /**创建时间 数据类型datetime*/
	private Date createTime;
	
	 /**创建者 数据类型varchar(32)*/
	private String createUser;

	public Long getRoleResourceId() {
		return roleResourceId;
	}

	public void setRoleResourceId(Long roleResourceId) {
		this.roleResourceId = roleResourceId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
}
