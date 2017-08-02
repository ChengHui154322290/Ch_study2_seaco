package com.tp.common.vo.wms.kjb2c;

public class KjsoDeclResponse implements java.io.Serializable {

	private static final long serialVersionUID = -4984782719043054121L;
	
	private String storer;	// 货主编码
	private String externalNo;	// 跨境平台系统申报单号
	private String success;	// 处理结果：true-成功，false-失败
	private String lockFlag;	// 1=锁定标记，表示出库报检，存在被锁定的批次，仅当success=false有效，其他错误返回空
	private String notes;	// 备注: 显示锁定的批次 格式：货号(报检单号),货号(报检单号),…
	public String getStorer() {
		return storer;
	}
	public void setStorer(String storer) {
		this.storer = storer;
	}
	public String getExternalNo() {
		return externalNo;
	}
	public void setExternalNo(String externalNo) {
		this.externalNo = externalNo;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getLockFlag() {
		return lockFlag;
	}
	public void setLockFlag(String lockFlag) {
		this.lockFlag = lockFlag;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}
