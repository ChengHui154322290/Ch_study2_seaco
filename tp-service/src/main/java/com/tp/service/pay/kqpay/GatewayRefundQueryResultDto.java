package com.tp.service.pay.kqpay;

import java.io.Serializable;

public class GatewayRefundQueryResultDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String payTypeDesc;//原交易类型
	
	private String orderTime;//退款创建时间
	
	private String lastUpdateTime;//退款最后一次更新时间
	
	private String sequenceId;//交易流水号
	
	private String orderId;//退款订单号
	
	private String rOrderId;//原商家订单号
	
	private String dealOtherName;//交易对方
	
	private String orderAmout;//退款金额
	
	private String ownerFee;//费用
	
	private String extra_output_column;//额外输出参数
	
	private String status;//交易状态
	
	private String failReason;//退款失败原因
	
	private String signInfo;//订单签名字符串

	public String getExtra_output_column() {
		return extra_output_column;
	}

	public void setExtra_output_column(String extra_output_column) {
		this.extra_output_column = extra_output_column;
	}

	public String getPayTypeDesc() {
		return payTypeDesc;
	}

	public void setPayTypeDesc(String payTypeDesc) {
		this.payTypeDesc = payTypeDesc;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(String sequenceId) {
		this.sequenceId = sequenceId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getROrderId() {
		return rOrderId;
	}

	public void setROrderId(String orderId) {
		rOrderId = orderId;
	}

	public String getDealOtherName() {
		return dealOtherName;
	}

	public void setDealOtherName(String dealOtherName) {
		this.dealOtherName = dealOtherName;
	}

	public String getOrderAmout() {
		return orderAmout;
	}

	public void setOrderAmout(String orderAmout) {
		this.orderAmout = orderAmout;
	}

	public String getOwnerFee() {
		return ownerFee;
	}

	public void setOwnerFee(String ownerFee) {
		this.ownerFee = ownerFee;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public String getSignInfo() {
		return signInfo;
	}

	public void setSignInfo(String signInfo) {
		this.signInfo = signInfo;
	}

	@Override
	public String toString() {
		return "payTypeDesc=" + payTypeDesc + "\norderTime=" + orderTime + "\nlastUpdateTime=" + lastUpdateTime + "\nsequenceId="
				+ sequenceId + "\norderId=" + orderId + "\nrOrderId=" + rOrderId + "\ndealOtherName=" + dealOtherName + "\norderAmout=" + orderAmout
				+ "\nownerFee=" + ownerFee + "\nextra_output_column=" + extra_output_column + "\nstatus=" + status + "\nfailReason=" + failReason
				+ "\nsignInfo=" + signInfo;
	}
	
	
}
