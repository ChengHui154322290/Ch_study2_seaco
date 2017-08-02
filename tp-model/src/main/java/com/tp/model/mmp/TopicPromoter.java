package com.tp.model.mmp;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 专题对应分销渠道
  */
public class TopicPromoter extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1479887742588L;

	/**专题渠道ID 数据类型bigint(11)*/
	@Id
	private Long topicChannelId;
	
	/**专题ID 数据类型bigint(11)*/
	private Long topicId;
	
	/**店铺ID 数据类型bigint(11)*/
	private Long promoterId;
	
	/**店铺名称（冗余） 数据类型varchar(32)*/
	private String promoterName;
	
	/**分销渠道代码 数据类型varchar(16)*/
	private String channelCode;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	
	public Long getTopicChannelId(){
		return topicChannelId;
	}
	public Long getTopicId(){
		return topicId;
	}
	public Long getPromoterId(){
		return promoterId;
	}
	public String getPromoterName(){
		return promoterName;
	}
	public String getChannelCode(){
		return channelCode;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setTopicChannelId(Long topicChannelId){
		this.topicChannelId=topicChannelId;
	}
	public void setTopicId(Long topicId){
		this.topicId=topicId;
	}
	public void setPromoterId(Long promoterId){
		this.promoterId=promoterId;
	}
	public void setPromoterName(String promoterName){
		this.promoterName=promoterName;
	}
	public void setChannelCode(String channelCode){
		this.channelCode=channelCode;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
