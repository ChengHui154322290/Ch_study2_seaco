/**
 * 
 */
package com.tp.dto.mmp.enums;

/**
 * @author szy
 *
 */
public enum CouponUserSource {
	FANS_PLAN(1, "达人粉丝计划"),
	ISSUE(2, "发放"),
	RECEIVE(3, "领取");
	
	private int type;
	private String description;
	
	private CouponUserSource(int type, String description) {
		this.type = type;
		this.description = description;
	}
	
	public int getValue() {
		return this.type;
	}
	
	public String getDescription() {
		return this.description;
	}
}
