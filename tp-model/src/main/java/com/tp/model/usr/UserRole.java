package com.tp.model.usr;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 用户角色关系表
  */
public class UserRole extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451892616621L;

	/** 数据类型bigint(14)*/
	@Id
	private Long id;
	
	/**用户id 数据类型bigint(14)*/
	private Long userId;
	
	/**角色id 数据类型bigint(14)*/
	private Long roleId;
	
	
	public Long getId(){
		return id;
	}
	public Long getUserId(){
		return userId;
	}
	public Long getRoleId(){
		return roleId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setUserId(Long userId){
		this.userId=userId;
	}
	public void setRoleId(Long roleId){
		this.roleId=roleId;
	}
}
