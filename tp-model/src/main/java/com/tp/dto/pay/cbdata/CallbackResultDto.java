package com.tp.dto.pay.cbdata;

import java.io.Serializable;
import java.util.Map;

import com.tp.model.pay.PaymentInfo;

public class CallbackResultDto implements Serializable{
	private static final long serialVersionUID = 1147373570471422996L;
	
	private boolean success = false;
	private String messge;
	private boolean needSendMQ = false;
	public String getMessge() {
		return messge;
	}

	public void setMessge(String messge) {
		this.messge = messge;
	}
	private PaymentInfo paymentInfoDO;
	private Map<String, String> returnParams;
	
	public CallbackResultDto() {
	}
	
	public CallbackResultDto(boolean success, PaymentInfo paymentInfoDO) {
		this.success = success;
		this.paymentInfoDO = paymentInfoDO;
	}

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public PaymentInfo getPaymentInfoDO() {
		return paymentInfoDO;
	}
	public void setPaymentInfoDO(PaymentInfo paymentInfoDO) {
		this.paymentInfoDO = paymentInfoDO;
	}
	public Map<String, String> getReturnParams() {
		return returnParams;
	}
	public void setReturnParams(Map<String, String> returnParams) {
		this.returnParams = returnParams;
	}
	public boolean isNeedSendMQ() {
		return needSendMQ;
	}

	public void setNeedSendMQ(boolean needSendMQ) {
		this.needSendMQ = needSendMQ;
	}
}
