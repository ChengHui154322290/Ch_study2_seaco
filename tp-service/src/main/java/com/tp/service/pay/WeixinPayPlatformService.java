package com.tp.service.pay;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.net.ssl.SSLContext;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.PaymentConstant;
import com.tp.dto.pay.AppPayData;
import com.tp.dto.pay.cbdata.PayCallbackData;
import com.tp.dto.pay.cbdata.RefundCallbackData;
import com.tp.dto.pay.postdata.PayPostData;
import com.tp.dto.pay.postdata.WeixinPayPostData;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.pay.RefundPayinfo;
import com.tp.result.pay.RefundResult;
import com.tp.result.pay.TradeStatusResult;
import com.tp.service.pay.cbdata.WeixinPayCallbackData;
import com.tp.service.pay.cbdata.WeixinRefundResult;
import com.tp.service.pay.util.ErWeiMaUtil;
import com.tp.util.DateUtil;

import sun.misc.BASE64Encoder;

@Service("weixinPayPlatformService")
public class WeixinPayPlatformService extends AbstractPayPlatformService {
	private Logger log = LoggerFactory.getLogger(WeixinPayPlatformService.class);
	@Autowired
	Properties settings;
	
	
	
	CloseableHttpClient httpClientWithSSL = null;
	CloseableHttpClient appHttpClientWithSSL = null;
	@PostConstruct
	public void init() {
		super.init();
		try {
			String sslPassword = settings.getProperty("weixinPay.sslPassword");
			String sslFilePath = settings.getProperty("weixinPay.sslFilePath");
			String appSslPassword = settings.getProperty("weixinPay.appSslPassword");
			String appSslFilePath = settings.getProperty("weixinPay.appSslFilePath"); 
			KeyStore keyStore  = KeyStore.getInstance("PKCS12");
			KeyStore appKeyStore = KeyStore.getInstance("PKCS12");
			FileInputStream instream = new FileInputStream(new File(sslFilePath));
			File appSslFile = new File(appSslFilePath);
			if(!appSslFile.exists()){
				log.error("\r\n===============\r\n\t\t{}\t文件不存在,初始化证书失败\r\n===============",appSslFilePath);
				return;
			}
			FileInputStream appInstream = new FileInputStream(appSslFile);
			try {
			    keyStore.load(instream, sslPassword.toCharArray());
			    appKeyStore.load(appInstream, appSslPassword.toCharArray());
			} finally {
			    try {
			    	if (appInstream!=null) {
			    		appInstream.close();
					}
				} catch (Exception e) {
				}
			}

			// Trust own CA and all self-signed certs
			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, sslPassword.toCharArray()).build();
	        SSLContext appSslcontext = SSLContexts.custom().loadKeyMaterial(appKeyStore, appSslPassword.toCharArray()).build();
	        // Allow TLSv1 protocol only
	        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
	        SSLConnectionSocketFactory appSslsf = new SSLConnectionSocketFactory(appSslcontext, new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
	        httpClientWithSSL = HttpClients.custom().setSSLSocketFactory(sslsf).setRetryHandler(httpRequestRetryHandler).build();
	        appHttpClientWithSSL = HttpClients.custom().setSSLSocketFactory(appSslsf).setRetryHandler(httpRequestRetryHandler).build();
		} catch (Exception e) {
			log.error("初始化证书失败", e);
		}
	}
	
	@PreDestroy
	public void destroy(){
		super.destroy();
		try {
			if (httpClientWithSSL!=null) {
				httpClientWithSSL.close();
			}
			if (appHttpClientWithSSL!=null) {
				appHttpClientWithSSL.close();
			}
		} catch (Exception e) {
		}
	}
	private PayPostData constructPostData(PaymentInfo paymentInfo, boolean forSDK){
		WeixinPayPostData weixinPayPostData = new WeixinPayPostData(settings, paymentInfo,forSDK);
//		if(PaymentConstant.BIZ_TYPE.DSS.code.equals(paymentInfo.getBizType())) {
//			paymentInfo.setSerial(String.valueOf(Long.valueOf(paymentInfo.getSerial())+1));
//			paymentInfoDao.updateNotNullById(paymentInfo);
//			weixinPayPostData.setOut_trade_no(String.valueOf(paymentInfo.getSerial()));
//		}
		String payInfo = paymentInfo.getPayInfo();
		
		if (StringUtils.isBlank(payInfo)) {
			Map<String, String> map = weixinPayPostData.toMap();
			String sign = createSignature(map, forSDK);
			weixinPayPostData.setSign(sign);
			
			map.put("sign", sign);
			
			//提交到微信支付，取得prepay_id、code_url
			Map<String, String> result = submitUrl(map, settings.getProperty("weixinPay.actionUrl"), false, false);
			log.info("prepay result={}", result);
			
			weixinPayPostData.setReturnCode(result.get("return_code"));
			weixinPayPostData.setReturnMsg(result.get("return_msg"));
			String returnCode = weixinPayPostData.getReturnCode();
			String prepayId = result.get("prepay_id");
			weixinPayPostData.setPrepayId(prepayId);
			if ("SUCCESS".equals(returnCode) && !forSDK) {
				weixinPayPostData.setResultCode(result.get("result_code"));
				if ("SUCCESS".equals(weixinPayPostData.getResultCode())) {
					String codeUrl = result.get("code_url");
					weixinPayPostData.setCodeUrl(codeUrl);
					//保存起来以再次使用
					Map<String, Object> infos = new HashMap<String, Object>();
					infos.put("id", prepayId);
					infos.put("url", codeUrl);
					Gson gson = new Gson();
					paymentInfo.setPayInfo(gson.toJson(infos));
					
					ByteArrayOutputStream output = new ByteArrayOutputStream();
					ErWeiMaUtil.encoderQRCode(codeUrl, output);
					BASE64Encoder encoder = new BASE64Encoder();
					String imgData = encoder.encode(output.toByteArray());
					weixinPayPostData.setImgData(imgData);
				} else {
					log.error("payment-{}提交后得不到成功的结果：{}", paymentInfo.getPaymentId(), result);
				}
			} else {
				log.error("payment-{}提交不成功：{}", paymentInfo.getPaymentId(), result);
			}
		} else {
			log.info("原来已经有paymentInfo:{}", payInfo);
			Gson gson = new Gson();
			Map<String, Object> infos = gson.fromJson(payInfo, HashMap.class);
			Object prepayId = infos.get("id");
			Object codeUrl = infos.get("url");
			if (prepayId!=null&&codeUrl!=null) {
				weixinPayPostData.setReturnCode("SUCCESS");
				weixinPayPostData.setReturnMsg("OK");
				weixinPayPostData.setResultCode("SUCCESS");
				
				weixinPayPostData.setPrepayId(prepayId.toString());
				weixinPayPostData.setCodeUrl(codeUrl.toString());
				
				//通过base64编码的方式返回图片信息
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				ErWeiMaUtil.encoderQRCode(codeUrl.toString(), output);
				BASE64Encoder encoder = new BASE64Encoder();
				String imgData = encoder.encode(output.toByteArray());
				weixinPayPostData.setImgData(imgData);
			} else {
				weixinPayPostData.setReturnCode("FAIL");
				weixinPayPostData.setReturnMsg("错误的支付信息："+payInfo);
				weixinPayPostData.setResultCode("FAIL");
			}
		}
		
		return weixinPayPostData;
	
		
	}
	@Override
	protected PayPostData constructPostData(PaymentInfo paymentInfo) {
		return constructPostData(paymentInfo,false);
	}

	@Override
	protected AppPayData constructAppPostData(PaymentInfo paymentInfo, boolean forSdk) {
		WeixinPayPostData postData = (WeixinPayPostData) constructPostData(paymentInfo,true);
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.MINUTE, -30);
//		if(PaymentConstant.BIZ_TYPE.DSS.code.equals(paymentInfo.getBizType())) {
//			if(paymentInfo.getUpdateTime().before(c.getTime())) {
//				paymentInfo.setSerial(String.valueOf(Long.valueOf(paymentInfo.getSerial())+1));
//				paymentInfo.setUpdateTime(new Date());
//				paymentInfoDao.updateNotNullById(paymentInfo);
//			}
//			postData.setOut_trade_no(String.valueOf(paymentInfo.getSerial()));
//		}
		Map<String, String> map = postData.toMap();
		StringBuilder signStr = new StringBuilder();
		
		String key = settings.getProperty("weixinPay.key");
		String appId = settings.getProperty("weixinPay.appid");
		String mchId = settings.getProperty("weixinPay.mch_id");
		if(forSdk){
			key = settings.getProperty("weixinPay.appKey");
			appId = settings.getProperty("weixinPay.appAppid");
			mchId = settings.getProperty("weixinPay.appMch_id");
		}
		
		signStr.append("appid=").append(appId);
		signStr.append("&noncestr=").append(postData.getNonce_str());
		signStr.append("&package=Sign=WXPay");
		signStr.append("&partnerid=").append(mchId);
		signStr.append("&prepayid=").append(postData.getPrepayId());
		signStr.append("&timestamp=").append(DateUtil.parse(postData.getTime_start(), DateUtil.LONG_FORMAT).getTime()/1000);
		signStr.append("&key=").append(key);
		String sign = DigestUtils.md5Hex(signStr.toString()).toUpperCase();
		postData.setSign(sign);
		postData.setIosSign(DigestUtils.md5Hex(signStr.toString()).toUpperCase());
		postData.setAndroidSign(DigestUtils.md5Hex(signStr.toString().replace("package=Sign=WXPay", "package=prepay_id="+postData.getPrepayId())).toUpperCase());
		return postData;
	}
	
	/**
	 * JSAPI支付
	 */
	@Override
	protected AppPayData constructAppPostDataByParams(PaymentInfo paymentInfo, boolean forSdk, Map<String,Object> params) {
		WeixinPayPostData weixinPayPostData = new WeixinPayPostData(settings, paymentInfo,forSdk);
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.MINUTE, -30);
//		if(PaymentConstant.BIZ_TYPE.DSS.code.equals(paymentInfo.getBizType())) {
//			if(paymentInfo.getUpdateTime().before(c.getTime())) {
//				paymentInfo.setSerial(String.valueOf(Long.valueOf(paymentInfo.getSerial())+1));
//				paymentInfo.setUpdateTime(new Date());
//				paymentInfoDao.updateNotNullById(paymentInfo);
//			}
//			weixinPayPostData.setOut_trade_no(String.valueOf(paymentInfo.getSerial()));
//		}
		if(!forSdk){
			String trade_type = settings.getProperty("weixinPay.Jsapi_Trade_type");
			weixinPayPostData.setTrade_type(trade_type);
			weixinPayPostData.setOpenid(params.get("openid").toString());
			weixinPayPostData.setReturnUrl(settings.getProperty("weixinPay.mobile.frontUrl") + paymentInfo.getPaymentId());
			if(PaymentConstant.BIZ_TYPE.DSS.code.equals(paymentInfo.getBizType())) {
				weixinPayPostData.setReturnUrl(settings.getProperty("PAYMENT_DSS_WAP"));
			}
			if(paymentInfo.getOrderType()!=null && OrderConstant.FAST_ORDER_TYPE.equals(paymentInfo.getOrderType().intValue())){
				weixinPayPostData.setReturnUrl(weixinPayPostData.getReturnUrl().replaceFirst("(.*://)?[^/]*/", settings.getProperty("fastPay.notify_url")));
			}
			if (params != null && params.get("channelCode") != null) { //第三方商城支付
			//	weixinPayPostData.setReturnUrl(settings.getProperty("weixinPay.eshop.returnurl").replace("SHOPCODE", (String)params.get("channelCode")) + paymentInfo.getPaymentId());
//				weixinPayPostData.getNotify_url().replaceFirst("(.*://)?[^/]*/", settings.getProperty("shopPay.notify_url").replace("SHOPCODE", (String)params.get("channelCode")));
//				weixinPayPostData.setNotify_url(weixinPayPostData.getNotify_url().replaceFirst("(.*://)?[^/]*/", "http://shop.51seaco.com/"));
			}
			if(params !=null && (org.apache.commons.lang3.StringUtils.equals(String.valueOf(params.get("sysSource")),"world") || "w".equals(params.get("channelCode")) || "world".equals(params.get("channelCode")) )){
				weixinPayPostData.setReturnUrl( settings.getProperty("weixinPay.world.frontUrl") + paymentInfo.getPaymentId());
			}
		}

		String payInfo = paymentInfo.getPayInfo();
		if (StringUtils.isBlank(payInfo)) {
			Map<String, String> map = weixinPayPostData.toMap();
			String sign = createSignature(map, forSdk);
			
			map.put("sign", sign);
			
			//提交到微信支付，取得prepay_id
			System.out.println(map);
			Map<String, String> result = submitUrl(map, settings.getProperty("weixinPay.actionUrl"), false, false);
			log.info("prepay result={}", result);
			
			weixinPayPostData.setReturnCode(result.get("return_code"));
			weixinPayPostData.setReturnMsg(result.get("return_msg"));
			String returnCode = weixinPayPostData.getReturnCode();
			String prepayId = result.get("prepay_id");
			weixinPayPostData.setPrepayId(prepayId);
			if ("SUCCESS".equals(returnCode) && !forSdk) {
				weixinPayPostData.setResultCode(result.get("result_code"));
				if ("SUCCESS".equals(weixinPayPostData.getResultCode())) {
					//保存起来以再次使用
					Map<String, Object> infos = new HashMap<String, Object>();
					infos.put("id", prepayId);
					Gson gson = new Gson();
					paymentInfo.setPayInfo(gson.toJson(infos));
					
					StringBuilder signStr = new StringBuilder();
					signStr.append("appId=").append(weixinPayPostData.getAppid());
					signStr.append("&nonceStr=").append(weixinPayPostData.getNonce_str());
					signStr.append("&package=").append("prepay_id=").append(prepayId);
					signStr.append("&signType=MD5");
					signStr.append("&timeStamp=").append(DateUtil.parse(weixinPayPostData.getTime_start(), DateUtil.LONG_FORMAT).getTime()/1000);
					signStr.append("&key=").append(weixinPayPostData.getKey());
					
					weixinPayPostData.setSign(DigestUtils.md5Hex(signStr.toString()).toUpperCase());
				} else {
					log.error("payment-{}提交后得不到成功的结果：{}", paymentInfo.getPaymentId(), result);
				}
			} else {
				log.error("payment-{}提交不成功：{}", paymentInfo.getPaymentId(), result);
			}
		} else {
			log.info("原来已经有paymentInfo:{}", payInfo);
			Gson gson = new Gson();
			Map<String, Object> infos = gson.fromJson(payInfo, HashMap.class);
			Object prepayId = infos.get("id");
			Object codeUrl = infos.get("url");
			if (prepayId!=null&&codeUrl!=null) {
				weixinPayPostData.setReturnCode("SUCCESS");
				weixinPayPostData.setReturnMsg("OK");
				weixinPayPostData.setResultCode("SUCCESS");
				
				weixinPayPostData.setPrepayId(prepayId.toString());
				weixinPayPostData.setCodeUrl(codeUrl.toString());
				
				//通过base64编码的方式返回图片信息
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				ErWeiMaUtil.encoderQRCode(codeUrl.toString(), output);
				BASE64Encoder encoder = new BASE64Encoder();
				String imgData = encoder.encode(output.toByteArray());
				weixinPayPostData.setImgData(imgData);
			} else {
				weixinPayPostData.setReturnCode("FAIL");
				weixinPayPostData.setReturnMsg("错误的支付信息："+payInfo);
				weixinPayPostData.setResultCode("FAIL");
			}
		}
		
		return weixinPayPostData;
	}
	
	private String createSignature(Map<String, String> map,boolean ... forSDK) {
		StringBuilder signStr = new StringBuilder();
		Set<Entry<String, String>> entrySet = map.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String key = entry.getKey();
			if ("sign".equals(key)) {
				continue;
			}
			String value = entry.getValue();
			if (StringUtils.isBlank(value)) {
				continue;
			}
			if (signStr.length()>0) {
				signStr.append('&');
			}
			signStr.append(key).append('=').append(value);
		}
		String key = settings.getProperty("weixinPay.key");
		if(forSDK != null && forSDK.length > 0 && forSDK[0]){
			key = settings.getProperty("weixinPay.appKey");
		}
		if("APP".equals(map.get("trade_type"))){
			key = settings.getProperty("weixinPay.appKey");
		}
		signStr.append("&key=").append(key);
		
		String sign = DigestUtils.md5Hex(signStr.toString()).toUpperCase();
		return sign;
	}
	
	@Override
	protected boolean verifyResponse(Map<String, String> parameterMap) {
		String sign = createSignature(parameterMap);
		if (sign!=null&&sign.equals(parameterMap.get("sign"))) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected PayCallbackData getPayCallbackData(Map<String, String> parameterMap) {
		String requestBody = parameterMap.get(PaymentService.REQUEST_CONTENT_NAME);
		if (StringUtils.isNotBlank(requestBody)) {
			Map<String, String> map = convertXml2Map(requestBody);
			if (map!=null) {
				parameterMap.putAll(map);
			}
		}
		
		return new WeixinPayCallbackData(parameterMap);
	}

	@Override
	protected String getReturnMsg(PaymentInfo paymentInfoDO, PayCallbackData callbackData) {
		if (paymentInfoDO!=null) {
			if (PaymentConstant.PAYMENT_STATUS.PAYED.code.equals(paymentInfoDO.getStatus())) {
				return "<xml><return_code><![CDATA[SUCCESS]]></return_code>\n<return_msg><![CDATA[OK]]></return_msg></xml>";
				
			} else {
				return "<xml><return_code><![CDATA[FAIL]]></return_code>\n<return_msg><![CDATA[UNKNOWN]]></return_msg></xml>";
			}			
		} 
		return "<xml><return_code><![CDATA[FAIL]]></return_code>\n<return_msg><![CDATA[UNKNOWN]]></return_msg></xml>";
	}
	
	@Override
	protected RefundResult doRefund(RefundPayinfo refundPayinfoDO) {
		PaymentInfo paymentInfoDO = refundPayinfoDO.getPaymentInfo();
		if (paymentInfoDO==null) {
			paymentInfoDO = paymentInfoDao.queryById(refundPayinfoDO.getPaymentId());
		}
		PaymentInfo parent = getParentPaymentInfo(paymentInfoDO.getPrtPaymentId());
		Double amount = refundPayinfoDO.getAmount();
		
		//数据原因可能会出现退款比支付金额大一分的情况，当退款金额大于支付金额时，以支付总金额来退款
		if(amount > paymentInfoDO.getAmount())
			amount = paymentInfoDO.getAmount();
		
		String refundUrl = settings.getProperty("weixinPay.refundUrl");
		Map<String, String> queryFormData =  new TreeMap<String, String>();
		if(paymentInfoDO.getTradeType() != null && (paymentInfoDO.getTradeType().intValue() == PaymentConstant.TRADE_TYPE.APP.code.intValue())){
			queryFormData.put("appid", settings.getProperty("weixinPay.appAppid"));// 公众账号ID 
			queryFormData.put("mch_id", settings.getProperty("weixinPay.appMch_id"));// 微信支付分配的商户号 
		}
		else{
			queryFormData.put("appid", settings.getProperty("weixinPay.appid"));// 公众账号ID 
			queryFormData.put("mch_id", settings.getProperty("weixinPay.mch_id"));// 微信支付分配的商户号 
		}
		queryFormData.put("device_info", settings.getProperty("weixinPay.device_info"));// 设备号
		queryFormData.put("nonce_str", String.valueOf(Math.random()));// 随机字符串，不长于32位
		
		
		queryFormData.put("transaction_id", paymentInfoDO.getGatewayTradeNo());// 微信订单号
		queryFormData.put("out_trade_no", paymentInfoDO.getPaymentTradeNo());// 商户订单号
		if(parent != null) {
			queryFormData.put("out_trade_no", parent.getPaymentTradeNo());
		}
		queryFormData.put("out_refund_no", refundPayinfoDO.getBizCode().toString());// 商户退款单号
		queryFormData.put("total_fee", String.valueOf(Math.round(paymentInfoDO.getAmount()*100)));// 总金额
		queryFormData.put("refund_fee", String.valueOf(Math.round(amount*100)));// 退款金额
		queryFormData.put("refund_fee_type", settings.getProperty("weixinPay.fee_type"));// 货币种类
		queryFormData.put("op_user_id", settings.getProperty("weixinPay.mch_id"));// 操作员
//		queryFormData.put("op_user_id", "luxiaoming@1228793002");// 操作员
		if(paymentInfoDO.getTradeType() != null && (PaymentConstant.TRADE_TYPE.APP.code.intValue() == paymentInfoDO.getTradeType().intValue())){
			String sign = createSignature(queryFormData,true);
			queryFormData.put("sign", sign);
		}
		else{
			String sign = createSignature(queryFormData,false);
			queryFormData.put("sign", sign);
		}
		Map<String, String> refundResult = null;
		try {
			log.info("refund to {}:\n params:{}",  refundUrl, queryFormData);
			if(paymentInfoDO.getTradeType() != null && paymentInfoDO.getTradeType().intValue() == PaymentConstant.TRADE_TYPE.APP.code.intValue())
				refundResult = submitUrl(queryFormData,refundUrl, true,true);
			else
				refundResult = submitUrl(queryFormData,refundUrl, true,false);
			log.info("refund result:{}",  refundResult);
		} catch (Exception e) {
			log.error("请求微信平台出错", e);
			return new WeixinRefundResult(refundResult);
		}
		log.info("query result={}", refundResult);
		
		String return_code = refundResult.get("return_code");
		String return_msg = refundResult.get("return_msg");
		String result_code = refundResult.get("result_code");
		String err_code = refundResult.get("err_code");
		String err_code_des = refundResult.get("err_code_des");
		
		TradeStatusResult result = new TradeStatusResult();
		result.setTradeNo(paymentInfoDO.getPaymentTradeNo());
		result.setSuccess("SUCCESS".equals(result_code));
		if ("SUCCESS".equals(return_code)) {
			result.setErrorMsg(String.format("%s(%s)", err_code_des, err_code));
		} else {
			result.setErrorMsg(String.format("%s(%s)", return_msg, return_code));
		}
		result.setCanceled(Integer.valueOf(1).equals(paymentInfoDO.getCanceled()));
		
		//微信不回调，自己做回调？ FIXME
		
		return new WeixinRefundResult(refundResult);
	}
	
	@Override
	protected RefundCallbackData getRefundCallbackData(Map<String, String> parameterMap) {
		//微信支付没有退款回调
		return null;
	}


	/**
	 * 数据提交 提交到后台
	 * 
	 * @param submitFromData
	 * @param requestUrl 
	 * @param needSSL 是否需要证书
	 * @return
	 */
	private Map<String, String> submitUrl(Map<String, String> submitFromData,String requestUrl, boolean needSSL, boolean forApp){
        
        Map<String, String> result = new HashMap<String, String>();
        try {

            HttpPost httpPost = new HttpPost(requestUrl);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(8000).setConnectTimeout(8000).setConnectionRequestTimeout(8000).build();//设置请求和传输超时时间
            httpPost.setConfig(requestConfig);
            StringBuilder requestBody = new StringBuilder();
            //构造请求参数  
            requestBody.append("<xml>");
            Set<Entry<String, String>> entrySet = submitFromData.entrySet();
            for (Entry<String, String> entry : entrySet) {
            	String value = entry.getValue();
            	if (StringUtils.isBlank(value)) {
    				continue;
    			}
            	String key = entry.getKey();
				requestBody.append(String.format("<%s>%s</%s>", key, value, key));
			}
            requestBody.append("</xml>");
            log.debug("post request body={}", requestBody.toString());
         
            HttpEntity httpEntity = new StringEntity(requestBody.toString(), "UTF-8");
            httpPost.setEntity(httpEntity);
			
            CloseableHttpResponse response = null;
            if (needSSL) {
            	if(forApp)
            		response = appHttpClientWithSSL.execute(httpPost);
            	else
            		response = httpClientWithSSL.execute(httpPost);
			} else {
				response = httpClient.execute(httpPost);
			}
            
            try {
                HttpEntity entity = response.getEntity();

                StatusLine statusLine = response.getStatusLine();
                if (entity != null) {
                    long contentLength = entity.getContentLength();
					log.debug("Response content length: {}",  contentLength);
                    InputStream inputStream = entity.getContent();
                    
                    ByteArrayOutputStream outstream = new ByteArrayOutputStream(
                            contentLength > 0 ? (int) contentLength : 4096);
                    byte[] buffer = new byte[4096];
                    int len;
                    while ((len = inputStream.read(buffer)) > 0) {
                        outstream.write(buffer, 0, len);
                    }
                    String responseBody = outstream.toString("UTF-8");
                    outstream.close();
                    inputStream.close();
					Map<String, String> map = convertXml2Map(responseBody);
                        if (map!=null) {
             				result.putAll(map);
             			}
                    
                    log.info("status={}, response:{}", statusLine.getStatusCode(), responseBody);
                } else {
                	log.error("no http entity, status={}", statusLine.getStatusCode());
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
                httpPost.releaseConnection();
            }
        } catch(Exception e) {
        	log.error("", e);
        }
        
        return result;
    }

	@Override
	public TradeStatusResult queryPayStatus(PaymentInfo paymentInfoDO) {
		boolean forApp = false;
		if(paymentInfoDO.getTradeType() != null && PaymentConstant.TRADE_TYPE.APP.code.intValue() == paymentInfoDO.getTradeType().intValue())
			forApp = true;
		String queryUrl = settings.getProperty("weixinPay.queryUrl");
		Map<String, String> queryFormData =  new TreeMap<String, String>();
		queryFormData.put("appid", settings.getProperty("weixinPay.appid"));// 公众账号ID 
		queryFormData.put("mch_id", settings.getProperty("weixinPay.mch_id"));// 微信支付分配的商户号 
		if(forApp){
			queryFormData.put("appid", settings.getProperty("weixinPay.appAppid"));// 公众账号ID 
			queryFormData.put("mch_id", settings.getProperty("weixinPay.appMch_id"));// 微信支付分配的商户号 
		}
//		queryFormData.put("transaction_id", paymentInfoDO.getGatewayTradeNo());// 微信的订单号，优先使用 
		queryFormData.put("out_trade_no", paymentInfoDO.getPaymentTradeNo());// 商户系统内部的订单号，当没提供transaction_id时需要传这个。 
		if(PaymentConstant.BIZ_TYPE.DSS.code.equals(paymentInfoDO.getBizType())) {
			queryFormData.put("out_trade_no", paymentInfoDO.getSerial());
		}
		queryFormData.put("nonce_str", String.valueOf(Math.random()));// 随机字符串，不长于32位。
		
		String sign = createSignature(queryFormData, forApp);
		queryFormData.put("sign", sign);
		
		log.info("query to {} param:{}",  queryUrl, queryFormData);
		Map<String, String> queryResult = submitUrl(queryFormData,queryUrl, false, forApp);
		log.info("query result={}", queryResult);
		
		String return_code = queryResult.get("return_code");
		String return_msg = queryResult.get("return_msg");
		String result_code = queryResult.get("result_code");
		String err_code = queryResult.get("err_code");
		String err_code_des = queryResult.get("err_code_des");
		String trade_state = queryResult.get("trade_state");
		
		TradeStatusResult result = new TradeStatusResult();
		result.setTradeNo(queryResult.get("transaction_id"));
		if ("SUCCESS".equals(trade_state)) {
			result.setSuccess(true);
		} else {
			result.setSuccess(false);
			result.setErrorMsg(String.format("%s(%s):%s", return_msg, return_code,trade_state));
		}
		result.setCanceled(Integer.valueOf(1).equals(paymentInfoDO.getCanceled()));
		return result;
	}

	@Override
	public TradeStatusResult queryRefundStatus(RefundPayinfo refundPayinfoDO) {
		String queryUrl = settings.getProperty("weixinPay.refundQueryUrl");
		Map<String, String> queryFormData =  new TreeMap<String, String>();
		queryFormData.put("appid", settings.getProperty("weixinPay.appid"));// 公众账号ID 
		queryFormData.put("mch_id", settings.getProperty("weixinPay.mch_id"));// 微信支付分配的商户号 
		queryFormData.put("device_info", settings.getProperty("weixinPay.device_info"));// 微信支付分配的终端设备号 
		queryFormData.put("nonce_str", String.valueOf(Math.random()));// 随机字符串，不长于32位。
		
		String bizCode = refundPayinfoDO.getBizCode().toString();
		if (StringUtils.isNotBlank(bizCode)) {
//			queryFormData.put("refund_id", gatewayTradeNo);// 微信退款单号  
			queryFormData.put("out_refund_no", bizCode);// 商户退款单号
		} else {
			PaymentInfo paymentInfoDO = refundPayinfoDO.getPaymentInfo();
			if (paymentInfoDO==null) {
				Long paymentId = refundPayinfoDO.getPaymentId();
				paymentInfoDO = paymentInfoDao.queryById(paymentId);
			}
			if (paymentInfoDO!=null) {
				queryFormData.put("transaction_id", paymentInfoDO.getGatewayCode());// 微信订单号  
				queryFormData.put("out_trade_no", paymentInfoDO.getPaymentTradeNo());// 商户系统内部的订单号 
				if(paymentInfoDO.getTradeType() != null && paymentInfoDO.getTradeType().intValue() != 1){
					queryFormData.put("appid", settings.getProperty("weixinPay.appAppid"));// 公众账号ID 
					queryFormData.put("mch_id", settings.getProperty("weixinPay.appMch_id"));// 微信支付分配的商户号 
				}
			}
		}
		
		String sign = createSignature(queryFormData);
		queryFormData.put("sign", sign);
		
		log.info("query to {} params:{}", queryUrl, queryFormData);
		Map<String, String> queryResult = submitUrl(queryFormData,queryUrl, false, false);
		log.info("query result={}", queryResult);
		
		String return_code = queryResult.get("return_code");
		String return_msg = queryResult.get("return_msg");
		String result_code = queryResult.get("result_code");
		String err_code = queryResult.get("err_code");
		String err_code_des = queryResult.get("err_code_des");
		String refund_status_0 = queryResult.get("refund_status_0");
		
		TradeStatusResult result = new TradeStatusResult();
		result.setTradeNo(bizCode);
		if ("SUCCESS".equals(refund_status_0)) {
			result.setSuccess(true);
		} else {
			result.setSuccess(false);
			result.setErrorMsg(result_code+"-"+err_code_des);
		}
		return result;
	}

	
	/**
	 *获取微信对账文件 数据提交 提交到后台
	 * 
	 * @param submitFromData
	 * @param requestUrl 
	 * @param needSSL 是否需要证书
	 * @return
	 */
	private Map<String, String> submitReconUrl(Map<String, String> submitFromData,String requestUrl, boolean needSSL, boolean forApp){
        
        Map<String, String> result = new HashMap<String, String>();
        try {

            HttpPost httpPost = new HttpPost(requestUrl);

            StringBuilder requestBody = new StringBuilder();
            //构造请求参数  
            requestBody.append("<xml>");
            Set<Entry<String, String>> entrySet = submitFromData.entrySet();
            for (Entry<String, String> entry : entrySet) {
            	String value = entry.getValue();
            	if (StringUtils.isBlank(value)) {
    				continue;
    			}
            	String key = entry.getKey();
				requestBody.append(String.format("<%s>%s</%s>", key, value, key));
			}
            requestBody.append("</xml>");
            log.debug("post request body={}", requestBody.toString());
         
            HttpEntity httpEntity = new StringEntity(requestBody.toString(), "UTF-8");
            httpPost.setEntity(httpEntity);
			
            CloseableHttpResponse response = null;
            if (needSSL) {
            	if(forApp)
            		response = appHttpClientWithSSL.execute(httpPost);
            	else
            		response = httpClientWithSSL.execute(httpPost);
			} else {
				response = httpClient.execute(httpPost);
			}
            
            try {
                HttpEntity entity = response.getEntity();

                StatusLine statusLine = response.getStatusLine();
                if (entity != null) {
                    long contentLength = entity.getContentLength();
					log.debug("Response content length: {}",  contentLength);
                    InputStream inputStream = entity.getContent();
                    
                    ByteArrayOutputStream outstream = new ByteArrayOutputStream(
                            contentLength > 0 ? (int) contentLength : 4096);
                    byte[] buffer = new byte[4096];
                    int len;
                    while ((len = inputStream.read(buffer)) > 0) {
                        outstream.write(buffer, 0, len);
                    }
                    outstream.close();
                    
                    String responseBody = outstream.toString("UTF-8");
//                    log.info("status={}, response:{}", statusLine.getStatusCode(), responseBody);
                    
        			result.put("response",responseBody);
                } else {
                	log.error("no http entity, status={}", statusLine.getStatusCode());
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } catch(Exception e) {
        	log.error("", e);
        }
        
        return result;
    }

}
