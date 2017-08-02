package com.tp.dto.mmp.enums;

/**
 * 优惠券发放申请状态
 * @author szy
 *
 */
public enum CouponSendStatus {

	EDITING,  //0编辑中 
	AUDITING, //1审批中 
	CANCELED,   //2已取消 
	PASSED,     //3审核通过
	REFUSED,  //4已驳回
	STOP;     //5 终止
	
	public static CouponSendStatus parse(int open) {
		for (CouponSendStatus status : CouponSendStatus.values()) {
			if ((byte) status.ordinal() == open) {
				return status;
			}
		}
		return null;
	}

}
