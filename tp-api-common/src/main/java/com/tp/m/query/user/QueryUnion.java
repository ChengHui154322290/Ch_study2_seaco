package com.tp.m.query.user;

import com.tp.m.base.BaseQuery;

/**
 * 联合账户入参
 * @author zhuss
 * @2016年2月18日 下午3:57:31
 */
public class QueryUnion extends BaseQuery{

	private static final long serialVersionUID = 7434522412496750434L;

	private String openid;//微信绑定的唯一标识

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
}
