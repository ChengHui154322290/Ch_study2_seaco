package com.tp.model.usr;

import java.io.Serializable;

import com.tp.util.StringUtil;

public class QueryUserDto implements Serializable{
	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = 7671369159945304249L;

	private String userName;
	
	private String loginName;
	
	private Long roleId;
	
	private Long departmentId;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = StringUtil.isNullOrEmpty(userName)?null:userName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = StringUtil.isNullOrEmpty(loginName)?null:loginName;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	@Override
	public String toString() {
		return "QueryUserDto [userName=" + userName + ", loginName="
				+ loginName + ", roleId=" + roleId + ", departmentId="
				+ departmentId + "]";
	}
	
	
}
