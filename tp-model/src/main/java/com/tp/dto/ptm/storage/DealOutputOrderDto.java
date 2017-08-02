package com.tp.dto.ptm.storage;


import java.io.Serializable;

/**
 * 
 * @author shaoqunxi
 *
 */
public class DealOutputOrderDto implements Serializable{

	private static final long serialVersionUID = 3180700671391936420L;

	/**
	 * 已经取消的订单
	 */
	private String cancleOrders;
	/**
	 * 待收货的订单
	 */
	private String receiptOrders;
	
	/**
	 * 失败条数
	 */
	private int failNums ;
	
	/**
	 * 发货成功条数
	 */
	private int successNums ;

	/**
	 * @return the cancleOrders
	 */
	public String getCancleOrders() {
		return cancleOrders;
	}

	/**
	 * @param cancleOrders the cancleOrders to set
	 */
	public void setCancleOrders(String cancleOrders) {
		this.cancleOrders = cancleOrders;
	}

	/**
	 * @return the receiptOrders
	 */
	public String getReceiptOrders() {
		return receiptOrders;
	}

	/**
	 * @param receiptOrders the receiptOrders to set
	 */
	public void setReceiptOrders(String receiptOrders) {
		this.receiptOrders = receiptOrders;
	}

	/**
	 * @return the failNums
	 */
	public int getFailNums() {
		return failNums;
	}

	/**
	 * @param failNums the failNums to set
	 */
	public void setFailNums(int failNums) {
		this.failNums = failNums;
	}

	/**
	 * @return the successNums
	 */
	public int getSuccessNums() {
		return successNums;
	}

	/**
	 * @param successNums the successNums to set
	 */
	public void setSuccessNums(int successNums) {
		this.successNums = successNums;
	}
}
