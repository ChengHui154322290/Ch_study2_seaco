package com.tp.service.pay;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.dto.pay.AppPayData;
import com.tp.dto.pay.appdata.UnionAppPayData;
import com.tp.dto.pay.postdata.PayPostData;
import com.tp.dto.pay.postdata.UnionPayPostData;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.pay.RefundPayinfo;
import com.tp.result.pay.RefundResult;
import com.tp.result.pay.TradeStatusResult;
import com.tp.service.pay.unionpay.sdk.SDKConfig;
import com.tp.service.pay.unionpay.sdk.SDKUtil;
import com.tp.util.DateUtil;


//@Service
public class UnionAppPayPlatformService extends UnionPayPlatformService {
	private Logger log = LoggerFactory.getLogger(UnionAppPayPlatformService.class);

	public static void main(String[] args) {
		UnionAppPayPlatformService up = new UnionAppPayPlatformService();
		up.paymentConfig=new Properties();
		FileInputStream fin = null;
		try {
			fin = new FileInputStream("D:/develop/codes/payment/payment-service/payment-web/src/main/resources/config/payment.properties");
			up.paymentConfig.load(fin);
			System.out.println(new File("D:/develop/codes/payment/payment-service/payment-web/src/main/resources/acp_sdk.properties").exists());
			SDKConfig.getConfig().loadPropertiesFromPath("D:/develop/codes/payment/payment-service/payment-web/src/main/resources/");
			up.init();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (fin!=null) {
				try {
					fin.close();
				} catch (IOException e) {
				}
			}
		}
		
		PaymentInfo paymentInfoDO = new PaymentInfo();
		paymentInfoDO.setBizCode(1015030800000022L);
		paymentInfoDO.setAmount(1.02);
//		paymentInfoDO.setCreateTime(DateUtil.parse("20150308120253", "yyyyMMddHHmmss"));
		paymentInfoDO.setCreateTime(new Date());
//		paymentInfoDO.setGatewayTradeNo("201501081614462844888");
		
//		paymentInfoDO.setBizCode("20150109001");
//		paymentInfoDO.setCreateTime(DateUtil.parse("20150109150701", "yyyyMMddHHmmss"));
//		paymentInfoDO.setGatewayTradeNo("201501071608052848608");
		
		 AppPayData postData = up.constructAppPostData(paymentInfoDO, true);
		 System.out.println(postData);
		
//		TradeStatusResult queryPayStatus = up.queryPayStatus(paymentInfoDO);
//		System.out.println(queryPayStatus);
		
//		/**
//		 * 组装请求报文
//		 */
//		Map<String, String> data = new HashMap<String, String>();
//		// 版本号
//		data.put("version", "5.0.0");
//		// 字符集编码 默认"UTF-8"
//		data.put("encoding", "UTF-8");
//		// 签名方法 01 RSA
//		data.put("signMethod", "01");
//		// 交易类型 01-消费
//		data.put("txnType", "01");
//		// 交易子类型 01:自助消费 02:订购 03:分期付款
//		data.put("txnSubType", "01");
//		// 业务类型
//		data.put("bizType", "000201");
//		// 渠道类型，07-PC，08-手机
//		data.put("channelType", "08");
//		// 前台通知地址 ，控件接入方式无作用
//		data.put("frontUrl", "http://localhost:8080/ACPTest/acp_front_url.do");
//		// 后台通知地址
//		data.put("backUrl", "http://222.222.222.222:8080/ACPTest/acp_back_url.do");
//		// 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
//		data.put("accessType", "0");
//		// 商户号码，请改成自己的商户号
//		data.put("merId", "898340183980105");
//		// 商户订单号，8-40位数字字母
//		data.put("orderId", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
//		// 订单发送时间，取系统时间
//		data.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
//		// 交易金额，单位分
//		data.put("txnAmt", "1");
//		// 交易币种
//		data.put("currencyCode", "156");
//		// 请求方保留域，透传字段，查询、通知、对账文件中均会原样出现
//		// data.put("reqReserved", "透传信息");
//		// 订单描述，可不上送，上送时控件中会显示该信息
//		// data.put("orderDesc", "订单描述");
//
//		data = signData(data, true);
//
//		// 交易请求url 从配置文件读取
////		String requestAppUrl = "https://101.231.204.80:5000/gateway/api/appTransReq.do";//SDKConfig.getConfig().getAppRequestUrl();
//		String requestAppUrl = SDKConfig.getConfig().getAppRequestUrl();
//
//		Map<String, String> resmap = up.submitUrl(data, requestAppUrl);
//
//		System.out.println("请求报文=["+data.toString()+"]");
//		System.out.println("应答报文=["+resmap.toString()+"]");
		
	}

	@PostConstruct
	public void init() {
		super.init();
		SDKConfig.getConfig().loadPropertiesFromSrc();
	}
	@PreDestroy
	public void destroy(){
		super.destroy();
	}
	
	
	@Override
	protected PayPostData constructPostData(PaymentInfo paymentInfo) {
//		throw new RuntimeException("手机端不支持pc支付");
		AppPayData data = constructAppPostData(paymentInfo, false);
		if (data instanceof UnionPayPostData) {
			return (PayPostData) data;
		} else {
			throw new RuntimeException("手机端不支持在pc使用SDK支付");
		}
	}
	
	@Override
	protected AppPayData constructAppPostData(PaymentInfo paymentInfo, boolean forSdk) {
		
		if (forSdk) {
			/**
			 * 组装请求报文
			 */
			Map<String, String> submitFromData = new HashMap<String, String>();
			// 版本号
			submitFromData.put("version", paymentConfig.getProperty("unionPay.version"));
			// 字符集编码 默认"UTF-8"
			submitFromData.put("encoding", paymentConfig.getProperty("unionPay.encoding"));
			// 签名方法 01 RSA
			submitFromData.put("signMethod", paymentConfig.getProperty("unionPay.signMethod"));
			// 交易类型 01-消费
			submitFromData.put("txnType", paymentConfig.getProperty("unionPay.txnType"));
			// 交易子类型 01:自助消费 02:订购 03:分期付款
			submitFromData.put("txnSubType", paymentConfig.getProperty("unionPay.txnSubType"));
			// 业务类型
//			data.put("bizType", "000201");
			submitFromData.put("bizType", paymentConfig.getProperty("unionPay.bizType"));
			// 渠道类型，07-PC，08-手机
			submitFromData.put("channelType", paymentConfig.getProperty("unionAppPay.channelType"));
			// 前台通知地址 ，控件接入方式无作用
//			data.put("frontUrl", "http://localhost:8080/ACPTest/acp_front_url.do");
			// 后台通知地址
			submitFromData.put("backUrl", paymentConfig.getProperty("unionPay.backUrl"));
			// 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
			submitFromData.put("accessType", paymentConfig.getProperty("unionPay.accessType"));
			// 商户号码，请改成自己的商户号
			submitFromData.put("merId", paymentConfig.getProperty("unionAppPay.merId"));
			// 商户订单号，8-40位数字字母
			String bizCode = String.valueOf(paymentInfo.getBizCode());
			submitFromData.put("orderId", bizCode);
			// 订单发送时间，取系统时间
			submitFromData.put("txnTime", DateUtil.format(paymentInfo.getCreateTime(), "yyyyMMddHHmmss"));
			// 交易金额，单位分
			submitFromData.put("txnAmt", String.valueOf(Math.round(paymentInfo.getAmount()*100)));
			// 交易币种
			submitFromData.put("currencyCode", paymentConfig.getProperty("unionPay.currencyCode"));
			// 请求方保留域，透传字段，查询、通知、对账文件中均会原样出现
			// data.put("reqReserved", "透传信息");
			// 订单描述，可不上送，上送时控件中会显示该信息
			// data.put("orderDesc", "订单描述");

			for (Iterator<Entry<String, String>> it = submitFromData.entrySet().iterator(); it.hasNext();) {
				Entry<String, String> entry = it.next();
				String value = entry.getValue();
				if (StringUtils.isBlank(value)) {
					// 对value值进行去除前后空处理
//					submitFromData.put(obj.getKey(), value.trim());
					it.remove();
					System.out.println(entry.getKey() + " remove: -->" + String.valueOf(value));
				}
			}
			
			
			SDKUtil.sign(submitFromData, "UTF-8", true);

			// 交易请求url 从配置文件读取
			String appRequestUrl = SDKConfig.getConfig().getAppRequestUrl();
			log.info("request to {}:\n params:{}",  appRequestUrl, submitFromData);
			Map<String, String> resmap = submitUrl(submitFromData, appRequestUrl);
			log.info("request result:{}",  resmap);

			String respCode = resmap.get("respCode");
			UnionAppPayData appPayData = new UnionAppPayData();
			if ("00".equals(respCode)) {
				appPayData.setTn(resmap.get("tn"));
				appPayData.setBizCode(bizCode);
			} else {
				log.error("请求手机ＳＤＫ支付出错：bizCode={},  result={}", bizCode, resmap);
			}
			
			return appPayData;
		}
		
		UnionPayPostData unionpayPostData = new UnionPayPostData(paymentConfig, paymentInfo);
		String frontRequestUrl = SDKConfig.getConfig().getFrontRequestUrl();
		unionpayPostData.setActionUrl(frontRequestUrl);
		unionpayPostData.setFrontUrl(paymentConfig.getProperty("unionAppPay.mobile.frontUrl")+paymentInfo.getPaymentId());
		unionpayPostData.setChannelType(paymentConfig.getProperty("unionAppPay.channelType"));
		unionpayPostData.setMerId(paymentConfig.getProperty("unionAppPay.merId"));
		Map<String, String> map = unionpayPostData.toMap();
		/**
		 * 签名
		 */
		SDKUtil.sign(map, "UTF-8", true);
		unionpayPostData.updateData(map);
		return unionpayPostData;
	}
	
	@Override
	protected RefundResult doRefund(RefundPayinfo refundPayinfoObj) {
		return doRefund(refundPayinfoObj, true);
	}
	
	
	@Override
	public TradeStatusResult queryPayStatus(PaymentInfo paymentInfoDO) {
		return queryStatus(paymentInfoDO.getBizCode(), paymentInfoDO.getCreateTime(), paymentInfoDO.getGatewayTradeNo(), true);
	}

	@Override
	public TradeStatusResult queryRefundStatus(RefundPayinfo refundPayinfoObj) {
		PaymentInfo paymentInfoDO = paymentInfoService.queryById(refundPayinfoObj.getPaymentId());
		return queryStatus(refundPayinfoObj.getBizCode(), refundPayinfoObj.getCreateTime(), paymentInfoDO.getGatewayTradeNo(), true);
	}

}
