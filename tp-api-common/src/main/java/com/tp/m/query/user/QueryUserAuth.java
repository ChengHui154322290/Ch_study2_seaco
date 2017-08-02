package com.tp.m.query.user;

import com.tp.m.base.BaseQuery;

/**
 * 用户实名认证
 * @author zhuss
 * @2016年1月4日 下午2:53:21
 */
public class QueryUserAuth extends BaseQuery{
	private static final long serialVersionUID = 5426802200799645478L;
	private String name;//身份证人姓名
	private String code;//身份证号码
	private String imgfront;//正面照
	private String imgback;//背面照
	
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
