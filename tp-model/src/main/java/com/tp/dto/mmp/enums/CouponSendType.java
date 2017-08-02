package com.tp.dto.mmp.enums;

/**
 * 优惠券发放申请状态
 * @author szy
 *
 */
public enum CouponSendType {

	NORMAL,  //0 手动发放
	AUTO_NEWUSER, //1 新用户自动发放
	AUTO_SHARE; // 2 分享自动发放
	
	
	public static CouponSendType parse(int open) {
		for (CouponSendType status : CouponSendType.values()) {
			if ((byte) status.ordinal() == open) {
				return status;
			}
		}
		return null;
	}

}
