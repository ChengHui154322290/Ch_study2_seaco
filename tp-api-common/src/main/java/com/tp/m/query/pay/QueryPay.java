package com.tp.m.query.pay;

import com.tp.m.base.BaseQuery;

public class QueryPay extends BaseQuery{

	private static final long serialVersionUID = 4281882667234606068L;

	private String payid;//支付ID - 用于提交订单后的待支付列表支付
	private String payway;
	private String ordercode;//订单号 - 用于用户中心的待支付列表支付
	private String openid; //wap的微信支付必传字段属性
	private boolean isSdk;
	
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
	public String getOrdercode() {
		return ordercode;
	}
	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}
	public boolean isSdk() {
		return isSdk;
	}
	public void setSdk(boolean isSdk) {
		this.isSdk = isSdk;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
}
