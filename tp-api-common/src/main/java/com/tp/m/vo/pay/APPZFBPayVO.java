package com.tp.m.vo.pay;


/**
 * APP的支付宝支付信息
 * @author zhuss
 * @2016年1月8日 上午11:13:37
 */
public class APPZFBPayVO extends BasePayVO{
	
	private static final long serialVersionUID = 6660275026063940137L;
	private String payid;//支付id
	private String payway;//支付方式
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

	public String getPayid() {
		return payid;
	}

	public void setPayid(String payid) {
		this.payid = payid;
	}

	public String getPayway() {
		return payway;
	}

	public void setPayway(String payway) {
		this.payway = payway;
	}
	
}
