package com.tp.m.query.verify;

import com.tp.m.base.BaseQuery;

public class QueryCaptcha extends BaseQuery{

	private static final long serialVersionUID = -2024081643960650721L;
	
	private String tel;
	private String captchaimage;//图片验证码
	private String type; //图形验证码的类型：0表示用户登录 1表示用户注册
	private String uuid;
	
	
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getCaptchaimage() {
		return captchaimage;
	}
	public void setCaptchaimage(String captchaimage) {
		this.captchaimage = captchaimage;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
