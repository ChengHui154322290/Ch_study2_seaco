package com.tp.dto.mmp.enums;

/**
 * 优惠券类型
 *
 */
public enum CouponType {

	HAS_CONDITION,//满减券  0

	NO_CONDITION,//现金券 红包 1
	FIRST_MINUS;//首单立减
	
	public static CouponType parse(int open) {
		for (CouponType status : CouponType.values()) {
			if ((byte) status.ordinal() == open) {
				return status;
			}
		}
		return null;
	}

}
