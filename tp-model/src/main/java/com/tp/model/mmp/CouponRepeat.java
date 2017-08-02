package com.tp.model.mmp;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 补券规则信息表
  */
public class CouponRepeat extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579104L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**规则描述 数据类型varchar(20)*/
	private String name;
	
	/**消耗送券批次列表 逗号分隔 数据类型varchar(200)*/
	private String batchIds;
	
	/**重发频率，0代表每次都重发 1,2,3等等代表重发的次数  数据类型tinyint(1)*/
	private Integer frequency;
	
	/**重发触发时间类型 0：订单支付 1：订单确认收货 2：订单下单 数据类型tinyint(1)*/
	private Integer triggerType;
	
	/**开始时间 数据类型datetime*/
	private Date startTime;
	
	/**结束时间 数据类型datetime*/
	private Date endTime;
	
	/**是否发送短信，0代表不发送，1代表发送 数据类型tinyint(1)*/
	private Integer smsState;
	
	/**短信内容 数据类型varchar(100)*/
	private String smsContent;
	
	/**创建人id 数据类型bigint(20)*/
	private Long createUserId;
	
	/**创建人姓名 数据类型varchar(50)*/
	private String createUserName;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改人id 数据类型bigint(20)*/
	private Long modifyUserId;
	
	/**修改人姓名 数据类型varchar(50)*/
	private String modifyUserName;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	/**状态,0：不可用 1：可用 数据类型tinyint(1)*/
	private Integer state;
	
	
	public Long getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public String getBatchIds(){
		return batchIds;
	}
	public Integer getFrequency(){
		return frequency;
	}
	public Integer getTriggerType(){
		return triggerType;
	}
	public Date getStartTime(){
		return startTime;
	}
	public Date getEndTime(){
		return endTime;
	}
	public Integer getSmsState(){
		return smsState;
	}
	public String getSmsContent(){
		return smsContent;
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
	public Long getModifyUserId(){
		return modifyUserId;
	}
	public String getModifyUserName(){
		return modifyUserName;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public Integer getState(){
		return state;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setBatchIds(String batchIds){
		this.batchIds=batchIds;
	}
	public void setFrequency(Integer frequency){
		this.frequency=frequency;
	}
	public void setTriggerType(Integer triggerType){
		this.triggerType=triggerType;
	}
	public void setStartTime(Date startTime){
		this.startTime=startTime;
	}
	public void setEndTime(Date endTime){
		this.endTime=endTime;
	}
	public void setSmsState(Integer smsState){
		this.smsState=smsState;
	}
	public void setSmsContent(String smsContent){
		this.smsContent=smsContent;
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
	public void setModifyUserId(Long modifyUserId){
		this.modifyUserId=modifyUserId;
	}
	public void setModifyUserName(String modifyUserName){
		this.modifyUserName=modifyUserName;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public void setState(Integer state){
		this.state=state;
	}
}
