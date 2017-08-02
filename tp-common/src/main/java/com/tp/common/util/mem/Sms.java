package com.tp.common.util.mem;

import java.io.Serializable;
import java.util.Date;

public class Sms implements Serializable{

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = -2683216145638177357L;

	/**
	 * 手机号码
	 */
	private String mobile;
	
	/**
	 * 短信内容
	 */
	private String content;
	
	/**
	 * 发送短信
	 */
	private Date sendTime;
	
	/***
	 * 发送IP
	 */
	private String ip;
	

	public Sms() {}
	
	public Sms(String mobile, String content) {
		super();
		this.mobile = mobile;
		this.content = content;
	}

	public Sms(String mobile, String content, Date sendTime) {
		super();
		this.mobile = mobile;
		this.content = content;
		this.sendTime = sendTime;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public String toString() {
		return "Sms [mobile=" + mobile + ", content=" + content + ", sendTime="
				+ sendTime + "]";
	}
	
}
