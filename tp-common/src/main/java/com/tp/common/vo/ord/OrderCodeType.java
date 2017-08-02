package com.tp.common.vo.ord;

/**
 * 订单号类型（父订单、子订单）
 * 
 * @author szy
 * @version 0.0.1
 */
public enum OrderCodeType {

	/** 父订单 */
	PARENT, 
	/** 子订单 */
	SON,

	DSS_PAY,

	SEAGOOR_PAY,

	SEAGOOR_PAY_REFUND
}
