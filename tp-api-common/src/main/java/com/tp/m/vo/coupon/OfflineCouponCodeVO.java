package com.tp.m.vo.coupon;

import com.tp.m.base.BaseVO;

/**
 * 线下推广优惠码
 * @author zhuss
 */
public class OfflineCouponCodeVO implements BaseVO{

	private static final long serialVersionUID = 2063099400287733565L;

	private String code; //优惠码
	
	private String qrcode;//二维码图片地址
	
	

	public OfflineCouponCodeVO() {
		super();
	}

	public OfflineCouponCodeVO(String code, String qrcode) {
		super();
		this.code = code;
		this.qrcode = qrcode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
}
