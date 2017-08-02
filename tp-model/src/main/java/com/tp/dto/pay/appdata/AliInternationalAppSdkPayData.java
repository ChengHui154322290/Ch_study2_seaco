package com.tp.dto.pay.appdata;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.PaymentConstant.CurrencyEnum;
import com.tp.dto.pay.AppPayData;
import com.tp.dto.pay.postdata.PayPostData;
import com.tp.model.pay.PaymentInfo;
import com.tp.util.AlipayUtil;
import com.tp.util.RSA;


public class AliInternationalAppSdkPayData implements PayPostData, AppPayData, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2381785453129400076L;
	private String actionUrl;
	private String key;
	
	private String service;
	private String partner;
	private String inputCharset;
	private String signType;
	private String notifyUrl;
	
	private String outTradeNo;
	private String subject;
	private String paymentType;
	private String sellerId;
	private String rmbFee;
	private String forexBiz;
	private String currency;
	private String body;
	
	private String extParams;
	
	private String privateKey;
	
	
	
	public AliInternationalAppSdkPayData(Properties paymentConfig, PaymentInfo dto) {
		this.key = paymentConfig.getProperty("ALIPAY_INTERNATIONAL_KEY");
		
		this.service = "mobile.securitypay.pay";
		this.partner = paymentConfig.getProperty("ALIPAY_INTERNATIONAL_PARTNER");
		this.inputCharset = paymentConfig.getProperty("ALIPAY_INPUT_CHARSET");
		this.signType = "RSA";
		this.notifyUrl = paymentConfig.getProperty("ALIPAY_NOTIFY_URL");
		
		this.outTradeNo = dto.getPaymentTradeNo();
		this.subject = paymentConfig.getProperty("ALIPAY_SUBJECT");
		this.paymentType = "1";
		this.sellerId = paymentConfig.getProperty("ALIPAY_SELLER_EMAIL");
		this.rmbFee = String.format("%.2f", dto.getAmount());
		this.forexBiz = "FP";
		this.currency = CurrencyEnum.USD.toString();
		this.body = "西客商城订单";
		this.privateKey = paymentConfig.getProperty("ALIPAY_INTERNATIONAL_RSA_PRIVATE_KEY");
		this.extParams = "customs_place|ningbo#merchant_customs_code|1015";
		if(dto.getOrderType()!=null && OrderConstant.FAST_ORDER_TYPE.equals(dto.getOrderType().intValue())){
			this.notifyUrl = notifyUrl.replaceFirst("(.*://)?[^/]*/", paymentConfig.getProperty("fastPay.notify_url"));
		}
	}
	
	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getForexBiz() {
		return forexBiz;
	}

	public void setForexBiz(String forexBiz) {
		this.forexBiz = forexBiz;
	}

	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}


	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}


	public String getInputCharset() {
		return inputCharset;
	}

	public void setInputCharset(String inputCharset) {
		this.inputCharset = inputCharset;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getRmbFee() {
		return rmbFee;
	}

	public void setRmbFee(String rmbFee) {
		this.rmbFee = rmbFee;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getExtParams() {
		return extParams;
	}

	public void setExtParams(String extParams) {
		this.extParams = extParams;
	}

	@Override
	public String getPaymentTradeNo() {
		return outTradeNo;
	}

	@Override
	public String getSignature() {
		Map<String, String> sParam = new HashMap<String, String>();
		
		sParam.put("service", getService());
		sParam.put("partner", getPartner());
		sParam.put("input_charset", getInputCharset());
		sParam.put("notify_url", getNotifyUrl());
		
		sParam.put("out_trade_no", getOutTradeNo());
		sParam.put("subject", getSubject());
		sParam.put("payment_type", this.getPaymentType());
		sParam.put("seller_id", this.getSellerId());
		sParam.put("rmb_fee", String.valueOf(getRmbFee()));
		sParam.put("forex_biz", this.getForexBiz());
		sParam.put("currency", CurrencyEnum.USD.toString());
		sParam.put("body", getBody());
		
		sParam = AlipayUtil.paraFilter(sParam);
		String prestr = AlipayUtil.createLinkString(sParam);
		
		return RSA.sign(prestr, privateKey, this.getInputCharset());
	}
}
