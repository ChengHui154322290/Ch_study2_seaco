package com.tp.m.vo.pay;



/**
 * WAP的微信支付（微信公众号内支付）
 * @author zhuss
 * @2016年1月8日 上午11:14:06
 */
public class WAPWXPayVO extends BasePayVO{
	
	private static final long serialVersionUID = 408125218311841203L;
	private String appId;//公众号名称
	private String timeStamp; //支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
	private String nonceStr; // 支付签名随机串，不长于 32 位
	private String _package;// 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
	private String signType; //签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
	private String paySign;//随机串
	private String returnurl;//支付成功后的回调地址
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String get_package() {
		return _package;
	}

	public void set_package(String _package) {
		this._package = _package;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getPaySign() {
		return paySign;
	}

	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}

	public String getReturnurl() {
		return returnurl;
	}

	public void setReturnurl(String returnurl) {
		this.returnurl = returnurl;
	}
}
