package com.tp.model.usr;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class RoleInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451892616620L;

	/**id 数据类型bigint(14)*/
	@Id
	private Long id;
	
	/**角色名称 数据类型varchar(50)*/
	private String name;
	
	/** 数据类型varchar(100)*/
	private String roleDesc;
	
	/**角色菜单关系展示所用 数据类型text*/
	private String roleMenuLimit;
	
	/**状态 数据类型tinyint(4)*/
	private Integer status;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	/** 数据类型varchar(32)*/
	private String createUser;
	
	/**修改角色用户id 数据类型varchar(32)*/
	private String updateUser;
	
	
	public Long getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public String getRoleMenuLimit(){
		return roleMenuLimit;
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
	public void setName(String name){
		this.name=name;
	}
	public void setRoleMenuLimit(String roleMenuLimit){
		this.roleMenuLimit=roleMenuLimit;
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
	public String getRoleDesc() {
		return roleDesc;
	}
	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}
}
