package com.tp.m.query.product;

import com.tp.m.base.BaseQuery;

/**
 * 商品入参
 * @author zhuss
 * @2016年1月5日 下午3:34:45
 */
public class QueryProduct extends BaseQuery{

	private static final long serialVersionUID = -2355589949518377396L;

	private String pid;
	private String sku;//商品sku
	private String tid; //专题id	
	private String shopmobile; // 店铺手机	
	// 分销员ID
	private Long promoterId; 	
	
	public Long getPromoterId() {
		return promoterId;
	}	
	public void setPromoterId(Long promoterId) {
		this.promoterId = promoterId;
	}
	public String getShopmobile() {
		return shopmobile;
	}
	public void setShopmobile(String shopmobile) {
		this.shopmobile = shopmobile;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
}
