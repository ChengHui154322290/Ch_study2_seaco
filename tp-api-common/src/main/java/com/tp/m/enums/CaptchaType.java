package com.tp.m.enums;

import com.tp.m.util.StringUtil;

/**
 * 手机验证码类型
 * @author zhuss
 * @2016年1月2日 下午4:10:32
 */
public enum CaptchaType{

	REGIST("1","注册帐号"),
	UPDATE_PWD("2","修改密码"),
	BIND_TEL("3","绑定手机"),
	RECEIVE_COUPON("4","手机号领取优惠券"),
	REGIST_DSS("5","注册分销用户"),
	BIND_UNION("6","绑定联合账户"),
	MODIFY_MOBILE("11","更换手机号");
	public String code;
	
    public String desc;

	private CaptchaType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public static String getName(String code){
		for (CaptchaType c : CaptchaType.values()) {
			if (code.equals(c.code))
				return c.name();
		}
		return null;
	}

	public static boolean check(String code){
		if (StringUtil.isNotBlank(code)) {
			for (CaptchaType c : CaptchaType.values()) {
				if (code.equals(c.code))
					return true;
			}
		}
		return false;
	}
}
