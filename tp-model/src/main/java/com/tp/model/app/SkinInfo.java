package com.tp.model.app;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class SkinInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1484119125857L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**APP皮肤名称 数据类型varchar(64)*/
	private String name;
	
	/** 数据类型varchar(64)*/
	private String iconA;
	
	/** 数据类型varchar(64)*/
	private String iconASelected;
	
	/** 数据类型varchar(64)*/
	private String iconB;
	
	/** 数据类型varchar(64)*/
	private String iconBSelected;
	
	/** 数据类型varchar(64)*/
	private String iconC;
	
	/** 数据类型varchar(64)*/
	private String iconCSelected;
	
	/** 数据类型varchar(64)*/
	private String iconD;
	
	/** 数据类型varchar(64)*/
	private String iconDSelected;
	
	/** 数据类型varchar(64)*/
	private String tapBar;
	
	/** 数据类型varchar(12)*/
	private String unSelectedColor;
	
	/** 数据类型varchar(12)*/
	private String selectedColor;
	
	/**0禁用，1启用 数据类型tinyint(1)*/
	private Integer status;
	
	/**皮肤有效期 开始时间 数据类型datetime*/
	private Date startTime;
	
	/**皮肤有效期 结束时间 数据类型datetime*/
	private Date endTime;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型datetime*/
	private Date updateTime;
	
	/** 数据类型varchar(32)*/
	private String createUser;
	
	/** 数据类型varchar(32)*/
	private String updateUser;
	
	
	public Long getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public String getIconA(){
		return iconA;
	}
	public String getIconASelected(){
		return iconASelected;
	}
	public String getIconB(){
		return iconB;
	}
	public String getIconBSelected(){
		return iconBSelected;
	}
	public String getIconC(){
		return iconC;
	}
	public String getIconCSelected(){
		return iconCSelected;
	}
	public String getIconD(){
		return iconD;
	}
	public String getIconDSelected(){
		return iconDSelected;
	}
	public String getTapBar(){
		return tapBar;
	}
	public String getUnSelectedColor(){
		return unSelectedColor;
	}
	public String getSelectedColor(){
		return selectedColor;
	}
	public Integer getStatus(){
		return status;
	}
	public Date getStartTime(){
		return startTime;
	}
	public Date getEndTime(){
		return endTime;
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
	public void setIconA(String iconA){
		this.iconA=iconA;
	}
	public void setIconASelected(String iconASelected){
		this.iconASelected=iconASelected;
	}
	public void setIconB(String iconB){
		this.iconB=iconB;
	}
	public void setIconBSelected(String iconBSelected){
		this.iconBSelected=iconBSelected;
	}
	public void setIconC(String iconC){
		this.iconC=iconC;
	}
	public void setIconCSelected(String iconCSelected){
		this.iconCSelected=iconCSelected;
	}
	public void setIconD(String iconD){
		this.iconD=iconD;
	}
	public void setIconDSelected(String iconDSelected){
		this.iconDSelected=iconDSelected;
	}
	public void setTapBar(String tapBar){
		this.tapBar=tapBar;
	}
	public void setUnSelectedColor(String unSelectedColor){
		this.unSelectedColor=unSelectedColor;
	}
	public void setSelectedColor(String selectedColor){
		this.selectedColor=selectedColor;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setStartTime(Date startTime){
		this.startTime=startTime;
	}
	public void setEndTime(Date endTime){
		this.endTime=endTime;
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
}
