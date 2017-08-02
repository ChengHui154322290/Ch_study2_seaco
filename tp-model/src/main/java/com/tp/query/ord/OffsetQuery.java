package com.tp.query.ord;

import java.io.Serializable;
import java.util.Date;

import com.tp.model.ord.OffsetInfo;

/**
 * 补偿查询参数
 * @author szy
 *
 */
public class OffsetQuery extends OffsetInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6825482578177316293L;

	private Long orderCode;
	
	private Long offsetCode;
	
	private Integer offsetStatus;
	
	private Integer reason;
	
	private Long offsetAmountStart;
	
	private Long offsetAmountEnd;
	
	private Date createDateBegin;
	
	private Date createDateEnd;

	public Integer getOffsetStatus() {
		return offsetStatus;
	}

	public void setOffsetStatus(Integer offsetStatus) {
		this.offsetStatus = offsetStatus;
	}

	public Integer getReason() {
		return reason;
	}

	public void setReason(Integer reason) {
		this.reason = reason;
	}

	public Long getOffsetAmountStart() {
		return offsetAmountStart;
	}

	public void setOffsetAmountStart(Long offsetAmountStart) {
		this.offsetAmountStart = offsetAmountStart;
	}

	public Long getOffsetAmountEnd() {
		return offsetAmountEnd;
	}

	public void setOffsetAmountEnd(Long offsetAmountEnd) {
		this.offsetAmountEnd = offsetAmountEnd;
	}

	public Date getCreateDateBegin() {
		return createDateBegin;
	}

	public void setCreateDateBegin(Date createDateBegin) {
		this.createDateBegin = createDateBegin;
	}

	public Date getCreateDateEnd() {
		return createDateEnd;
	}

	public void setCreateDateEnd(Date createDateEnd) {
		this.createDateEnd = createDateEnd;
	}

	public Long getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(Long orderCode) {
		this.orderCode = orderCode;
	}

	public Long getOffsetCode() {
		return offsetCode;
	}

	public void setOffsetCode(Long offsetCode) {
		this.offsetCode = offsetCode;
	}
}
