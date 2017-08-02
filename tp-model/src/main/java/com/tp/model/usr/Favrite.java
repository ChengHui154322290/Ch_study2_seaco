package com.tp.model.usr;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 会员的常用功能信息表
  */
public class Favrite extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451892616620L;

	/** 数据类型bigint(11) unsigned*/
	@Id
	private Long id;
	
	/**用户id 数据类型bigint(20)*/
	private Long userId;
	
	/**权限id 数据类型bigint(11)*/
	private Long menuId;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getUserId(){
		return userId;
	}
	public Long getMenuId(){
		return menuId;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setUserId(Long userId){
		this.userId=userId;
	}
	public void setMenuId(Long menuId){
		this.menuId=menuId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
