package com.tp.dto.pay.tonglian;

import java.io.Serializable;

public class TxInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String merchantId;//商户号
	private String version;//报文版本
	private String payType;//支付方式
	private String signType;//签名方式
	private String charset;//字符集
	private String orderNo;//商户订单号
	private String orderDatetime;//商户订单时间
	private CustomsInfo customsInfo;
	
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderDatetime() {
		return orderDatetime;
	}
	public void setOrderDatetime(String orderDatetime) {
		this.orderDatetime = orderDatetime;
	}
	public CustomsInfo getCustomsInfo() {
		return customsInfo;
	}
	public void setCustomsInfo(CustomsInfo customsInfo) {
		this.customsInfo = customsInfo;
	}
	
	
}
