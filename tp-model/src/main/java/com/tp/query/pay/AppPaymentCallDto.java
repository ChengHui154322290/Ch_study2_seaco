package com.tp.query.pay;

import java.io.Serializable;
import java.util.Map;

public class AppPaymentCallDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2918942665197739570L;
	// 支付id
	private Long paymentId;
	// 支付方式编码
	private String gateway;
	// 是否SDK支付（true：sdk，false：WAP）
	private boolean isSdk;
	// 支付用户id
	private String userId;
	// 自定义参数
	private Map<String, Object> params;
	
	public Long getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	public boolean isSdk() {
		return isSdk;
	}
	public void setSdk(boolean isSdk) {
		this.isSdk = isSdk;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	@Override
	public String toString() {
		return "AppPaymentCallDto [paymentId=" + paymentId + ", gateway=" + gateway + ", isSdk=" + isSdk + ", userId="
				+ userId + ", params=" + params + "]";
	}
}

