package com.tp.model.cms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 页面反馈信息表
  */
public class IndexFeedback extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451848L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**用户id 数据类型bigint(11)*/
	private Long userId;
	
	/**用户名字 数据类型varchar(50)*/
	private String userName;
	
	/**手机号 数据类型varchar(30)*/
	private String mobile;
	
	/**邮箱号 数据类型varchar(128)*/
	private String email;
	
	/**反馈信息 数据类型varchar(3000)*/
	private String feedbackInfo;
	
	/**反馈时间 数据类型datetime*/
	private Date feedbackDate;
	
	
	public Long getId(){
		return id;
	}
	public Long getUserId(){
		return userId;
	}
	public String getUserName(){
		return userName;
	}
	public String getMobile(){
		return mobile;
	}
	public String getEmail(){
		return email;
	}
	public String getFeedbackInfo(){
		return feedbackInfo;
	}
	public Date getFeedbackDate(){
		return feedbackDate;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setUserId(Long userId){
		this.userId=userId;
	}
	public void setUserName(String userName){
		this.userName=userName;
	}
	public void setMobile(String mobile){
		this.mobile=mobile;
	}
	public void setEmail(String email){
		this.email=email;
	}
	public void setFeedbackInfo(String feedbackInfo){
		this.feedbackInfo=feedbackInfo;
	}
	public void setFeedbackDate(Date feedbackDate){
		this.feedbackDate=feedbackDate;
	}
}
