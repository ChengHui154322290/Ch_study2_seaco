package com.tp.m.vo.user;

import com.tp.m.base.BaseVO;

/**
 * 用户实名认证
 * @author zhuss
 * @2016年1月4日 下午1:52:11
 */
public class UserAuthVO implements BaseVO{

	private static final long serialVersionUID = -5876667012678725261L;

	private String name;//身份证人姓名
	private String code;//身份证号码
	private String imgfront;//正面照
	private String imgback;//背面照
	
	
	public UserAuthVO() {
		super();
	}
	public UserAuthVO(String name, String code, String imgfront,
			String imgback) {
		super();
		this.name = name;
		this.code = code;
		this.imgfront = imgfront;
		this.imgback = imgback;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getImgfront() {
		return imgfront;
	}
	public void setImgfront(String imgfront) {
		this.imgfront = imgfront;
	}
	public String getImgback() {
		return imgback;
	}
	public void setImgback(String imgback) {
		this.imgback = imgback;
	}
}
