package com.tp.service.pay.cbdata;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.tp.dto.pay.cbdata.PayCallbackData;
import com.tp.util.DateUtil;


public class KqPayCallbackData implements PayCallbackData, Serializable {
	private static final long serialVersionUID = 6827094618981757555L;
	// 人民币网关账号，该账号为11位人民币网关商户编号+01,该值与提交时相同。
	String merchantAcctId;
	// 网关版本，固定值：v2.0,该值与提交时相同。
	String version;
	// 语言种类，1代表中文显示，2代表英文显示。默认为1,该值与提交时相同。
	String language;
	// 签名类型,该值为4，代表PKI加密方式,该值与提交时相同。
	String signType;
	// 支付方式，一般为00，代表所有的支付方式。如果是银行直连商户，该值为10,该值与提交时相同。
	String payType;
	// 银行代码，如果payType为00，该值为空；如果payType为10,该值与提交时相同。
	String bankId;
	// 已绑 短手机 尾号 前三位后四位手机号码
	String bindMobile;
	// 已绑短卡号
	String bindCard;
	// 商户订单号，该值与提交时相同。
	String orderId;
	// 订单提交时间，格式：yyyyMMddHHmmss，如：20071117020101,该值与提交时相同。
	String orderTime;
	// 订单金额，金额以“分”为单位，商户测试以1分测试即可，切勿以大金额测试,该值与支付时相同。
	String orderAmount;
	// 快钱交易号，商户每一笔交易都会在快钱生成一个交易号。
	String dealId;
	// 银行交易号 ，快钱交易在银行支付时对应的交易号，如果不是通过银行卡支付，则为空
	String bankDealId;
	// 快钱交易时间，快钱对交易进行处理的时间,格式：yyyyMMddHHmmss，如：20071117020101
	String dealTime;
	// 商户实际支付金额 以分为单位。比方10元，提交时金额应为1000。该金额代表商户快钱账户最终收到的金额。
	String payAmount;
	// 费用，快钱收取商户的手续费，单位为分。
	String fee;
	// 扩展字段1，该值与提交时相同。
	String ext1;
	// 扩展字段2，该值与提交时相同。
	String ext2;
	// 处理结果， 10支付成功，11 支付失败，00订单申请成功，01 订单申请失败
	String payResult;
	// 错误代码 ，请参照《人民币网关接口文档》最后部分的详细解释。
	String errCode;
	// 签名字符串
	String signMsg;

	public KqPayCallbackData(Map<String, String> parameterMap) {
		this.merchantAcctId = parameterMap.get("merchantAcctId");
		this.version = parameterMap.get("version");
		this.language = parameterMap.get("language");
		this.signType = parameterMap.get("signType");
		this.payType = parameterMap.get("payType");
		this.bindCard = parameterMap.get("bindCard");
		this.bindMobile = parameterMap.get("bindMobile");
		this.bankId = parameterMap.get("bankId");
		this.orderId = parameterMap.get("orderId");
		this.orderTime = parameterMap.get("orderTime");
		this.orderAmount = parameterMap.get("orderAmount");
		this.dealId = parameterMap.get("dealId");
		this.bankDealId = parameterMap.get("bankDealId");
		this.dealTime = parameterMap.get("dealTime");
		this.payAmount = parameterMap.get("payAmount");
		this.fee = parameterMap.get("fee");
		this.ext1 = parameterMap.get("ext1");
		this.ext2 = parameterMap.get("ext2");
		this.payResult = parameterMap.get("payResult");
		this.errCode = parameterMap.get("errCode");
		this.signMsg = parameterMap.get("signMsg");
	}

	public String getMerchantAcctId() {
		return merchantAcctId;
	}

	public void setMerchantAcctId(String merchantAcctId) {
		this.merchantAcctId = merchantAcctId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getBindMobile() {
		return bindMobile;
	}

	public void setBindMobile(String bindMobile) {
		this.bindMobile = bindMobile;
	}

	public String getBindCard() {
		return bindCard;
	}

	public void setBindCard(String bindCard) {
		this.bindCard = bindCard;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getDealId() {
		return dealId;
	}

	public void setDealId(String dealId) {
		this.dealId = dealId;
	}

	public String getBankDealId() {
		return bankDealId;
	}

	public void setBankDealId(String bankDealId) {
		this.bankDealId = bankDealId;
	}

	public String getDealTime() {
		return dealTime;
	}

	public void setDealTime(String dealTime) {
		this.dealTime = dealTime;
	}

	public String getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getPayResult() {
		return payResult;
	}

	public void setPayResult(String payResult) {
		this.payResult = payResult;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getSignMsg() {
		return signMsg;
	}

	public void setSignMsg(String signMsg) {
		this.signMsg = signMsg;
	}

	@Override
	public String getPaymentTradeNo() {
		return getOrderId();
	}

	@Override
	public String getGatewayTradeNo() {
		return getDealId();
	}

	@Override
	public boolean isSuccess() {
		return "10".equals(getPayResult());
	}

	@Override
	public String getMessage() {
		return getErrCode();
	}

	@Override
	public String getCallbackInfo() {
		return null;
	}

	@Override
	public long getPaymentAmount() {
		return Long.valueOf(getOrderAmount());
	}

	@Override
	public String getPaymentGateway() {
		return "快钱支付";
	}

	@Override
	public Date getCallBackTime() {
		return DateUtil.parse(getDealTime(), "yyyyMMddHHmmss");
	}

	@Override
	public String getCreateUserID() {
		return "kqpay";
	}

	public String getMsg2Sign() {
		StringBuilder result = new StringBuilder();
		result.append("merchantAcctId").append('=').append(merchantAcctId);
		if (StringUtils.isNotBlank(version)) {
			result.append('&').append("version").append('=').append(version);
		}
		if (StringUtils.isNotBlank(language)) {
			result.append('&').append("language").append('=').append(language);
		}
		if (StringUtils.isNotBlank(signType)) {
			result.append('&').append("signType").append('=').append(signType);
		}
		if (StringUtils.isNotBlank(payType)) {
			result.append('&').append("payType").append('=').append(payType);
		}
		if (StringUtils.isNotBlank(bankId)) {
			result.append('&').append("bankId").append('=').append(bankId);
		}
		if (StringUtils.isNotBlank(orderId)) {
			result.append('&').append("orderId").append('=').append(orderId);
		}
		if (StringUtils.isNotBlank(orderTime)) {
			result.append('&').append("orderTime").append('=')
					.append(orderTime);
		}
		if (StringUtils.isNotBlank(orderAmount)) {
			result.append('&').append("orderAmount").append('=')
					.append(orderAmount);
		}
		if (StringUtils.isNotBlank(bindCard)) {
			result.append('&').append("bindCard").append('=').append(bindCard);
		}
		if (StringUtils.isNotBlank(bindMobile)) {
			result.append('&').append("bindMobile").append('=')
					.append(bindMobile);
		}
		if (StringUtils.isNotBlank(dealId)) {
			result.append('&').append("dealId").append('=').append(dealId);
		}
		if (StringUtils.isNotBlank(bankDealId)) {
			result.append('&').append("bankDealId").append('=')
					.append(bankDealId);
		}
		if (StringUtils.isNotBlank(dealTime)) {
			result.append('&').append("dealTime").append('=').append(dealTime);
		}
		if (StringUtils.isNotBlank(payAmount)) {
			result.append('&').append("payAmount").append('=')
					.append(payAmount);
		}
		if (StringUtils.isNotBlank(fee)) {
			result.append('&').append("fee").append('=').append(fee);
		}
		if (StringUtils.isNotBlank(ext1)) {
			result.append('&').append("ext1").append('=').append(ext1);
		}
		if (StringUtils.isNotBlank(ext2)) {
			result.append('&').append("ext2").append('=').append(ext2);
		}
		if (StringUtils.isNotBlank(payResult)) {
			result.append('&').append("payResult").append('=')
					.append(payResult);
		}
		if (StringUtils.isNotBlank(errCode)) {
			result.append('&').append("errCode").append('=').append(errCode);
		}

		return result.toString();
	}

}
