package com.tp.model.ord;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 订单推广渠道信息跟踪表
  */
public class OrderChannelTrack extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1473392161073L;

	/**渠道跟踪ID 数据类型bigint(11)*/
	@Id
	private Long trackId;
	
	/**会员ID 数据类型bigint(14)*/
	private Long memberId;
	
	/**父订单ID 数据类型bigint(14)*/
	private Long parentOrderId;
	
	/**父订单编号 数据类型bigint(16)*/
	private Long parentOrderCode;
	
	/**分销渠道代码 数据类型varchar(32)*/
	private String channelCode;
	/**订单来源或渠道 数据类型varchar(16)*/
	private String source;
	
	/**渠道对应平台代码 数据类型varchar(24)*/
	private String clientCode;
	
	/**渠道分发信息 数据类型varchar(200)*/
	private String distributeCode;
	
	/**会话编码 数据类型varchar(64)*/
	private String sessionId;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	
	public Long getTrackId(){
		return trackId;
	}
	public Long getMemberId(){
		return memberId;
	}
	public Long getParentOrderId(){
		return parentOrderId;
	}
	public Long getParentOrderCode(){
		return parentOrderCode;
	}
	public String getSource(){
		return source;
	}
	public String getClientCode(){
		return clientCode;
	}
	public String getDistributeCode(){
		return distributeCode;
	}
	public String getSessionId(){
		return sessionId;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public void setTrackId(Long trackId){
		this.trackId=trackId;
	}
	public void setMemberId(Long memberId){
		this.memberId=memberId;
	}
	public void setParentOrderId(Long parentOrderId){
		this.parentOrderId=parentOrderId;
	}
	public void setParentOrderCode(Long parentOrderCode){
		this.parentOrderCode=parentOrderCode;
	}
	public void setSource(String source){
		this.source=source;
	}
	public void setClientCode(String clientCode){
		this.clientCode=clientCode;
	}
	public void setDistributeCode(String distributeCode){
		this.distributeCode=distributeCode;
	}
	public void setSessionId(String sessionId){
		this.sessionId=sessionId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
}
