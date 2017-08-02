/**
 * 
 */
package com.tp.dto.mmp;

import java.io.Serializable;

import com.tp.model.BaseDO;

public class ExchangeCouponCodeDTO extends BaseDO implements Serializable {

	private static final long serialVersionUID = -6585153574742952074L;

	/** 用户ID */
	private Long userId;

	/** 兑换码 */
	private String exchangeCode;
	/**兑换码批次*/
	private String couponId;
	/**个人优惠券ID*/
	private Long couponUserId;

	/** 用户手机号 */
	private String mobile;

	/** 用户名 */
	private String nickName;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getExchangeCode() {
		return exchangeCode;
	}

	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public Long getCouponUserId() {
		return couponUserId;
	}

	public void setCouponUserId(Long couponUserId) {
		this.couponUserId = couponUserId;
	}
    
}
