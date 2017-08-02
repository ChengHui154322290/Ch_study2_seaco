package com.tp.m.vo.pay;

import com.tp.m.base.BaseVO;

/**
 * APP支付信息父类
 * @author zhuss
 * @2016年1月8日 上午11:10:17
 */
public class BasePayVO implements BaseVO{
	
	private static final long serialVersionUID = -1010881738283591673L;
	
	private String ordercode;
	private String price;
	private String payid;
	
	public String getOrdercode() {
		return ordercode;
	}
	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPayid() {
		return payid;
	}
	public void setPayid(String payid) {
		this.payid = payid;
	}
	
}
