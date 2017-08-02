package com.tp.dto.ord.remote;

import java.io.Serializable;

/**
 * 订单计数dto
 * 
 * @author szy
 * @version 0.0.1
 */
public class OrderCountDTO implements Serializable {

	private static final long serialVersionUID = -6683022318631387739L;
	
	/** 待支付 */
	private int payment;
	/** 待收货 */
	private int reception;
	/** 售后 */
	private int afterSale;
	/** 待使用数量 */
	private int unusecount;
	
	
	public int getPayment() {
		return payment;
	}
	public void setPayment(int payment) {
		this.payment = payment;
	}
	public int getReception() {
		return reception;
	}
	public void setReception(int reception) {
		this.reception = reception;
	}
	public int getAfterSale() {
		return afterSale;
	}
	public void setAfterSale(int afterSale) {
		this.afterSale = afterSale;
	}
	public int getUnusecount() {
		return unusecount;
	}
	public void setUnusecount(int unusecount) {
		this.unusecount = unusecount;
	}
	
}
