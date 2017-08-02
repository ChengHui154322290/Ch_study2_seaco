package com.tp.service.pay.cbdata;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.tp.result.pay.RefundResult;
import com.tp.util.DateUtil;


public class WeixinRefundResult implements RefundResult, Serializable{
	private static final long serialVersionUID = -6169574829869650167L;
	
	private Map<String, String> parameterMap;
	public WeixinRefundResult(Map<String, String> parameterMap) {
		this.parameterMap = parameterMap;
	}
	
	
	@Override
	public String getRefundNo() {
		return parameterMap.get("out_refund_no");
	}

	@Override
	public String getGatewayTradeNo() {
		return parameterMap.get("refund_id");
	}

	@Override
	public String getRefundSerial() {
		return parameterMap.get("refund_id");
	}

	@Override
	public boolean isSuccess() {
		return "SUCCESS".equals(parameterMap.get("result_code"));
	}

	@Override
	public String getMessage() {
		return parameterMap.get("return_msg");
	}

	@Override
	public String getCallbackInfo() {
		return parameterMap.get("return_msg");
	}

	@Override
	public long getPaymentAmount() {
		return Long.valueOf(parameterMap.get("refund_fee"));
	}

	@Override
	public String getPaymentGateway() {
		return "微信支付网关";
	}

	@Override
	public Date getCallBackTime() {
		return new Date();
	}

	@Override
	public String getCreateUserID() {
		return "weixinpay";
	}


	@Override
	public boolean hasCallback() {
		return false;
	}
}
