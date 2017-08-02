package com.tp.m.vo.verify;

import com.tp.m.base.BaseVO;

public class CaptchaVO implements BaseVO{

	private static final long serialVersionUID = -2578946438528684074L;
	private String key;
	private String verifycode;//验证码
	private int errorcount;//错误次数
	
	
	public CaptchaVO() {
		super();
	}
	public CaptchaVO(String verifycode) {
		super();
		this.verifycode = verifycode;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getVerifycode() {
		return verifycode;
	}
	public void setVerifycode(String verifycode) {
		this.verifycode = verifycode;
	}
	public int getErrorcount() {
		return errorcount;
	}
	public void setErrorcount(int errorcount) {
		this.errorcount = errorcount;
	}
}
