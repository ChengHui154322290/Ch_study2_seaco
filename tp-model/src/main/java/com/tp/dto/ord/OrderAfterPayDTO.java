package com.tp.dto.ord;

/**
 * 支付回调订单dto
 * 
 * @author szy
 * @version 0.0.1
 */
public class OrderAfterPayDTO implements BaseDTO {

	private static final long serialVersionUID = -6436223080230518751L;

	/** 父订单id */
	private Long orderId;
	private Long orderCode;
	private Integer status;
	private Long memberId;
	private Long gatewayId;
	/** 会员昵称 */
	private String nickname;
	
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Long getGatewayId() {
		return gatewayId;
	}
	public void setGatewayId(Long gatewayId) {
		this.gatewayId = gatewayId;
	}
	public Long getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(Long orderCode) {
		this.orderCode = orderCode;
	}
}
