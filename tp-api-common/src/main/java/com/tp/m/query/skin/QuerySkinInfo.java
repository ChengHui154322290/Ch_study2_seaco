package com.tp.m.query.skin;

import com.tp.m.base.BaseQuery;
/**
 * 皮肤入参
 * @author zhuss
 * @2016年1月5日 下午3:34:45
 */
public class QuerySkinInfo extends BaseQuery{
	private static final long serialVersionUID = -2355589949518377396L;
	private String skinid;//皮肤版本号
	public String getSkinid() {
		return skinid;
	}
	public void setSkinid(String skinid) {
		this.skinid = skinid;
	}
 
}
