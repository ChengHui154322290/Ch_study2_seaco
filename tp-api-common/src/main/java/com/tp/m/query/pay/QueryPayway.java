package com.tp.m.query.pay;

import com.tp.m.base.BaseQuery;

public class QueryPayway extends BaseQuery{

	private static final long serialVersionUID = 2904905428066315492L;
	private String ordertype;//订单类型
	private String channelid;//支付渠道ID
	
	
	public QueryPayway() {
		super();
	}
	public QueryPayway(String ordertype, String channelid) {
		super();
		this.ordertype = ordertype;
		this.channelid = channelid;
	}
	public String getOrdertype() {
		return ordertype;
	}
	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}
	public String getChannelid() {
		return channelid;
	}
	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}
}
