package com.tp.dto.pay.postdata;

public interface RefundPostData {
	/**
	 * 进行数字签名
	 * @return 数字签名
	 */
	public String getSignature();
	/**
	 * 获取支付对账流水号.
	 * @return
	 */
	String getPaymentTradeNo();
}
