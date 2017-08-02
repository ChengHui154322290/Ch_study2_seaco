package com.tp.model.mmp;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 优惠券发放信息表
  */
public class CouponSend extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579104L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**发送的优惠券批次，逗号隔开 数据类型varchar(255)*/
	private String couponIds;
	
	/**创建者id 数据类型bigint(20)*/
	private Long createUser;
	
	/**本次发放的名称 数据类型varchar(255)*/
	private String name;
	
	/**发放的类型 0普通发刚 1 自动发放 数据类型int(1)*/
	private Integer type;
	
	/**发放的状态 编辑、审核、等 数据类型int(1)*/
	private Integer status;
	
	/**发放状态 0 未发放 1 已发放 数据类型int(1)*/
	private Integer sendStatus;
	
	/**有效期 开始时间 数据类型datetime*/
	private Date startTime;
	
	/**有效期 结束时间 数据类型datetime*/
	private Date endTime;
	
	/**是否全部发放 数据类型int(1)*/
	private Integer toAll;
	
	/**接受者id 数据类型mediumtext*/
	private String toUserIds;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	/**0 不放 1 发信息 数据类型int(1)*/
	private Integer sendMsg;
	
	/**短信内同 数据类型text*/
	private String msgContent;
	
	/**张数 数据类型bigint(11)*/
	private Long sendSize;
	
	/**发送结果 数据类型mediumtext*/
	private String sendResult;
	
	
	public Long getId(){
		return id;
	}
	public String getCouponIds(){
		return couponIds;
	}
	public Long getCreateUser(){
		return createUser;
	}
	public String getName(){
		return name;
	}
	public Integer getType(){
		return type;
	}
	public Integer getStatus(){
		return status;
	}
	public Integer getSendStatus(){
		return sendStatus;
	}
	public Date getStartTime(){
		return startTime;
	}
	public Date getEndTime(){
		return endTime;
	}
	public Integer getToAll(){
		return toAll;
	}
	public String getToUserIds(){
		return toUserIds;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public Integer getSendMsg(){
		return sendMsg;
	}
	public String getMsgContent(){
		return msgContent;
	}
	public Long getSendSize(){
		return sendSize;
	}
	public String getSendResult(){
		return sendResult;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setCouponIds(String couponIds){
		this.couponIds=couponIds;
	}
	public void setCreateUser(Long createUser){
		this.createUser=createUser;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setSendStatus(Integer sendStatus){
		this.sendStatus=sendStatus;
	}
	public void setStartTime(Date startTime){
		this.startTime=startTime;
	}
	public void setEndTime(Date endTime){
		this.endTime=endTime;
	}
	public void setToAll(Integer toAll){
		this.toAll=toAll;
	}
	public void setToUserIds(String toUserIds){
		this.toUserIds=toUserIds;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public void setSendMsg(Integer sendMsg){
		this.sendMsg=sendMsg;
	}
	public void setMsgContent(String msgContent){
		this.msgContent=msgContent;
	}
	public void setSendSize(Long sendSize){
		this.sendSize=sendSize;
	}
	public void setSendResult(String sendResult){
		this.sendResult=sendResult;
	}
}
