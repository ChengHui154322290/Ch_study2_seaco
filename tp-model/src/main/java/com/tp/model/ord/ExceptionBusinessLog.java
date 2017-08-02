package com.tp.model.ord;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 异常业务日志表
  */
public class ExceptionBusinessLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451468597509L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**日志类型-(1＝支付成功业务。。。) 数据类型tinyint(3)*/
	private Integer type;
	
	/**状态（0=未处理、新建，1=已处理） 数据类型tinyint(3)*/
	private Integer status;
	
	/**业务编号 数据类型varchar(20)*/
	private String bizCode;
	
	/**日志内容 数据类型varchar(200)*/
	private String content;
	
	/**处理次数 数据类型int(11)*/
	private Integer processCount;
	
	/**完成时间 数据类型datetime*/
	private Date doneTime;
	
	/**备用字段 数据类型varchar(45)*/
	private String bacCode;
	
	/**备用字段 数据类型varchar(45)*/
	private String bacCode2;
	
	/**备用字段 数据类型varchar(45)*/
	private String bacCode3;
	
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
	public Integer getType(){
		return type;
	}
	public Integer getStatus(){
		return status;
	}
	public String getBizCode(){
		return bizCode;
	}
	public String getContent(){
		return content;
	}
	public Integer getProcessCount(){
		return processCount;
	}
	public Date getDoneTime(){
		return doneTime;
	}
	public String getBacCode(){
		return bacCode;
	}
	public String getBacCode2(){
		return bacCode2;
	}
	public String getBacCode3(){
		return bacCode3;
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
	public void setType(Integer type){
		this.type=type;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setBizCode(String bizCode){
		this.bizCode=bizCode;
	}
	public void setContent(String content){
		this.content=content;
	}
	public void setProcessCount(Integer processCount){
		this.processCount=processCount;
	}
	public void setDoneTime(Date doneTime){
		this.doneTime=doneTime;
	}
	public void setBacCode(String bacCode){
		this.bacCode=bacCode;
	}
	public void setBacCode2(String bacCode2){
		this.bacCode2=bacCode2;
	}
	public void setBacCode3(String bacCode3){
		this.bacCode3=bacCode3;
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
