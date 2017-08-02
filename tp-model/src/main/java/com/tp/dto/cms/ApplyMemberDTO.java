package com.tp.dto.cms;

import java.io.Serializable;

public class ApplyMemberDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7621455529024041799L;
	
	/** 申请的用户id*/
	private Long userId;
	
	/** 参加的达人活动id*/
	private Long activityId;
	
	/** 手机号*/
	private String mobile; 
	
	/**达人宣言 */
	private String declaration; 
	
	/**申请用户名 */
	private String userName;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getActivityId() {
		return activityId;
	}
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getDeclaration() {
		return declaration;
	}
	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	

}
