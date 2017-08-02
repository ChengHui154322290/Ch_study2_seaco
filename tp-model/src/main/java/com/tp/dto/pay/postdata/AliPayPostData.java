package com.tp.dto.pay.postdata;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.tp.common.vo.OrderConstant;
import com.tp.dto.pay.AppPayData;
import com.tp.model.pay.PaymentInfo;
import com.tp.util.AlipayUtil;

public class AliPayPostData implements Serializable, PayPostData, AppPayData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 552078284128780870L;
	private String service = "create_direct_pay_by_user";
	private String partner;
	private String inputCharset = "utf-8";

	private String paymentType = "1";

	private String notifyUrl;

	private String returnUrl;

	private String sellerEmail;

	private String subject;

	private String totalFee;

	private String body;

	private String showUrl;

	private String outTradeNo;

	private String signType = "MD5";

	private String key;

	/** wap form 字段 **/
	private String sec_id;

	private String format;

	private String v;

	private String req_data;

	private String sign;
	
	private String wap_action_url;
	
	private String payid;
	

	public Map<String, String> getsParam() {
		return sParam;
	}

	public void setsParam(Map<String, String> sParam) {
		this.sParam = sParam;
	}

	private Map<String, String> sParam = new HashMap<String, String>();

	public AliPayPostData() {

	}

	public AliPayPostData(Properties paymentConfig, PaymentInfo dto) {
		this.partner = paymentConfig.getProperty("ALIPAY_PARTNER");
		this.key = paymentConfig.getProperty("ALIPAY_KEY");
		this.inputCharset = paymentConfig.getProperty("ALIPAY_INPUT_CHARSET");
		this.notifyUrl = paymentConfig.getProperty("ALIPAY_NOTIFY_URL");
		this.returnUrl = paymentConfig.getProperty("ALIPAY_RETURN_URL") + dto.getPaymentId();
		if(dto.getOrderType()!=null && OrderConstant.FAST_ORDER_TYPE.equals(dto.getOrderType().intValue())){
			this.notifyUrl = notifyUrl.replaceFirst("(.*://)?[^/]*/", paymentConfig.getProperty("fastPay.notify_url"));
			this.returnUrl = returnUrl.replaceFirst("(.*://)?[^/]*/", paymentConfig.getProperty("fastPay.notify_url"));
		}
		this.sellerEmail = paymentConfig.getProperty("ALIPAY_SELLER_EMAIL");
		this.subject = paymentConfig.getProperty("ALIPAY_SUBJECT");
		this.signType = paymentConfig.getProperty("ALIPAY_SIGN_TYPE");
		this.outTradeNo = dto.getPaymentTradeNo();
		this.totalFee = String.format("%.2f", dto.getAmount());
	}

	
	
	public String getPayid() {
		return payid;
	}

	public void setPayid(String payid) {
		this.payid = payid;
	}

	public String getWap_action_url() {
		return wap_action_url;
	}

	public void setWap_action_url(String wap_action_url) {
		this.wap_action_url = wap_action_url;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSec_id() {
		return sec_id;
	}

	public void setSec_id(String sec_id) {
		this.sec_id = sec_id;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

	public String getReq_data() {
		return req_data;
	}

	public void setReq_data(String req_data) {
		this.req_data = req_data;
	}

	public String getInputCharset() {
		return inputCharset;
	}

	public void setInputCharset(String inputCharset) {
		this.inputCharset = inputCharset;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getBody() {
		return body == null ? "" : body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getShowUrl() {
		return showUrl == null ? "" : showUrl;
	}

	public void setShowUrl(String showUrl) {
		this.showUrl = showUrl;
	}

	@Override
	public String getSignature() {
		sParam.put("service", "create_direct_pay_by_user");
		sParam.put("partner", getPartner());
		sParam.put("_input_charset", getInputCharset());
		sParam.put("payment_type", getPaymentType());
		sParam.put("notify_url", getNotifyUrl());
		sParam.put("return_url", getReturnUrl());
		sParam.put("seller_email", getSellerEmail());
		sParam.put("out_trade_no", getOutTradeNo());
		sParam.put("subject", getSubject());
		sParam.put("total_fee", String.valueOf(getTotalFee()));
		sParam.put("body", getBody());
		sParam.put("show_url", getShowUrl());
		sParam.put("sign_type", getSignType());
		return AlipayUtil.buildRequestMysign(sParam, key);
	}

	@Override
	public String getPaymentTradeNo() {
		return getOutTradeNo();
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
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

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getSellerEmail() {
		return sellerEmail;
	}

	public void setSellerEmail(String sellerEmail) {
		this.sellerEmail = sellerEmail;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}
}
