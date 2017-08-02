package com.tp.m.query.promotion;

import com.tp.m.base.BaseQuery;

/**
 * 专题入参
 * @author zhuss
 * @2016年1月4日 下午6:46:50
 */
public class QueryTopic extends BaseQuery{

	private static final long serialVersionUID = 3090808301837190132L;

	private String tid;//专题ID
	private String sort; //排序
	
	// 分销员ID for dss
	private Long promoterId;
	
	//分销店铺电话
	private String shopmobile;
	
	public String getShopmobile() {
		return shopmobile;
	}
	public void setShopmobile(String shopmobile) {
		this.shopmobile = shopmobile;
	}
	public Long getPromoterId() {
		return promoterId;
	}
	public void setPromoterId(Long promoterId) {
		this.promoterId = promoterId;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
}
