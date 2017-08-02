package com.tp.service.pay.cbdata;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.tp.result.pay.RefundResult;


public class UnionRefundResult implements RefundResult, Serializable{
	private static final long serialVersionUID = 2465712809821002773L;

	private Map<String, String> parameterMap;
	
	private Date callBackTime = new Date(); // 回调时间
	
	public UnionRefundResult(Map<String, String> parameterMap) {
		this.parameterMap = parameterMap;
	}

	@Override
	public String getRefundNo() {
		return parameterMap.get("orderId");
	}

	@Override
	public String getGatewayTradeNo() {
		return parameterMap.get("queryId");
	}

	@Override
	public String getRefundSerial() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSuccess() {
		return "00".equals(parameterMap.get("respCode"));
	}

	@Override
	public String getMessage() {
		return parameterMap.get("respMsg");
	}

	@Override
	public String getCallbackInfo() {
		return parameterMap.get("respMsg");
	}

	@Override
	public long getPaymentAmount() {
		return Long.valueOf(parameterMap.get("txnAmt"));
	}

	@Override
	public String getPaymentGateway() {
		return "银联支付网关";
	}

	@Override
	public Date getCallBackTime() {
		return callBackTime;
	}

	@Override
	public String getCreateUserID() {
		return "unionpay";
	}

	@Override
	public boolean hasCallback() {
		return false;
	}
}
