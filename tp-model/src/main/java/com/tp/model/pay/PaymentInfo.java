package com.tp.model.pay;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 支付信息表
  */
public class PaymentInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451024638870L;

	/**支付编号 数据类型bigint(20)*/
	@Id
	private Long paymentId;
	
	/**业务对象编号 数据类型varchar(32)*/
	private Long bizCode;
	
	/**业务类型 数据类型tinyint(4)*/
	private Integer bizType;
	
	/**流水号 数据类型varchar(64)*/
	private String serial;
	
	/**支付类型(在线支付-1，货到付款-2，POS机-3) 数据类型int(11)*/
	private Integer paymentType;
	
	/**订单类型 数据类型bigint(20)*/
	private Long orderType;
	
	/**支付渠道代码 数据类型bigint(20)*/
	private Long channelId;
	
	/**支付网关编号 数据类型int(11)*/
	private Long gatewayId;
	
	/**支付金额 数据类型double(12,2)*/
	private Double amount;
	
	/**支付状态 数据类型tinyint(11)*/
	private Integer status;
	
	/**支付流水号 数据类型varchar(32)*/
	private String paymentTradeNo;
	
	/**支付信息，如微信的code_url 数据类型varchar(100)*/
	private String payInfo;
	
	/**支付网关流水号 数据类型varchar(32)*/
	private String gatewayTradeNo;
	
	/**支付操作IP 数据类型varchar(32)*/
	private String actionIp;
	
	/**回调信息 数据类型varchar(500)*/
	private String callbackInfo;
	
	/**回调时间 数据类型datetime*/
	private Date callbackTime;
	
	/**是否已回调业务系统 数据类型int(11)*/
	private Integer notified;
	
	/**通知时间 数据类型datetime*/
	private Date notifyTime;
	
	/**是否取消 0-否，1-是，默认为0 数据类型tinyint(1)*/
	private Integer canceled;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**创建人(支付人） 数据类型varchar(32)*/
	private String createUser;
	
	/**更新人(银行回调IP地址） 数据类型varchar(32)*/
	private String updateUser;
	
	/**税款 数据类型double(12,2)*/
	private Double taxFee;
	
	/**运费 数据类型double(10,2)*/
	private Double freight;
	
	/**姓名 数据类型varchar(20)*/
	private String realName;
	
	/**证件类型 数据类型varchar(10)*/
	private String identityType;
	
	/**证件号码 数据类型varchar(30)*/
	private String identityCode;
	
	/**交易方式（1：PC，2：WAP_mobile：3：WAP_btmobile 4:APP_mobile 5:APP_btmobile) 数据类型tinyint(4)*/
	private Integer tradeType;
	
	/**报关状态（0：未报关，1：报关成功：2：报关失败) 数据类型tinyint(1)*/
	private Integer paymentCustomsType;

	/**支付报关流水号*/
	private String paymentCustomsNo;
	
	/**合并单父ID 数据类型bigint(20)*/
	private Long prtPaymentId;
	
	@Virtual
    /** 单据创建时间*/
    private Date bizCreateTime;
	
	@Virtual
    private String gatewayCode;

	@Virtual
    private String gatewayName;
	
    /**
     * 此支付支持的支付网关，由<code>channelId</code>决定，不作持久化
     */
    private List<PaymentGateway> availableGateways;
    
	public List<PaymentGateway> getAvailableGateways() {
		return availableGateways;
	}
	public void setAvailableGateways(List<PaymentGateway> availableGateways) {
		this.availableGateways = availableGateways;
	}
	public String getGatewayCode() {
		return gatewayCode;
	}
	public void setGatewayCode(String gatewayCode) {
		this.gatewayCode = gatewayCode;
	}
	public String getGatewayName() {
		return gatewayName;
	}
	public void setGatewayName(String gatewayName) {
		this.gatewayName = gatewayName;
	}
	public Date getBizCreateTime() {
		return bizCreateTime;
	}
	public void setBizCreateTime(Date bizCreateTime) {
		this.bizCreateTime = bizCreateTime;
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
	public String getSerial(){
		return serial;
	}
	public Integer getPaymentType(){
		return paymentType;
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
	public Double getAmount(){
		return amount;
	}
	public Integer getStatus(){
		return status;
	}
	public String getPaymentTradeNo(){
		return paymentTradeNo;
	}
	public String getPayInfo(){
		return payInfo;
	}
	public String getGatewayTradeNo(){
		return gatewayTradeNo;
	}
	public String getActionIp(){
		return actionIp;
	}
	public String getCallbackInfo(){
		return callbackInfo;
	}
	public Date getCallbackTime(){
		return callbackTime;
	}
	public Integer getNotified(){
		return notified;
	}
	public Date getNotifyTime(){
		return notifyTime;
	}
	public Integer getCanceled(){
		return canceled;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public Double getTaxFee(){
		return taxFee;
	}
	public Double getFreight(){
		return freight;
	}
	public String getRealName(){
		return realName;
	}
	public String getIdentityType(){
		return identityType;
	}
	public String getIdentityCode(){
		return identityCode;
	}
	public Integer getTradeType(){
		return tradeType;
	}
	public Integer getPaymentCustomsType(){
		return paymentCustomsType;
	}
	public Long getPrtPaymentId(){
		return prtPaymentId;
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
	public void setSerial(String serial){
		this.serial=serial;
	}
	public void setPaymentType(Integer paymentType){
		this.paymentType=paymentType;
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
	public void setAmount(Double amount){
		this.amount=amount;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setPaymentTradeNo(String paymentTradeNo){
		this.paymentTradeNo=paymentTradeNo;
	}
	public void setPayInfo(String payInfo){
		this.payInfo=payInfo;
	}
	public void setGatewayTradeNo(String gatewayTradeNo){
		this.gatewayTradeNo=gatewayTradeNo;
	}
	public void setActionIp(String actionIp){
		this.actionIp=actionIp;
	}
	public void setCallbackInfo(String callbackInfo){
		this.callbackInfo=callbackInfo;
	}
	public void setCallbackTime(Date callbackTime){
		this.callbackTime=callbackTime;
	}
	public void setNotified(Integer notified){
		this.notified=notified;
	}
	public void setNotifyTime(Date notifyTime){
		this.notifyTime=notifyTime;
	}
	public void setCanceled(Integer canceled){
		this.canceled=canceled;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
	public void setTaxFee(Double taxFee){
		this.taxFee=taxFee;
	}
	public void setFreight(Double freight){
		this.freight=freight;
	}
	public void setRealName(String realName){
		this.realName=realName;
	}
	public void setIdentityType(String identityType){
		this.identityType=identityType;
	}
	public void setIdentityCode(String identityCode){
		this.identityCode=identityCode;
	}
	public void setTradeType(Integer tradeType){
		this.tradeType=tradeType;
	}
	public void setPaymentCustomsType(Integer paymentCustomsType){
		this.paymentCustomsType=paymentCustomsType;
	}
	public void setPrtPaymentId(Long prtPaymentId){
		this.prtPaymentId=prtPaymentId;
	}

	public String getPaymentCustomsNo() {
		return paymentCustomsNo;
	}

	public void setPaymentCustomsNo(String paymentCustomsNo) {
		this.paymentCustomsNo = paymentCustomsNo;
	}
}
