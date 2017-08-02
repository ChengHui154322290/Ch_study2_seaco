package com.tp.service.pay.cbdata;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.tp.result.pay.RefundResult;


public class AlipayRefundResult implements RefundResult, Serializable{
	private static final long serialVersionUID = 2233047040099476303L;
	//人民币网关账号，该账号为11位人民币网关商户编号+01,该值与提交时相同。

	private Map<String, String> refundResult;
	
	public AlipayRefundResult(Map<String, String> refundResult){
		this.refundResult = refundResult;
	}
	public AlipayRefundResult(){}
	
	@Override
	public String getRefundNo() {
		return refundResult.get("out_trade_no");
	}
	@Override
	public String getGatewayTradeNo() {
		return refundResult.get("trade_no");
	}
	@Override
	public String getRefundSerial() {
		return refundResult.get("out_trade_no");
	}
	@Override
	public boolean isSuccess() {
		return "T".equals(refundResult.get("is_success"));
	}
	@Override
	public String getMessage() {
		return refundResult.get("error");
	}
	@Override
	public String getCallbackInfo() {
		return refundResult.get("error");
	}
	@Override
	public long getPaymentAmount() {
		return 0;
	}
	@Override
	public String getPaymentGateway() {
		return refundResult.get("payType") == null ? "支付宝国际支付网关" : refundResult.get("payType");
	}
	@Override
	public Date getCallBackTime() {
		return new Date();
	}
	@Override
	public String getCreateUserID() {
		return refundResult.get("alipay") == null ? "alipay international" : refundResult.get("alipay");
	}
	@Override
	public boolean hasCallback() {
		return true;
	}
}
