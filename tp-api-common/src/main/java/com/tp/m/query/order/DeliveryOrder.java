package com.tp.m.query.order;

/**
 * 速购配送
 * @author szy
 *
 */
public class DeliveryOrder extends QueryOrder {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3870682461076095443L;
	
	private String username;
	private String fastuserid;//配送人员ID
	private String fastusername;//配送人员姓名
	private String fastusermobile;//配送人员手机
	private String remark;//备注
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFastuserid() {
		return fastuserid;
	}
	public void setFastuserid(String fastuserid) {
		this.fastuserid = fastuserid;
	}
	public String getFastusername() {
		return fastusername;
	}
	public void setFastusername(String fastusername) {
		this.fastusername = fastusername;
	}
	public String getFastusermobile() {
		return fastusermobile;
	}
	public void setFastusermobile(String fastusermobile) {
		this.fastusermobile = fastusermobile;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
