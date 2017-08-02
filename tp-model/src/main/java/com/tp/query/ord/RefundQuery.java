package com.tp.query.ord;

import java.io.Serializable;
import java.util.Date;

import com.tp.model.BaseDO;

/**
 * 退款查询参数
 * @author szy
 *
 */
public class RefundQuery extends BaseDO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2682254988127021166L;

	private String orderCode;
	
	private String refundCode;
	
	private Integer refundStatus;
	
	private Date createTimeBegin;
	
	private Date createTimeEnd;
	
	private Long gatewayId;
	
	private Integer refundType;
	
	private Double refundAmountStart;
	
	private Double refundAmountEnd;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getRefundCode() {
		return refundCode;
	}

	public void setRefundCode(String refundCode) {
		this.refundCode = refundCode;
	}

	public Date getCreateTimeBegin() {
		return createTimeBegin;
	}

	public void setCreateTimeBegin(Date createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}

	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public Integer getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
	}

	public Long getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(Long gatewayId) {
		this.gatewayId = gatewayId;
	}

	public Integer getRefundType() {
		return refundType;
	}

	public void setRefundType(Integer refundType) {
		this.refundType = refundType;
	}

	public Double getRefundAmountStart() {
		return refundAmountStart;
	}

	public void setRefundAmountStart(Double refundAmountStart) {
		this.refundAmountStart = refundAmountStart;
	}

	public Double getRefundAmountEnd() {
		return refundAmountEnd;
	}

	public void setRefundAmountEnd(Double refundAmountEnd) {
		this.refundAmountEnd = refundAmountEnd;
	}
}
