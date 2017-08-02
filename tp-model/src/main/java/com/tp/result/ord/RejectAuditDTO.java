package com.tp.result.ord;

import java.io.Serializable;

/**
 * 传递退货、拒收参数
 * @author szy
 *
 */
public class RejectAuditDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7320439809990856525L;

	private String rejectNo;
	
	private Long rejectId;
	
	private Long rejectItemId;
	
	private Boolean success;
	
	private String remark;
	
	private String createUser;
	
	private Double amount;
	
	private Integer points;
	
	private String auditImage;

	
	public String getAuditImage() {
		return auditImage;
	}

	public void setAuditImage(String auditImage) {
		this.auditImage = auditImage;
	}

	public String getRejectNo() {
		return rejectNo;
	}

	public void setRejectNo(String rejectNo) {
		this.rejectNo = rejectNo;
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
