package com.tp.m.query.promotion;

import com.tp.m.base.BaseQuery;

/**
 * 专题入参
 * @author zhuss
 * @2016年1月4日 下午6:46:50
 */
public class QueryGroupbuy extends BaseQuery{

	private static final long serialVersionUID = 3090808301837190132L;

	private String gbid;
	private String gid;

	public String getGbid() {
		return gbid;
	}

	public void setGbid(String gbid) {
		this.gbid = gbid;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}
}
