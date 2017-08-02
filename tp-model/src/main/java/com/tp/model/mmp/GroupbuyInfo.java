package com.tp.model.mmp;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class GroupbuyInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1458022853903L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/** 数据类型bigint(20)*/
	private Long topicId;
	
	/** 数据类型bigint(20)*/
	private Long topicItemId;
	
	/** 数据类型tinyint(1)*/
	private Integer type;
	
	/** 数据类型int(11)*/
	private Integer memberLimit;
	
	/** 数据类型int(11)*/
	private Integer duration;
	
	/** 数据类型decimal(10,0)*/
	private Double groupPrice;
	
	/** 数据类型varchar(255)*/
	private String createUser;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型varchar(255)*/
	private String updateUser;
	
	/** 数据类型datetime*/
	private Date updateTime;

	private Integer sort;

	private String introduce;

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Long getId(){
		return id;
	}
	public Long getTopicId(){
		return topicId;
	}
	public Long getTopicItemId(){
		return topicItemId;
	}
	public Integer getType(){
		return type;
	}
	public Integer getMemberLimit(){
		return memberLimit;
	}
	public Integer getDuration(){
		return duration;
	}
	public Double getGroupPrice(){
		return groupPrice;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setTopicId(Long topicId){
		this.topicId=topicId;
	}
	public void setTopicItemId(Long topicItemId){
		this.topicItemId=topicItemId;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setMemberLimit(Integer memberLimit){
		this.memberLimit=memberLimit;
	}
	public void setDuration(Integer duration){
		this.duration=duration;
	}
	public void setGroupPrice(Double groupPrice){
		this.groupPrice=groupPrice;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
}
