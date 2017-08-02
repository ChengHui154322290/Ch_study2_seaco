package com.tp.query.ord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tp.model.BaseDO;

/**
 * 传递退货、拒收参数
 * @author szy
 *
 */
public class RejectQuery extends BaseDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7320439809990856525L;

	private Long orderCode;
	
	private Long rejectCode;
	
	private Long userId;
	
	private Integer rejectStatus;
	
	private Integer auditStatus;
	
	private Long supplierId;
	
	private String channelCode;
	
	private String orderByParams="create_time";
	private String orderBy="desc";
	
	private List<Long> rejectCodeList = new ArrayList<Long>();
	
	private List<Long> refundCodeList = new ArrayList<Long>();
	
	private List<Long> offsetCodeList = new ArrayList<Long>();
	
	private List<Integer> orderTypeList;
	
	public Long getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(Long orderCode) {
		this.orderCode = orderCode;
	}
	public Long getRejectCode() {
		return rejectCode;
	}
	public void setRejectCode(Long rejectCode) {
		this.rejectCode = rejectCode;
	}
	public Integer getRejectStatus() {
		return rejectStatus;
	}
	public void setRejectStatus(Integer rejectStatus) {
		this.rejectStatus = rejectStatus;
	}
	public Integer getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getOrderByParams() {
		return orderByParams;
	}
	public void setOrderByParams(String orderByParams) {
		this.orderByParams = orderByParams;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public List<Long> getRejectCodeList() {
		return rejectCodeList;
	}
	public void setRejectCodeList(List<Long> rejectCodeList) {
		this.rejectCodeList = rejectCodeList;
	}
	public List<Long> getRefundCodeList() {
		return refundCodeList;
	}
	public void setRefundCodeList(List<Long> refundCodeList) {
		this.refundCodeList = refundCodeList;
	}
	public List<Long> getOffsetCodeList() {
		return offsetCodeList;
	}
	public void setOffsetCodeList(List<Long> offsetCodeList) {
		this.offsetCodeList = offsetCodeList;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public List<Integer> getOrderTypeList() {
		return orderTypeList;
	}
	public void setOrderTypeList(List<Integer> orderTypeList) {
		this.orderTypeList = orderTypeList;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	
}
