package com.tp.dto.pay;

import java.io.Serializable;
import java.util.Date;

public class PayPaymentSimpleDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4474420610498367965L;

	/**支付对象编号*/
	private Long bizCode;
	
	/**业务对象业务*/
	private Integer bizType;
	
	/**支付流水号*/
	private String serial;
	
	
	/**支付金额*/
	private Double amount;
	
	/** 订单类型　*/
	private Long orderType;
	/** 支付渠道　*/
	private Long channelId;
	/**支付网关*/
	private Long gatewayId;
	
	/** 支付ip*/
	private String actionIP;
	
	/** 描述 */
	private String description;
	
	/** 商品展示url */
	private String url;
	
	/** 支付人*/
	private Long userId;

	/** 单据创建时间*/
	private Date bizCreateTime;
	
	 /**税款*/
    private Double                 taxFee;

    /**运费*/
    private Double                 freight;

    /**姓名*/
    private String                 realName;

    /**证件类型*/
    private String                 identityType;

    /**证件号码*/
    private String                 identityCode;
	

	public Date getBizCreateTime() {
		return bizCreateTime;
	}

	public void setBizCreateTime(Date bizCreateTime) {
		this.bizCreateTime = bizCreateTime;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(Long gatewayId) {
		this.gatewayId = gatewayId;
	}

	public Long getOrderType() {
		return orderType;
	}

	public void setOrderType(Long orderType) {
		this.orderType = orderType;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getActionIP() {
		return actionIP;
	}

	public void setActionIP(String actionIP) {
		this.actionIP = actionIP;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getBizCode() {
		return bizCode;
	}

	public void setBizCode(Long bizCode) {
		this.bizCode = bizCode;
	}

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	public Double getTaxFee() {
		return taxFee;
	}

	public void setTaxFee(Double taxFee) {
		this.taxFee = taxFee;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}

	public String getIdentityCode() {
		return identityCode;
	}

	public void setIdentityCode(String identityCode) {
		this.identityCode = identityCode;
	}
}
