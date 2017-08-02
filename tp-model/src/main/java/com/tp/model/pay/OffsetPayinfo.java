package com.tp.model.pay;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class OffsetPayinfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451024638869L;

	/**补偿单打款信息 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**打款金额 数据类型double(10,2)*/
	private Double amount;
	
	/**打款状态(0:待打款，1打款中，2打款成功，3打款失败) 数据类型tinyint(4)*/
	private Integer status;
	
	/**支付方式 数据类型tinyint(4)*/
	private Integer gatewayId;
	
	/**支付账号 数据类型varchar(30)*/
	private String payAccount;
	
	/**收款人账号 数据类型varchar(30)*/
	private String payeeAccount;
	
	/**收款人姓名 数据类型varchar(20)*/
	private String payeeName;
	
	/**流水号 数据类型varchar(64)*/
	private String serial;
	
	/**交易流水号 数据类型varchar(64)*/
	private String gatewayTradeNo;
	
	/**回调信息 数据类型varchar(500)*/
	private String callbackInfo;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date udpateTime;
	
	
	public Long getId(){
		return id;
	}
	public Double getAmount(){
		return amount;
	}
	public Integer getStatus(){
		return status;
	}
	public Integer getGatewayId(){
		return gatewayId;
	}
	public String getPayAccount(){
		return payAccount;
	}
	public String getPayeeAccount(){
		return payeeAccount;
	}
	public String getPayeeName(){
		return payeeName;
	}
	public String getSerial(){
		return serial;
	}
	public String getGatewayTradeNo(){
		return gatewayTradeNo;
	}
	public String getCallbackInfo(){
		return callbackInfo;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUdpateTime(){
		return udpateTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setAmount(Double amount){
		this.amount=amount;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setGatewayId(Integer gatewayId){
		this.gatewayId=gatewayId;
	}
	public void setPayAccount(String payAccount){
		this.payAccount=payAccount;
	}
	public void setPayeeAccount(String payeeAccount){
		this.payeeAccount=payeeAccount;
	}
	public void setPayeeName(String payeeName){
		this.payeeName=payeeName;
	}
	public void setSerial(String serial){
		this.serial=serial;
	}
	public void setGatewayTradeNo(String gatewayTradeNo){
		this.gatewayTradeNo=gatewayTradeNo;
	}
	public void setCallbackInfo(String callbackInfo){
		this.callbackInfo=callbackInfo;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUdpateTime(Date udpateTime){
		this.udpateTime=udpateTime;
	}
}
