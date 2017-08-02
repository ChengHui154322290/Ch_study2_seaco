package com.tp.dto.pay.postdata;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.PaymentConstant.CurrencyEnum;
import com.tp.dto.pay.AppPayData;
import com.tp.model.pay.PaymentInfo;
import com.tp.util.AlipayUtil;

/**
 * 支付宝国际支付实体
 * 
 * @author zhouhui
 * @version $Id: AliPayInternationalPostData.java, v 0.1 2015年4月15日 下午2:13:29
 *          zhouhui Exp $
 */
public class AliPayInternationalMergePostData implements Serializable, PayPostData, AppPayData {
	// serialVersionUID
	private static final long serialVersionUID = -5851584272079923007L;

	// 服务名称
	private String service = "create_forex_trade";
	// 境外商户id
	private String partner;
	// 字符集
	private String inputCharset = "utf-8";
	// 针对该交易支付成功之后的通知接收URL。
	private String notifyUrl;
	// 交易付款成功之后，则结果返回url仅适合返回处理结果
	private String returnUrl;
	// 商品标题
	private String subject;
	// 交易金额
	private String totalFee;
	// 人民币交易金额当交易支付是rmb的时候 交易金额用rebFee 注rmbFee和totalFee不能同一时间出现，一个是国际支付一个是rmb支付
	private String rmbFee;
	// 商品描述
	private String body;
	// 币种非rmb的情况需要指定
	private String currency;
	// 外部交易号
	private String outTradeNo;
	// 签名机制
	private String sign;
	// 签名方式
	private String signType = "MD5";

	private String key = "";
	
	private String extParams;
	//分账信息
	private String split_fund_info="";
	
	private String productCode;
	
	private Map<String, String> sParam = new HashMap<String, String>();

	public Map<String, String> getsParam() {
		return sParam;
	}

	public void setsParam(Map<String, String> sParam) {
		this.sParam = sParam;
	}


	public AliPayInternationalMergePostData(){
		
	}

	@Override
	public String getSignature() {
		sParam.put("service", getService());
		sParam.put("partner", getPartner());
		sParam.put("_input_charset", getInputCharset());
		sParam.put("notify_url", getNotifyUrl());
		sParam.put("return_url", getReturnUrl());
		sParam.put("out_trade_no", getOutTradeNo());
		sParam.put("subject", getSubject());
		sParam.put("body", getBody());
		sParam.put("rmb_fee", String.valueOf(getRmbFee()));
		sParam.put("sign_type", getSignType());
		sParam.put("currency", CurrencyEnum.USD.toString());
		sParam.put("split_fund_info", getSplit_fund_info());
		sParam.put("product_code", productCode);
		return AlipayUtil.buildRequestMysign(sParam, getKey());
	}
	
	public AliPayInternationalMergePostData(Properties paymentConfig, PaymentInfo dto) {
		this.partner = paymentConfig.getProperty("ALIPAY_MERGEALIPAY_PARTNER");
		this.key = paymentConfig.getProperty("ALIPAY_MERGEALIPAY_KEY");
		this.inputCharset = paymentConfig.getProperty("ALIPAY_INPUT_CHARSET");
		this.notifyUrl = paymentConfig.getProperty("ALIPAY_MERGEALIPAY_NOTIFY_URL");
		this.returnUrl = paymentConfig.getProperty("ALIPAY_RETURN_URL")
				+ dto.getPaymentId();
		if(dto.getOrderType()!=null && OrderConstant.FAST_ORDER_TYPE.equals(dto.getOrderType().intValue())){
			this.notifyUrl = notifyUrl.replaceFirst("(.*://)?[^/]*/", paymentConfig.getProperty("fastPay.notify_url"));
			this.returnUrl = returnUrl.replaceFirst("(.*://)?[^/]*/", paymentConfig.getProperty("fastPay.notify_url"));
		}
		this.subject = paymentConfig.getProperty("ALIPAY_SUBJECT");
		this.signType = paymentConfig.getProperty("ALIPAY_SIGN_TYPE");
		this.currency = CurrencyEnum.USD.toString();
		this.outTradeNo = dto.getPaymentTradeNo();
		this.rmbFee = String.format("%.2f", dto.getAmount());
		this.split_fund_info=getSplit_fund_info();
		this.productCode = "NEW_OVERSEAS_SELLER";
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

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getRmbFee() {
		return rmbFee;
	}

	public void setRmbFee(String rmbFee) {
		this.rmbFee = rmbFee;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getExtParams() {
		return extParams;
	}

	public void setExtParams(String extParams) {
		this.extParams = extParams;
	}

	public String getSplit_fund_info() {
		return split_fund_info;
	}

	public void setSplit_fund_info(String split_fund_info) {
		this.split_fund_info = split_fund_info;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
}
