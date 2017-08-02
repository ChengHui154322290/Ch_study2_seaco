package com.tp.m.vo.coupon;

import com.tp.m.base.BaseVO;

/**
 * 优惠券数量
 * @author zhuss
 *
 */
public class CouponCountVO implements BaseVO {

	private static final long serialVersionUID = 605556568892361991L;
	private String count;
	
	
	public CouponCountVO() {
		super();
	}
	public CouponCountVO(String count) {
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
