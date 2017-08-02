package com.tp.model.usr;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 部门表
  */
public class Department extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451892616618L;

	/**id 数据类型bigint(14)*/
	@Id
	private Long id;
	
	/**父部门id 数据类型bigint(14)*/
	private Long parentId;
	
	/**类别,用于查找整条分支的,是创建时候生成的完全随机的一段字符串 数据类型varchar(50)*/
	private String category;
	
	/**角色名称 数据类型varchar(50)*/
	private String name;
	
	/**状态 数据类型tinyint(4)*/
	private Integer status;
	
	/** 数据类型varchar(100)*/
	private String departDesc;
	
	/**新增角色用户id 数据类型varchar(32)*/
	private String createUser;
	
	/**修改角色用户id 数据类型varchar(32)*/
	private String updateUser;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getParentId(){
		return parentId;
	}
	public String getCategory(){
		return category;
	}
	public String getName(){
		return name;
	}
	public Integer getStatus(){
		return status;
	}
	public String getCreateUser(){
		return createUser;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setParentId(Long parentId){
		this.parentId=parentId;
	}
	public void setCategory(String category){
		this.category=category;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public String getDepartDesc() {
		return departDesc;
	}
	public void setDepartDesc(String departDesc) {
		this.departDesc = departDesc;
	}
}
