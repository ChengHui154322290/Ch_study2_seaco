package com.tp.model.usr;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class RoleMenuLimit extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451892616621L;

	/** 数据类型bigint(14)*/
	@Id
	private Long id;
	
	/** 数据类型bigint(14)*/
	private Long roleId;
	
	/** 数据类型bigint(14)*/
	private Long sysMenuLimitId;
	
	public RoleMenuLimit(){
	}
	public RoleMenuLimit(Long roleId, Long sysMenuLimitId) {
		this.roleId =roleId;
		this.sysMenuLimitId = sysMenuLimitId;
	}
	public Long getId(){
		return id;
	}
	public Long getRoleId(){
		return roleId;
	}
	public Long getSysMenuLimitId(){
		return sysMenuLimitId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setRoleId(Long roleId){
		this.roleId=roleId;
	}
	public void setSysMenuLimitId(Long sysMenuLimitId){
		this.sysMenuLimitId=sysMenuLimitId;
	}
}
