package com.tp.model.app;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 记录程序崩溃的信息。
  */
public class StatisticCrash extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450431557815L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**异常崩溃的名字（裁剪100字符） 数据类型varchar(100)*/
	private String name;
	
	/**异常崩溃的位置 异常崩溃的位置（裁剪100字符） 数据类型varchar(100)*/
	private String content;
	
	/**异常崩溃的详情	进行Base64加密 数据类型varchar(512)*/
	private String detail;
	
	/**异常崩溃的对应模块 数据类型varchar(100)*/
	private String module;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/**公共参数表 id 数据类型bigint(20)*/
	private Long staBaseId;
	
	
	public Long getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public String getContent(){
		return content;
	}
	public String getDetail(){
		return detail;
	}
	public String getModule(){
		return module;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Long getStaBaseId(){
		return staBaseId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setContent(String content){
		this.content=content;
	}
	public void setDetail(String detail){
		this.detail=detail;
	}
	public void setModule(String module){
		this.module=module;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setStaBaseId(Long staBaseId){
		this.staBaseId=staBaseId;
	}
}
