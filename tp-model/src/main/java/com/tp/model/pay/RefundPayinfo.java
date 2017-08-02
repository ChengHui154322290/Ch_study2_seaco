package com.tp.model.pay;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 退款支付明细表
  */
public class RefundPayinfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451024638871L;

	/**退款支付ID 数据类型bigint(20)*/
	@Id
	private Long payRefundId;
	
	/**支付编号 数据类型bigint(20)*/
	private Long paymentId;
	
	/**业务对象ID 数据类型varchar(32)*/
	private Long bizCode;
	
	/**业务对象类型 数据类型int(11)*/
	private Integer bizType;
	
	/**退款类型(1-订单，2-补偿，3-提现) 数据类型int(11)*/
	private Integer refundType;
	
	/**金额 数据类型double(10,2)*/
	private Double amount;
	
	/**退款网关 数据类型bigint(18)*/
	private Long gatewayId;
	
	/**状态( 1-正在退款，2-退款完成, 3-退款出错 ) 数据类型tinyint(4)*/
	private Integer status;
	
	/**业务流水号 数据类型varchar(32)*/
	private String serial;
	
	/**网关交易流水号 数据类型varchar(32)*/
	private String gatewayTradeNo;
	
	/**回调时间 数据类型datetime*/
	private Date callbackTime;
	
	/**回调信息 数据类型varchar(500)*/
	private String callbackInfo;
	
	/**是否已通知业务系统 数据类型tinyint(4)*/
	private Integer notified;
	
	/**通知时间 数据类型datetime*/
	private Date notifyTime;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	/**修改人 数据类型varchar(32)*/
	private String updateUser;
	
	@Virtual
	private PaymentInfo paymentInfo;
	
	public Long getPayRefundId(){
		return payRefundId;
	}
	public Long getPaymentId(){
		return paymentId;
	}
	public Long getBizCode(){
		return bizCode;
	}
	public Integer getBizType(){
		return bizType;
	}
	public Integer getRefundType(){
		return refundType;
	}
	public Double getAmount(){
		return amount;
	}
	public Long getGatewayId(){
		return gatewayId;
	}
	public Integer getStatus(){
		return status;
	}
	public String getSerial(){
		return serial;
	}
	public String getGatewayTradeNo(){
		return gatewayTradeNo;
	}
	public Date getCallbackTime(){
		return callbackTime;
	}
	public String getCallbackInfo(){
		return callbackInfo;
	}
	public Integer getNotified(){
		return notified;
	}
	public Date getNotifyTime(){
		return notifyTime;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public void setPayRefundId(Long payRefundId){
		this.payRefundId=payRefundId;
	}
	public void setPaymentId(Long paymentId){
		this.paymentId=paymentId;
	}
	public void setBizCode(Long bizCode){
		this.bizCode=bizCode;
	}
	public void setBizType(Integer bizType){
		this.bizType=bizType;
	}
	public void setRefundType(Integer refundType){
		this.refundType=refundType;
	}
	public void setAmount(Double amount){
		this.amount=amount;
	}
	public void setGatewayId(Long gatewayId){
		this.gatewayId=gatewayId;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setSerial(String serial){
		this.serial=serial;
	}
	public void setGatewayTradeNo(String gatewayTradeNo){
		this.gatewayTradeNo=gatewayTradeNo;
	}
	public void setCallbackTime(Date callbackTime){
		this.callbackTime=callbackTime;
	}
	public void setCallbackInfo(String callbackInfo){
		this.callbackInfo=callbackInfo;
	}
	public void setNotified(Integer notified){
		this.notified=notified;
	}
	public void setNotifyTime(Date notifyTime){
		this.notifyTime=notifyTime;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
	public PaymentInfo getPaymentInfo() {
		return paymentInfo;
	}
	public void setPaymentInfo(PaymentInfo paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	
}
