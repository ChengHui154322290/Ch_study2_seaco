package com.tp.dto.mmp.enums;

/**
 * 优惠券状态
 * @author szy
 *
 */
public enum CouponStatus {

	//NORMAL,    // 正常有效  0  = PASSED  审核通过
	//INVALID,//无效 作废 1  = STOP终止

	EDITING,  //0编辑中 
	AUDITING, //1审批中 
	CANCELED,   //2已取消 
	PASSED,     //3审核通过
	REFUSED,  //4已驳回
	STOP;     //5 终止
	
	public static CouponStatus parse(int open) {
		for (CouponStatus status : CouponStatus.values()) {
			if ((byte) status.ordinal() == open) {
				return status;
			}
		}
		return null;
	}

}
