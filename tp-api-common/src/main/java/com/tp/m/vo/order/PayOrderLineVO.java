package com.tp.m.vo.order;

import java.util.List;

import com.tp.m.base.BaseVO;
import com.tp.m.vo.pay.PaywayVO;

public class PayOrderLineVO implements BaseVO{

	private static final long serialVersionUID = 1719930906656127362L;

	private String ordercode;//订单编号
	private String orderprice;//订单金额
	private String payid;//支付ID
	private List<PaywayVO> payways; 

	public PayOrderLineVO() {
		super();
	}

	public String getOrdercode() {
		return ordercode;
	}

	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}

	public String getOrderprice() {
		return orderprice;
	}

	public void setOrderprice(String orderprice) {
		this.orderprice = orderprice;
	}

	public String getPayid() {
		return payid;
	}

	public void setPayid(String payid) {
		this.payid = payid;
	}

	public List<PaywayVO> getPayways() {
		return payways;
	}

	public void setPayways(List<PaywayVO> payways) {
		this.payways = payways;
	}
}
