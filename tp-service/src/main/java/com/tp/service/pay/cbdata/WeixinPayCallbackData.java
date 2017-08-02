package com.tp.service.pay.cbdata;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.tp.dto.pay.cbdata.PayCallbackData;
import com.tp.util.DateUtil;


public class WeixinPayCallbackData implements PayCallbackData, Serializable{
	private static final long serialVersionUID = 2282781824480112694L;
	
	private Map<String, String> parameterMap;
	public WeixinPayCallbackData(Map<String, String> parameterMap) {
		this.parameterMap = parameterMap;
	}
	

	@Override
	public String getPaymentTradeNo() {
		return parameterMap.get("out_trade_no");
	}

	@Override
	public String getGatewayTradeNo() {
		return parameterMap.get("transaction_id");
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
		return Long.valueOf(parameterMap.get("total_fee"));
	}

	@Override
	public String getPaymentGateway() {
		return "微信支付网关";
	}

	@Override
	public Date getCallBackTime() {
		return DateUtil.parse(parameterMap.get("time_end"), "yyyyMMddHHmmss");
	}

	@Override
	public String getCreateUserID() {
		return "weixinpay";
	}

}
