package com.tp.dto.mmp.enums;

/**
 * 优惠券发放领取类型
 *
 */
public enum CouponOfferType {

	ALL(0, "可发可领"),

	SEND(1,"可发"),

	RECEIVE(2,"可领");
	
	private int code;
	
    private String desc;

	CouponOfferType(int code,String desc){
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
}
