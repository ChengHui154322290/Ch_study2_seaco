package com.tp.dto.mmp.enums;

/**
 * 优惠券发放申请状态
 * @author szy
 *
 */
public enum CouponSendSendStatus {

	NOSEND,  //0 未发放
	SENDED ; //1 已发放
	
	public static CouponSendSendStatus parse(int open) {
		for (CouponSendSendStatus status : CouponSendSendStatus.values()) {
			if ((byte) status.ordinal() == open) {
				return status;
			}
		}
		return null;
	}

}
