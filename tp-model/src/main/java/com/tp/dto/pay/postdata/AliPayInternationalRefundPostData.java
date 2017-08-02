package com.tp.dto.pay.postdata;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.tp.common.vo.PaymentConstant.CurrencyEnum;
import com.tp.util.AlipayUtil;
import com.tp.util.DateUtil;

/**
 * 支付宝国际退款实体
 * 
 * @author szy
 * @version 0.0.1
 *          zhouhui Exp $
 */
public class AliPayInternationalRefundPostData
		implements
			Serializable,
			RefundPostData {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5860032384516390393L;
	// 服务名称
	private String service = "forex_refund";
	// 境外商户id
	private String partner;
	// 字符集
	private String inputCharset = "utf-8";
	// 签名方式
	private String signType = "MD5";
	// 外部退款号
	private String outReturnNo; // TODO 待确认
	// 外部交易号
	private String outTradeNo;
	// 退款金额（rmb）
	private String returnRmbAmount;
	// 退款时间
	private String gmtReturn;
	// 退款原因
	private String reason;

	private String currency;

	private String key;

	private Map<String, String> sParam = new HashMap<String, String>();

	public AliPayInternationalRefundPostData() {

	}

	public AliPayInternationalRefundPostData(Properties paymentConfig) {
		this.partner = paymentConfig.getProperty("ALIPAY_INTERNATIONAL_PARTNER");
		this.key = paymentConfig.getProperty("ALIPAY_INTERNATIONAL_KEY");
		this.inputCharset = paymentConfig.getProperty("ALIPAY_INPUT_CHARSET");
		this.signType = paymentConfig.getProperty("ALIPAY_SIGN_TYPE");
		this.currency = CurrencyEnum.USD.toString();
//		this.outTradeNo = dto.getGatewayTradeNo(); // 支付宝返回的流水号
//		this.outReturnNo = dto.getBizCode(); // 退款传给支付宝的流水号 每次退款
												// 生成一个唯一的不然会报错
		this.gmtReturn = DateUtil.getLongDateString(new Date());
//		this.returnRmbAmount = String.valueOf(dto.getAmount());
		this.reason = "refund";
	}

	@Override
	public String getSignature() {
		sParam.put("service", "forex_refund");
		sParam.put("partner", getPartner());
		sParam.put("_input_charset", getInputCharset());
		sParam.put("out_return_no", getOutReturnNo());
		sParam.put("out_trade_no", getOutTradeNo());
		sParam.put("return_amount", getReturnRmbAmount());
		sParam.put("currency", getCurrency());
		sParam.put("gmt_return", getGmtReturn());
		sParam.put("reason", getReason());
		return AlipayUtil.buildRequestPara(sParam, key, inputCharset);
	}

	@Override
	public String getPaymentTradeNo() {
		return getOutTradeNo();
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getInputCharset() {
		return inputCharset;
	}

	public void setInputCharset(String inputCharset) {
		this.inputCharset = inputCharset;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getOutReturnNo() {
		return outReturnNo;
	}

	public void setOutReturnNo(String outReturnNo) {
		this.outReturnNo = outReturnNo;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getReturnRmbAmount() {
		return returnRmbAmount;
	}

	public void setReturnRmbAmount(String returnRmbAmount) {
		this.returnRmbAmount = returnRmbAmount;
	}

	public String getGmtReturn() {
		return gmtReturn;
	}

	public void setGmtReturn(String gmtReturn) {
		this.gmtReturn = gmtReturn;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
