package com.tp.service.pay.kqpay;

import java.io.Serializable;

public class GatewayRefundQueryRequest implements Serializable{
	private static final long serialVersionUID = 1L;

	private String version; // 接口版本
	private String signType; // 签名类型
	private String merchantAcctId; //人民币账号
	private String startDate;//开始日期
	private String endDate;//结束日期
	private String lastupdateStartDate;//退款完成查询开始时间
	private String lastupdateEndDate;//退款完成查询结束时间
	private String customerBatchId;//客户方的批次号（满足其业务上的需要）
	private String orderId;//退款订单号
	private String requestPage; // 请求记录集页码
	private String rOrderId;//原商家订单号
	private String seqId;//交易号
	private String status;//交易状态
	private String extra_output_column;//额外输出参数
	private String signMsg; // 签名字符串

	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getMerchantAcctId() {
		return merchantAcctId;
	}
	public void setMerchantAcctId(String merchantAcctId) {
		this.merchantAcctId = merchantAcctId;
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
	public String getCustomerBatchId() {
		return customerBatchId;
	}
	public void setCustomerBatchId(String customerBatchId) {
		this.customerBatchId = customerBatchId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getRequestPage() {
		return requestPage;
	}
	public void setRequestPage(String requestPage) {
		this.requestPage = requestPage;
	}
	public String getROrderId() {
		return rOrderId;
	}
	public void setROrderId(String rOrderId) {
		this.rOrderId = rOrderId;
	}
	public String getSeqId() {
		return seqId;
	}
	public void setSeqId(String seqId) {
		this.seqId = seqId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSignMsg() {
		return signMsg;
	}
	public void setSignMsg(String signMsg) {
		this.signMsg = signMsg;
	}
	public String getExtra_output_column() {
		return extra_output_column;
	}
	public void setExtra_output_column(String extra_output_column) {
		this.extra_output_column = extra_output_column;
	}
	

}
