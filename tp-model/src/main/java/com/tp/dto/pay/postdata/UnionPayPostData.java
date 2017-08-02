package com.tp.dto.pay.postdata;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.tp.dto.pay.AppPayData;
import com.tp.model.pay.PaymentInfo;
import com.tp.util.DateUtil;

public class UnionPayPostData implements PayPostData, Serializable, AppPayData {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -3917341518942476480L;
	private String actionUrl;
	private String version; // 消息版本号 M
	private String encoding; // 字符编码M 全大写
	private String certId;//证书ID
	public String getCertId() {
		return certId;
	}
	public void setCertId(String certId) {
		this.certId = certId;
	}

	// 签名
	private String signature;
	private String signMethod;//加密方式
	
	private String txnType; // 交易类型M
	private String txnSubType;//交易子类M
	private String bizType;//产品类型 M
	private String channelType;//渠道类型 M
	private String frontUrl; // 前台通知地址 C
	private String backUrl; // 后台通知地址 M
	
	private String accessType; // TODO 接入类型:商户接入填0 0- 商户 ， 1： 收单， 2：平台商户 M
	private String merId; // 商户代码M
	private String orderId; // 商户订单号 M 最大为32个
	private String txnTime; // 交易开始日期时间M
	private String txnAmt; // 交易金额 M
	private String currencyCode; // 交易币种 M 156代表人民币
	private String customerIp; // 持卡人IP C
	
	private String payTimeout; // TODO 交易超时时间 O // 建议商户提交，以防止钓鱼网站的问题
	private String merReserved = ""; // 商户保留域
	private String key;// 商城密匙，需要和银联商户网站上配置的一样
	
	/**
	 * 
	 */
	private UnionPayPostData(Properties paymentConfig) {
//		actionUrl = paymentConfig.getProperty("unionPay.actionUrl");
//		key = paymentConfig.getProperty("unionPay.key");
		txnType = paymentConfig.getProperty("unionPay.txnType");
		currencyCode = paymentConfig.getProperty("unionPay.currencyCode");
		
//		customerIp = paymentConfig.getProperty("unionPay.customerIp");
		txnSubType = paymentConfig.getProperty("unionPay.txnSubType");
		version = paymentConfig.getProperty("unionPay.version");
		signMethod = paymentConfig.getProperty("unionPay.signMethod");
		backUrl = paymentConfig.getProperty("unionPay.backUrl");
		encoding = paymentConfig.getProperty("unionPay.encoding");
		bizType = paymentConfig.getProperty("unionPay.bizType");
//		signature = paymentConfig.getProperty("unionPay.signature");
		
//		orderId = paymentConfig.getProperty("unionPay.orderId");
		accessType = paymentConfig.getProperty("unionPay.accessType");
//		txnTime = DateUtil.format(new Date(), "yyyyMMddHHmmss"); //paymentConfig.getProperty("unionPay.txnTime");
//		txnAmt = paymentConfig.getProperty("unionPay.txnAmt");
//		merReserved = paymentConfig.getProperty("unionPay.merReserved");
	}
	/**
	 * 包装支付的数据.
	 * 
	 * @param ordOrder
	 * @param ordPayment
	 */
	public UnionPayPostData(Properties paymentConfig, PaymentInfo paymentInfo) {
		this(paymentConfig);
		this.orderId=""+paymentInfo.getBizCode();
		this.txnTime= DateUtil.format(paymentInfo.getCreateTime(), "yyyyMMddHHmmss");
		this.txnAmt = String.valueOf(Math.round(paymentInfo.getAmount()*100));

	}
	
	//需要加密ＳＤＫ要求用Map数据
	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
//		if (!StringUtils.isBlank(actionUrl)) {
//			map.put("actionUrl", actionUrl);
//		}
		if (!StringUtils.isBlank(txnType)) {
			map.put("txnType", txnType);
		}
		if (!StringUtils.isBlank(frontUrl)) {
			map.put("frontUrl", frontUrl);
		}
		if (!StringUtils.isBlank(currencyCode)) {
			map.put("currencyCode", currencyCode);
		}
		if (!StringUtils.isBlank(channelType)) {
			map.put("channelType", channelType);
		}
		if (!StringUtils.isBlank(merId)) {
			map.put("merId", merId);
		}
		if (!StringUtils.isBlank(customerIp)) {
			map.put("customerIp", customerIp);
		}
		if (!StringUtils.isBlank(txnSubType)) {
			map.put("txnSubType", txnSubType);
		}
		if (!StringUtils.isBlank(txnAmt)) {
			map.put("txnAmt", txnAmt);
		}
		if (!StringUtils.isBlank(version)) {
			map.put("version", version);
		}
		if (!StringUtils.isBlank(signMethod)) {
			map.put("signMethod", signMethod);
		}
		if (!StringUtils.isBlank(backUrl)) {
			map.put("backUrl", backUrl);
		}
		if (!StringUtils.isBlank(encoding)) {
			map.put("encoding", encoding);
		}
		if (!StringUtils.isBlank(bizType)) {
			map.put("bizType", bizType);
		}
//		if (!StringUtils.isBlank(signature)) {
//			map.put("signature", signature);
//		}
		if (!StringUtils.isBlank(orderId)) {
			map.put("orderId", orderId);
		}
		if (!StringUtils.isBlank(accessType)) {
			map.put("accessType", accessType);
		}
		if (!StringUtils.isBlank(txnTime)) {
			map.put("txnTime", txnTime);
		}
		if (!StringUtils.isBlank(merReserved)) {
			map.put("merReserved", merReserved);
		}
		return map;
	}
	
	public void updateData(Map<String, String> map) {
//		actionUrl=map.get("actionUrl");
		txnType=map.get("txnType");
		frontUrl=map.get("frontUrl");
		currencyCode=map.get("currencyCode");
		channelType=map.get("channelType");
		merId=map.get("merId");
		customerIp=map.get("customerIp");
		txnSubType=map.get("txnSubType");
		txnAmt=map.get("txnAmt");
		version=map.get("version");
		signMethod=map.get("signMethod");
		backUrl=map.get("backUrl");
		encoding=map.get("encoding");
		bizType=map.get("bizType");
		certId=map.get("certId");
		signature=map.get("signature");
		orderId=map.get("orderId");
		accessType=map.get("accessType");
		txnTime=map.get("txnTime");
		merReserved=map.get("merReserved");
	}
	
	public String getActionUrl() {
		return actionUrl;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getSignMethod() {
		return signMethod;
	}
	public void setSignMethod(String signMethod) {
		this.signMethod = signMethod;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getTxnSubType() {
		return txnSubType;
	}
	public void setTxnSubType(String txnSubType) {
		this.txnSubType = txnSubType;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	public String getFrontUrl() {
		return frontUrl;
	}
	public void setFrontUrl(String frontUrl) {
		this.frontUrl = frontUrl;
	}
	public String getBackUrl() {
		return backUrl;
	}
	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}
	public String getAccessType() {
		return accessType;
	}
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getTxnTime() {
		return txnTime;
	}
	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}
	public String getTxnAmt() {
		return txnAmt;
	}
	public void setTxnAmt(String txnAmt) {
		this.txnAmt = txnAmt;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getCustomerIp() {
		return customerIp;
	}
	public void setCustomerIp(String customerIp) {
		this.customerIp = customerIp;
	}
	public String getPayTimeout() {
		return payTimeout;
	}
	public void setPayTimeout(String payTimeout) {
		this.payTimeout = payTimeout;
	}
	public String getMerReserved() {
		return merReserved;
	}
	public void setMerReserved(String merReserved) {
		this.merReserved = merReserved;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	@Override
	public String getPaymentTradeNo() {
		return getOrderId();
	}
	
}
