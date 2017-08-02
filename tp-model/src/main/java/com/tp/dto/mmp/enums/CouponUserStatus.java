package com.tp.dto.mmp.enums;

/**
 * 发放后的优惠券状态
 *
 */
public enum CouponUserStatus {

	NORMAL(0,"正常有效"),    // 正常有效  0

	USED(1,"已使用"),    //已使用  1

	OVERDUE(2,"过期"),  //过期  2

	INVALID(3,"无效 作废 "),  //无效 作废 3

	DELETED(4,"删除 "); //删除  4
	
	private Integer code;
	private String cnName;
	CouponUserStatus(Integer code,String cnName){
		this.code = code;
		this.cnName = cnName;
	}
	
	public static CouponUserStatus parse(int open) {
		for (CouponUserStatus status : CouponUserStatus.values()) {
			if ((byte) status.ordinal() == open) {
				return status;
			}
		}
		return null;
	}

	public static String getCnName(Integer code){
		for (CouponUserStatus status : CouponUserStatus.values()) {
			if (status.code.equals(code)) {
				return status.cnName;
			}
		}
		return null;
	}
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

}
