package com.tp.query.ord;

import java.io.Serializable;

/**
 * 传递退货、拒收参数
 * @author szy
 *
 */
public class RejectAudit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7320439809990856525L;

	private Long rejectCode;
	
	private Long rejectId;
	
	private Long rejectItemId;
	
	private Boolean success;
	
	private String remark;
	
	private String createUser;
	
	private Double amount;
	
	private Integer points;

	private String returnAddress;
	
	private String returnContact;
	
	private String returnMobile;

	public String getReturnContact() {
		return returnContact;
	}
	
	public void  setReturnContact(String returnContact) {
		this.returnContact = returnContact;
	}
	
	public String getReturnMobile() {
		return returnMobile;
	}
	
	public void setReturnMobile(String returnMobile) {
		this.returnMobile = returnMobile;
	}
	
	public String getReturnAddress() {
		return returnAddress;
	}
	
	public Long getRejectCode() {
		return rejectCode;
	}

	public void setRejectCode(Long rejectCode) {
		this.rejectCode = rejectCode;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getRemark() {
		return remark;
	}

	public void setReturnAddress(String address) {
		this.returnAddress = address;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getRejectItemId() {
		return rejectItemId;
	}

	public void setRejectItemId(Long rejectItemId) {
		this.rejectItemId = rejectItemId;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Long getRejectId() {
		return rejectId;
	}

	public void setRejectId(Long rejectId) {
		this.rejectId = rejectId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}
}
