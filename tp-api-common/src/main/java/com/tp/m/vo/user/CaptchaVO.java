package com.tp.m.vo.user;

import com.tp.m.base.BaseVO;


/**
 * 验证码相关出参
 * @author zhuss
 * @2016年1月3日 下午7:39:53
 */
public class CaptchaVO implements BaseVO{
	private static final long serialVersionUID = -3468663542644817350L;

	private String livetime;//存活时间：单位是秒

	
	public CaptchaVO() {
		super();
	}
	public CaptchaVO(String livetime) {
		super();
		this.livetime = livetime;
	}
	public String getLivetime() {
		return livetime;
	}
	public void setLivetime(String livetime) {
		this.livetime = livetime;
	}
}
