package com.tp.dto.pay.postdata;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.tp.util.AlipayUtil;
import com.tp.util.DateUtil;

public class AliPayRefundPostData implements RefundPostData, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 377067666639429480L;
	private String service = "refund_fastpay_by_platform_pwd";
	private String partner;
	private String notifyUrl;
	private String detailData;
	private String inputCharset;
	private String refundDate;
	private String batchNum;
	private String batchNo;
	private String sellerEmail;
	private String key;

	private Map<String, String> sParam = new HashMap<String, String>();

	public AliPayRefundPostData() {

	}

	public AliPayRefundPostData(Properties paymentConfig) {
		this.partner = paymentConfig.getProperty("ALIPAY_PARTNER");
		this.inputCharset = paymentConfig.getProperty("ALIPAY_INPUT_CHARSET");
		this.notifyUrl = paymentConfig.getProperty("ALIPAY_REFUND_RETURN_URL");
		this.sellerEmail = paymentConfig.getProperty("ALIPAY_SELLER_EMAIL");
		this.key = paymentConfig.getProperty("ALIPAY_KEY");
		this.refundDate = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String getSignature() {
		sParam.put("service", "refund_fastpay_by_platform_pwd");
		sParam.put("partner", getPartner());
		sParam.put("_input_charset", getInputCharset());
		sParam.put("notify_url", getNotifyUrl());
		sParam.put("seller_email", getSellerEmail());
		sParam.put("sign_type", "MD5");
		sParam.put("detail_data", getDetailData());
		sParam.put("batch_no", getBatchNo());
		sParam.put("batch_num", getBatchNum());
		sParam.put("refund_date", getRefundDate());

		return AlipayUtil.buildRequestMysign(sParam, key);
	}

	@Override
	public String getPaymentTradeNo() {
		return getPaymentTradeNo();
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

	public String getDetailData() {
		return detailData;
	}

	public void setDetailData(String detailData) {
		this.detailData = detailData;
	}

	public String getInputCharset() {
		return inputCharset;
	}

	public void setInputCharset(String inputCharset) {
		this.inputCharset = inputCharset;
	}

	public String getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(String refundDate) {
		this.refundDate = refundDate;
	}

	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Map<String, String> getsParam() {
		return sParam;
	}

	public void setsParam(Map<String, String> sParam) {
		this.sParam = sParam;
	}

	public String getSellerEmail() {
		return sellerEmail;
	}

	public void setSellerEmail(String sellerEmail) {
		this.sellerEmail = sellerEmail;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "AliPayRefundPostData [service=" + service + ", partner="
				+ partner + ", notifyUrl=" + notifyUrl + ", detailData="
				+ detailData + ", inputCharset=" + inputCharset
				+ ", refundDate=" + refundDate + ", batchNum=" + batchNum
				+ ", batchNo=" + batchNo + ", sellerEmail=" + sellerEmail
				+ ", key=" + key + ", sParam=" + sParam + "]";
	}
}
