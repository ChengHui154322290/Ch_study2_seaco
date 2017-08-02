package com.tp.service.pay.kqpay;

public class GatewayRefundQueryDto {
	
	/**
	 * 交易对方.
	 */
	private String payee;

	/**
	 * 原商家订单号
	 */
	private String rOrderId;
	
	/**
	 * 退款订单号
	 */
	private String orderId;
	
	/**
	 * 交易号
	 */
	private String seqId;

	/**
	 * 客户方的批次号（满足其业务上的需要）
	 */
	private String customerBatchId;

	/**
	 * 订单完成开始日期
	 */
	private String startDate;

	/**
	 * 订单完成终止日期
	 */
	private String endDate;
	
	/**
	 * 人民币账号
	 */
	private String merchantAcctId;
	
	/**
	 * 交易状态
	 */
	private String status;	

	private String lastupdateStartDate;//退款完成查询开始时间
	private String lastupdateEndDate;//退款完成查询结束时间
	
	private String extra_output_column; //额外输出参数	

	public String getExtra_output_column() {
		return extra_output_column;
	}

	public void setExtra_output_column(String extra_output_column) {
		this.extra_output_column = extra_output_column;
	}

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}
	
	public String getROrderId() {
		return rOrderId;
	}

	public void setROrderId(String orderId) {
		rOrderId = orderId;
	}

	public String getCustomerBatchId() {
		return customerBatchId;
	}

	public void setCustomerBatchId(String customerBatchId) {
		this.customerBatchId = customerBatchId;
	}

	public String getMerchantAcctId() {
		return merchantAcctId;
	}

	public void setMerchantAcctId(String merchantAcctId) {
		this.merchantAcctId = merchantAcctId;
	}

	public String getSeqId() {
		return seqId;
	}

	public void setSeqId(String seqId) {
		this.seqId = seqId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getLastupdateStartDate() {
		return lastupdateStartDate;
	}

	public void setLastupdateStartDate(String lastupdateStartDate) {
		this.lastupdateStartDate = lastupdateStartDate;
	}

	public String getLastupdateEndDate() {
		return lastupdateEndDate;
	}

	public void setLastupdateEndDate(String lastupdateEndDate) {
		this.lastupdateEndDate = lastupdateEndDate;
	}

	
}
