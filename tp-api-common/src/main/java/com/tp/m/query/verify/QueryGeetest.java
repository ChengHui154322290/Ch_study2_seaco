/**
 * NewHeight.com Inc.
 * Copyright (c) 2008-2010 All Rights Reserved.
 */
package com.tp.m.query.verify;

import com.tp.m.base.BaseQuery;

/**
 * 极验
 * @author zhuss
 */
public class QueryGeetest extends BaseQuery {

	
	private static final long serialVersionUID = 7013782562456457908L;
	//极验标志   0：登录  1：注册
	private String type;
	private String geetest_challenge;
	private String geetest_validate;
	private String geetest_seccode;
	private String uuid;
	
	public String getGeetest_challenge() {
		return geetest_challenge;
	}
	public void setGeetest_challenge(String geetest_challenge) {
		this.geetest_challenge = geetest_challenge;
	}
	public String getGeetest_validate() {
		return geetest_validate;
	}
	public void setGeetest_validate(String geetest_validate) {
		this.geetest_validate = geetest_validate;
	}
	public String getGeetest_seccode() {
		return geetest_seccode;
	}
	public void setGeetest_seccode(String geetest_seccode) {
		this.geetest_seccode = geetest_seccode;
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
