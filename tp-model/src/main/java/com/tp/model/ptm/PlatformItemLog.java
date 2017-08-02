package com.tp.model.ptm;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 平台请求日志表
  */
public class PlatformItemLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1499939050069L;

	/** 数据类型int(20)*/
	@Id
	private Integer id;
	
	/**接口类型 数据类型varchar(50)*/
	private String type;
	
	/**请求数据 数据类型varchar(2000)*/
	private String content;
	
	/**返回数据 数据类型varchar(2000)*/
	private String response;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(50)*/
	private String createUser;
	
	
	public Integer getId(){
		return id;
	}
	public String getType(){
		return type;
	}
	public String getContent(){
		return content;
	}
	public String getResponse(){
		return response;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public void setId(Integer id){
		this.id=id;
	}
	public void setType(String type){
		this.type=type;
	}
	public void setContent(String content){
		this.content=content;
	}
	public void setResponse(String response){
		this.response=response;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
}
