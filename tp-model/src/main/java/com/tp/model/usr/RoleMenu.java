package com.tp.model.usr;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 角色 菜单关系表
  */
public class RoleMenu extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451892616621L;

	/** 数据类型bigint(14)*/
	@Id
	private Long id;
	
	/**角色id 数据类型bigint(14)*/
	private Long roleId;
	
	/**菜单id 数据类型bigint(14)*/
	private Long sysMenuId;
	
	public RoleMenu(){
		
	}
	public RoleMenu(Long roleId, Long sysMenuId) {
		this.roleId = roleId;
		this.sysMenuId = sysMenuId;
	}
	public Long getId(){
		return id;
	}
	public Long getRoleId(){
		return roleId;
	}
	public Long getSysMenuId(){
		return sysMenuId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setRoleId(Long roleId){
		this.roleId=roleId;
	}
	public void setSysMenuId(Long sysMenuId){
		this.sysMenuId=sysMenuId;
	}
}
