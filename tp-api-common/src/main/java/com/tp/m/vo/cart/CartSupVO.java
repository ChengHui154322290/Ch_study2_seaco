package com.tp.m.vo.cart;

import com.tp.m.base.BaseVO;

/**
 * 购物车角标
 * @author zhuss
 * @2016年1月7日 上午11:36:59
 */
public class CartSupVO implements BaseVO{

	private static final long serialVersionUID = 4655412906868426549L;

	private String count;
	
	public CartSupVO() {
		super();
	}
	public CartSupVO(String count) {
		super();
		this.count = count;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
}
