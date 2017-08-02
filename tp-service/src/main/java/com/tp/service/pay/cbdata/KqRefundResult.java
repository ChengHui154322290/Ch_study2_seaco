package com.tp.service.pay.cbdata;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.tp.result.pay.RefundResult;

public class KqRefundResult implements RefundResult, Serializable{
	private static final long serialVersionUID = 2233047040099476303L;
	//人民币网关账号，该账号为11位人民币网关商户编号+01,该值与提交时相同。

	private Map<String, String> refundResult;
	
	public KqRefundResult(Map<String, String> refundResult){
		this.refundResult = refundResult;
	}
	public KqRefundResult(){}
	
	@Override
	public String getRefundNo() {
		return refundResult.get("orderid");
	}
	@Override
	public String getGatewayTradeNo() {
		return refundResult.get("txOrder");
	}
	@Override
	public String getRefundSerial() {
		return refundResult.get("txOrder");
	}
	@Override
	public boolean isSuccess() {
		return "Y".equals(refundResult.get("RESULT"));
	}
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getCallbackInfo() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public long getPaymentAmount() {
		if (StringUtils.isBlank(refundResult.get("RESULT"))) {
			return 0;
		} else {
			return Long.valueOf(Math.round(100*Double.valueOf(refundResult.get("AMOUNT"))));
		}
	}
	@Override
	public String getPaymentGateway() {
		return "快钱支付网关";
	}
	@Override
	public Date getCallBackTime() {
		return new Date();
	}
	@Override
	public String getCreateUserID() {
		return "kqpay";
	}
	@Override
	public boolean hasCallback() {
		return false;
	}
}
