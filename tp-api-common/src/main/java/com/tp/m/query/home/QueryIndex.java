package com.tp.m.query.home;

import com.tp.m.base.BaseQuery;

/**
 * 首页入参
 * @author zhuss
 * @2016年1月2日 下午3:00:18
 */
public class QueryIndex extends BaseQuery{

	private static final long serialVersionUID = 3590322779326514797L;
	
	//分销店铺电话
	private String shopmobile;

	public String getShopmobile() {
		return shopmobile;
	}

	public void setShopmobile(String shopmobile) {
		this.shopmobile = shopmobile;
	}
	
}
