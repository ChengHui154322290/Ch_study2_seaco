package com.tp.m.vo.pay;


/**
 * APP的支付宝支付信息
 * @author zhuss
 * @2016年1月8日 上午11:13:37
 */
public class APPGJZFBPayVO extends BasePayVO{
	
	private static final long serialVersionUID = 6660275026063940137L;
	private String orderinfo; //订单信息
	private String signedinfo; //签名后的信息

	public String getOrderinfo() {
		return orderinfo;
	}

	public void setOrderinfo(String orderinfo) {
		this.orderinfo = orderinfo;
	}

	public String getSignedinfo() {
		return signedinfo;
	}

	public void setSignedinfo(String signedinfo) {
		this.signedinfo = signedinfo;
	}
}
