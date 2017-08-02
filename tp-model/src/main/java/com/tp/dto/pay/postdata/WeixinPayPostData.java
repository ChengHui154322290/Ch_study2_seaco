package com.tp.dto.pay.postdata;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.tp.common.vo.OrderConstant;
import com.tp.dto.pay.AppPayData;
import com.tp.model.pay.PaymentInfo;
import com.tp.util.DateUtil;

public class WeixinPayPostData implements PayPostData, Serializable, AppPayData {
	private static final long serialVersionUID = 758569019252697640L;
	
	private String appid;//公众账号ID，微信分配的公众账号ID,如：wx8888888888888888
	private String mch_id;//商户号，微信支付分配的商户号,如：1900000109
	private String device_info;//设备号，微信支付分配的终端设备号，商户自定义,如：13467007045764
	private String nonce_str;//随机字符串，随机字符串，不长于32位。推荐生成算法详见第4.3.2节,如：5K8264ILTKCH16CQ2502SI8ZNMTM67VS
	private String sign;//签名，签名，详见4.3.1节签名生成算法,如：C380BEC2BFD727A4B6845133519F3AD6
	private String androidSign;
	private String iosSign;
	private String body;//商品描述，商品或支付单简要描述,如：Ipad mini  16G  白色
	private String detail;//商品详情，商品名称明细列表,如：Ipad mini  16G  白色
	private String attach;//附加数据，附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据,如：说明
	private String out_trade_no;//商户订单号，商户系统内部的订单号,32个字符内、可包含字母, 其他说明见第4.2节商户订单号,如：1.2177525012014E+27
	private String fee_type;//货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见第4.2节货币类型,如：CNY
	private String total_fee;//总金额，订单总金额，只能为整数，详见第4.2节支付金额,如：888
	private String spbill_create_ip;//终端IP，APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。,如：8.8.8.8
	private String time_start;//交易起始时间，订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见第4.2节时间规则,如：20091225091010
	private String time_expire;//交易结束时间，订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。其他详见第4.2节时间规则,如：20091227091010
	private String goods_tag;//商品标记，商品标记，代金券或立减优惠功能的参数，说明详见第10节代金券或立减优惠,如：WXG
	private String notify_url;//通知地址，接收微信支付异步通知回调地址,如：http://www.baidu.com/
	private String trade_type;//交易类型，取值如下：JSAPI，NATIVE，APP，详细说明见第4.2节参数规定,如：JSAPI
	private String product_id;//商品ID，trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。,如：1.22354132140703E+22
	private String openid;//用户标识，trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid。,如：oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
	
	private String key;
	private String actionUrl;
	
	private String returnCode;
	private String returnMsg;
	private String resultCode;
	private String prepayId;
	private String codeUrl;
	
	private String imgData;
	private String returnUrl;

	
	/**
	 * 
	 */
	private WeixinPayPostData(Properties paymentConfig) {
		appid = paymentConfig.getProperty("weixinPay.appid");//公众账号ID，微信分配的公众账号ID,如：wx8888888888888888
		mch_id = paymentConfig.getProperty("weixinPay.mch_id");//商户号，微信支付分配的商户号,如：1900000109
		device_info = paymentConfig.getProperty("weixinPay.device_info");//设备号，微信支付分配的终端设备号，商户自定义,如：13467007045764
		nonce_str = String.valueOf(Math.random());//随机字符串，随机字符串，不长于32位。推荐生成算法详见第4.3.2节,如：5K8264ILTKCH16CQ2502SI8ZNMTM67VS
		body = paymentConfig.getProperty("weixinPay.body");//商品描述，商品或支付单简要描述,如：Ipad mini  16G  白色
		detail = paymentConfig.getProperty("weixinPay.detail");//商品详情，商品名称明细列表,如：Ipad mini  16G  白色
		attach = paymentConfig.getProperty("weixinPay.attach");//附加数据，附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据,如：说明
		fee_type = paymentConfig.getProperty("weixinPay.fee_type");//货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见第4.2节货币类型,如：CNY
//		spbill_create_ip = paymentConfig.getProperty("weixinPay.spbill_create_ip");//终端IP，APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。,如：8.8.8.8
		time_start = DateUtil.format(new Date(), "yyyyMMddHHmmss");//paymentConfig.getProperty("weixinPay.appid");//交易起始时间，订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见第4.2节时间规则,如：20091225091010
		time_expire = DateUtil.format(new Date(System.currentTimeMillis()+3600_000), "yyyyMMddHHmmss");//交易结束时间，订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。其他详见第4.2节时间规则,如：20091227091010
//		goods_tag = paymentConfig.getProperty("weixinPay.appid");//商品标记，商品标记，代金券或立减优惠功能的参数，说明详见第10节代金券或立减优惠,如：WXG
		notify_url = paymentConfig.getProperty("weixinPay.notify_url");//通知地址，接收微信支付异步通知回调地址,如：http://www.baidu.com/
		trade_type = paymentConfig.getProperty("weixinPay.trade_type");//交易类型，取值如下：JSAPI，NATIVE，APP，详细说明见第4.2节参数规定,如：JSAPI
//		openid = paymentConfig.getProperty("weixinPay.appid");//用户标识，trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid。,如：oUpF8uMuAJO_M2pxb1Q9zNjWeS6o

		key = paymentConfig.getProperty("weixinPay.key");
		actionUrl = paymentConfig.getProperty("weixinPay.actionUrl");
		
	}
	/**
	 * 包装支付的数据.
	 * 
	 * @param ordOrder
	 * @param ordPayment
	 */
	public WeixinPayPostData(Properties paymentConfig, PaymentInfo paymentInfo,boolean forSDK) {
		this(paymentConfig);
		if(paymentInfo.getOrderType()!=null && OrderConstant.FAST_ORDER_TYPE.equals(paymentInfo.getOrderType().intValue())){
			this.notify_url = this.notify_url.replaceFirst("(.*://)?[^/]*/", paymentConfig.getProperty("fastPay.notify_url"));
		}
		if(forSDK){
			appid = paymentConfig.getProperty("weixinPay.appAppid");//公众账号ID，微信分配的公众账号ID,如：wx8888888888888888
			mch_id = paymentConfig.getProperty("weixinPay.appMch_id");//商户号，微信支付分配的商户号,如：1900000109
			trade_type = paymentConfig.getProperty("weixinPay.appTrade_type");
			key = paymentConfig.getProperty("weixinPay.appKey");
		}
//		String sign = paymentConfig.getProperty("weixinPay.appid");//签名，签名，详见4.3.1节签名生成算法,如：C380BEC2BFD727A4B6845133519F3AD6
		out_trade_no = paymentInfo.getPaymentTradeNo();//商户订单号，商户系统内部的订单号,32个字符内、可包含字母, 其他说明见第4.2节商户订单号,如：1.2177525012014E+27
		total_fee = String.valueOf(Math.round(paymentInfo.getAmount()*100));//总金额，订单总金额，只能为整数，详见第4.2节支付金额,如：888
		product_id = String.valueOf(paymentInfo.getPaymentId());//商品ID，trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。,如：1.22354132140703E+22
		
		spbill_create_ip = paymentInfo.getActionIp();
	}
	/**
	 * 包装支付的数据.
	 *
	 * @param ordOrder
	 * @param ordPayment
	 */
	public WeixinPayPostData(Properties paymentConfig, PaymentInfo paymentInfo,boolean forSDK,boolean forExternal) {
		this(paymentConfig);
		this.key = paymentConfig.getProperty("weixinExternalPay.key");
		this.mch_id = paymentConfig.getProperty("weixinExternalPay.mch_id");
		this.notify_url = paymentConfig.getProperty("weixinExternalPay.notify_url");//通知地址，接收微信支付异步通知回调地址,如：http://www.baidu.com/
		if(paymentInfo.getOrderType()!=null && OrderConstant.FAST_ORDER_TYPE.equals(paymentInfo.getOrderType())){
			this.notify_url = this.notify_url.replaceFirst("(.*://)?[^/]*/", paymentConfig.getProperty("fastPay.notify_url"));
		}
		if(forSDK){
			appid = paymentConfig.getProperty("weixinPay.appAppid");//公众账号ID，微信分配的公众账号ID,如：wx8888888888888888
			trade_type = paymentConfig.getProperty("weixinPay.appTrade_type");
		}
//		String sign = paymentConfig.getProperty("weixinPay.appid");//签名，签名，详见4.3.1节签名生成算法,如：C380BEC2BFD727A4B6845133519F3AD6
		out_trade_no = paymentInfo.getPaymentTradeNo();//商户订单号，商户系统内部的订单号,32个字符内、可包含字母, 其他说明见第4.2节商户订单号,如：1.2177525012014E+27
		total_fee = String.valueOf(Math.round(paymentInfo.getAmount()*100));//总金额，订单总金额，只能为整数，详见第4.2节支付金额,如：888
		product_id = String.valueOf(paymentInfo.getPaymentId());//商品ID，trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。,如：1.22354132140703E+22

		spbill_create_ip = paymentInfo.getActionIp();
	}


	
	public Map<String, String> toMap() {
		Map<String, String> map = new TreeMap<String, String>();
		map.put("appid", appid);

		if (!StringUtils.isBlank(mch_id)) {
			map.put("mch_id", mch_id);
		}
		if (!StringUtils.isBlank(device_info)) {
			map.put("device_info", device_info);
		}
		if (!StringUtils.isBlank(nonce_str)) {
			map.put("nonce_str", nonce_str);
		}
		if (!StringUtils.isBlank(sign)) {
			map.put("sign", sign);
		}
		if (!StringUtils.isBlank(body)) {
			map.put("body", body);
		}
		if (!StringUtils.isBlank(detail)) {
			map.put("detail", detail);
		}
		if (!StringUtils.isBlank(attach)) {
			map.put("attach", attach);
		}
		if (!StringUtils.isBlank(out_trade_no)) {
			map.put("out_trade_no", out_trade_no);
		}
		if (!StringUtils.isBlank(fee_type)) {
			map.put("fee_type", fee_type);
		}
		if (!StringUtils.isBlank(total_fee)) {
			map.put("total_fee", total_fee);
		}
		if (!StringUtils.isBlank(spbill_create_ip)) {
			map.put("spbill_create_ip", spbill_create_ip);
		}
		if (!StringUtils.isBlank(time_start)) {
			map.put("time_start", time_start);
		}
		if (!StringUtils.isBlank(time_expire)) {
			map.put("time_expire", time_expire);
		}
		if (!StringUtils.isBlank(goods_tag)) {
			map.put("goods_tag", goods_tag);
		}
		if (!StringUtils.isBlank(notify_url)) {
			map.put("notify_url", notify_url);
		}
		if (!StringUtils.isBlank(trade_type)) {
			map.put("trade_type", trade_type);
		}
		if (!StringUtils.isBlank(product_id)) {
			map.put("product_id", product_id);
		}
		if (!StringUtils.isBlank(openid)) {
			map.put("openid", openid);
		}

		return map;
	}
	
	
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getPrepayId() {
		return prepayId;
	}
	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}
	public String getCodeUrl() {
		return codeUrl;
	}
	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
	}
	public String getImgData() {
		return imgData;
	}
	public void setImgData(String imgData) {
		this.imgData = imgData;
	}
	public String getActionUrl() {
		return actionUrl;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
	public String getDevice_info() {
		return device_info;
	}
	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getFee_type() {
		return fee_type;
	}
	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}
	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}
	public String getTime_start() {
		return time_start;
	}
	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}
	public String getTime_expire() {
		return time_expire;
	}
	public void setTime_expire(String time_expire) {
		this.time_expire = time_expire;
	}
	public String getGoods_tag() {
		return goods_tag;
	}
	public void setGoods_tag(String goods_tag) {
		this.goods_tag = goods_tag;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	@Override
	public String getPaymentTradeNo() {
		return getOut_trade_no();
	}
	@Override
	public String getSignature() {
		return sign;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public String getAndroidSign() {
		return androidSign;
	}
	public void setAndroidSign(String androidSign) {
		this.androidSign = androidSign;
	}
	public String getIosSign() {
		return iosSign;
	}
	public void setIosSign(String iosSign) {
		this.iosSign = iosSign;
	}
}
