package com.tp.model.mem;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 用户交易方式信息表
  */
public class MemberTradeInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451281300756L;

	/**用户ID 数据类型bigint(14)*/
	private Long userId;
	
	/**主键 数据类型bigint(14)*/
	@Id
	private Long id;
	
	/**支付网关 数据类型bigint(20)*/
	private Long paymentGatewayId;
	
	/**支付方式 数据类型int(11)*/
	private Integer paymentType;
	
	/**配送方式 数据类型int(11)*/
	private Integer deployType;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	
	public Long getUserId(){
		return userId;
	}
	public Long getId(){
		return id;
	}
	public Long getPaymentGatewayId(){
		return paymentGatewayId;
	}
	public Integer getPaymentType(){
		return paymentType;
	}
	public Integer getDeployType(){
		return deployType;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public void setUserId(Long userId){
		this.userId=userId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setPaymentGatewayId(Long paymentGatewayId){
		this.paymentGatewayId=paymentGatewayId;
	}
	public void setPaymentType(Integer paymentType){
		this.paymentType=paymentType;
	}
	public void setDeployType(Integer deployType){
		this.deployType=deployType;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
}
