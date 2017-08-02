package com.tp.model.mmp;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class TopicOperateLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1474253459743L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/** 数据类型bigint(20)*/
	private Long topicId;
	
	/** 数据类型varchar(32)*/
	private String type;
	
	/**变更内容 数据类型varchar(255)*/
	private String content;
	
	/** 数据类型varchar(255)*/
	private String remark;
	
	/** 数据类型bigint(20)*/
	private Long createUserId;
	
	/** 数据类型varchar(255)*/
	private String createUserName;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getTopicId(){
		return topicId;
	}
	public String getType(){
		return type;
	}
	public String getContent(){
		return content;
	}
	public String getRemark(){
		return remark;
	}
	public Long getCreateUserId(){
		return createUserId;
	}
	public String getCreateUserName(){
		return createUserName;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setTopicId(Long topicId){
		this.topicId=topicId;
	}
	public void setType(String type){
		this.type=type;
	}
	public void setContent(String content){
		this.content=content;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setCreateUserId(Long createUserId){
		this.createUserId=createUserId;
	}
	public void setCreateUserName(String createUserName){
		this.createUserName=createUserName;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
