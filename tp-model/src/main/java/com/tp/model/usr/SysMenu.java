package com.tp.model.usr;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 菜单表
  */
public class SysMenu extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451892616621L;

	/**菜单表id 数据类型bigint(14)*/
	@Id
	private Long id;
	
	/**菜单code 数据类型varchar(40)*/
	private String code;
	
	/**父菜单id，如果为空，则为根菜单 数据类型bigint(14)*/
	private Long parentId;
	
	/**菜单名称 数据类型varchar(20)*/
	private String name;
	
	/**菜单请求链接 数据类型varchar(100)*/
	private String url;
	
	/**所属 数据类型varchar(100)*/
	private String category;
	
	/**菜单类型（1：菜单  2：按钮） 数据类型int(5)*/
	private Integer menuType;
	
	/**状态 数据类型tinyint(4)*/
	private Integer status;
	
	/**创建人id 数据类型varchar(32)*/
	private String createUser;
	
	/**修改人id 数据类型varchar(32)*/
	private String updateUser;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	@Virtual
	private Long location;
	public Long getId(){
		return id;
	}
	public String getCode(){
		return code;
	}
	public Long getParentId(){
		return parentId;
	}
	public String getName(){
		return name;
	}
	public String getUrl(){
		return url;
	}
	public String getCategory(){
		return category;
	}
	public Integer getMenuType(){
		return menuType;
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
	public void setCode(String code){
		this.code=code;
	}
	public void setParentId(Long parentId){
		this.parentId=parentId;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setUrl(String url){
		this.url=url;
	}
	public void setCategory(String category){
		this.category=category;
	}
	public void setMenuType(Integer menuType){
		this.menuType=menuType;
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
	public Long getLocation() {
		return location;
	}
	public void setLocation(Long location) {
		this.location = location;
	}
}
