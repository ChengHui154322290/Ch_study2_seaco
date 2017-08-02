package com.tp.model.fin;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 订单商品项结算信息推送日志
  */
public class SettlePushOrderLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450405991707L;

	/**主键 数据类型bigint(14)*/
	@Id
	private Long id;
	
	/**动活ID 数据类型bigint(14)*/
	private Long topicId;
	
	/**活动名称 数据类型varchar(150)*/
	private String topicName;
	
	/**推送失败条数 数据类型int(6)*/
	private Integer errorCount;
	
	/**推送成功条数 数据类型int(6)*/
	private Integer pushCount;
	
	/**日志内容(失败订单商品项、失败原因、消耗时间，推送起始时间) 数据类型varchar(2000)*/
	private String context;
	
	/**创建时间 数据类型datetime*/
	private Date createDate;
	
	
	public Long getId(){
		return id;
	}
	public Long getTopicId(){
		return topicId;
	}
	public String getTopicName(){
		return topicName;
	}
	public Integer getErrorCount(){
		return errorCount;
	}
	public Integer getPushCount(){
		return pushCount;
	}
	public String getContext(){
		return context;
	}
	public Date getCreateDate(){
		return createDate;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setTopicId(Long topicId){
		this.topicId=topicId;
	}
	public void setTopicName(String topicName){
		this.topicName=topicName;
	}
	public void setErrorCount(Integer errorCount){
		this.errorCount=errorCount;
	}
	public void setPushCount(Integer pushCount){
		this.pushCount=pushCount;
	}
	public void setContext(String context){
		this.context=context;
	}
	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}
}
