package com.tp.m.enums;

/**
 * 检验入参字段是否合法
 * @author zhuss
 * @2016年1月2日 下午4:10:32
 */
public enum ValidFieldType {

	LOGONNAME("logonname","登录用户"),
	TELEPHONE("telephone","手机号"),
	EMAIL("email","邮箱"),
	PASSWORD("password","密码"),
	CAPTCHA("captcha","手机验证码"),
	ID("id","身份证号码");
	public String code;
	
    public String desc;

	private ValidFieldType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	} 
}
