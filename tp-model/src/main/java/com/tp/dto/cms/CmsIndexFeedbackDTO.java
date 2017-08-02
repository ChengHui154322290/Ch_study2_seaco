package com.tp.dto.cms;

import java.io.Serializable;
import java.util.Date;

import com.tp.model.BaseDO;

/**
 * 页面反馈信息
 * 
 * @author szy
 */

public class CmsIndexFeedbackDTO extends BaseDO implements Serializable{

	private static final long serialVersionUID = 456808991858277755L;

	/** 主键 */
	private Long id;

	/** 用户id */
	private Long userId;

	/** 用户名字 */
	private String userName;

	/** 反馈信息 */
	private String feedbackInfo;

	/** 反馈时间 */
	private Date feedbackDate;
	
	/** 反馈时间字符 */
	private String feedbackDateStr;
	
	/** 手机号码 */
	private String mobile;
	
	/** 邮箱号码 */
	private String email;
	
	/** 当前页 */
	private Integer pageNo;

	/** 总页数 */
	private Integer totalCount;
	
	/** 总条数 */
	private Integer totalCountNum;

	/**
	 * 设置 主键
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 设置 用户id
	 * 
	 * @param userId
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * 设置 用户名字
	 * 
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 设置 反馈信息
	 * 
	 * @param feedbackInfo
	 */
	public void setFeedbackInfo(String feedbackInfo) {
		this.feedbackInfo = feedbackInfo;
	}

	/**
	 * 设置 反馈时间
	 * 
	 * @param feedbackDate
	 */
	public void setFeedbackDate(Date feedbackDate) {
		this.feedbackDate = feedbackDate;
	}

	/**
	 * 获取 主键
	 * 
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 获取 用户id
	 * 
	 * @return userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * 获取 用户名字
	 * 
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 获取 反馈信息
	 * 
	 * @return feedbackInfo
	 */
	public String getFeedbackInfo() {
		return feedbackInfo;
	}

	/**
	 * 获取 反馈时间
	 * 
	 * @return feedbackDate
	 */
	public Date getFeedbackDate() {
		return feedbackDate;
	}

	public String getFeedbackDateStr() {
		return feedbackDateStr;
	}

	public void setFeedbackDateStr(String feedbackDateStr) {
		this.feedbackDateStr = feedbackDateStr;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getTotalCountNum() {
		return totalCountNum;
	}

	public void setTotalCountNum(Integer totalCountNum) {
		this.totalCountNum = totalCountNum;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}