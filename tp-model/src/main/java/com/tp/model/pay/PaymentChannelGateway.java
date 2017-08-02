package com.tp.model.pay;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 渠道对应网关关系表
  */
public class PaymentChannelGateway extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451024638869L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**订单类型 数据类型bigint(20)*/
	private Long orderType;
	
	/**发货渠道 数据类型bigint(20)*/
	private Long channelId;
	
	/**支付网关ID 数据类型bigint(20)*/
	private Long gatewayId;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	
	public Long getId(){
		return id;
	}
	public Long getOrderType(){
		return orderType;
	}
	public Long getChannelId(){
		return channelId;
	}
	public Long getGatewayId(){
		return gatewayId;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setOrderType(Long orderType){
		this.orderType=orderType;
	}
	public void setChannelId(Long channelId){
		this.channelId=channelId;
	}
	public void setGatewayId(Long gatewayId){
		this.gatewayId=gatewayId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
}
