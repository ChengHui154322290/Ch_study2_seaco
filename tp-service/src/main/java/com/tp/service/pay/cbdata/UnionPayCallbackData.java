package com.tp.service.pay.cbdata;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.tp.dto.pay.cbdata.PayCallbackData;

public class UnionPayCallbackData implements PayCallbackData, Serializable{
	private static final long serialVersionUID = -2134612670263544038L;
	
	private Map<String, String> parameterMap;
	
	private Date callBackTime = new Date(); // 回调时间
	
	public UnionPayCallbackData(Map<String, String> parameterMap) {
		this.parameterMap = parameterMap;
	}


	@Override
	public String getPaymentTradeNo() {
		return parameterMap.get("orderId");
	}

	@Override
	public String getGatewayTradeNo() {
		return parameterMap.get("queryId");
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

}
