package com.tp.test.pay;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.tp.dao.pay.PaymentInfoDao;
import com.tp.dto.pay.postdata.AliPayRefundPostData;
import com.tp.dto.pay.query.TradeStatusQuery;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.pay.RefundPayinfo;
import com.tp.query.pay.AppPaymentCallDto;
import com.tp.result.pay.TradeStatusResult;
import com.tp.service.pay.AbstractPayPlatformService;
import com.tp.service.pay.IAppPaymentService;
import com.tp.service.pay.IPayPlatformService;
import com.tp.service.pay.IPaymentService;
import com.tp.service.pay.cbdata.AlipayRefundResult;
import com.tp.service.pay.util.AlipayNotify;
import com.tp.service.pay.util.AlipaySubmit;
import com.tp.util.DateUtil;
import com.tp.test.BaseTest;

/**
 * Created by ldr on 2015/12/30.
 */

public class PayTester extends BaseTest {
	private Logger log = LoggerFactory.getLogger(PayTester.class);
	@Autowired
    IPayPlatformService alipayDirectPayPlatformService;
	@Autowired
    IPayPlatformService mergeAlipayPayPlatformService;
    @Autowired
	IAppPaymentService appPaymentService;
    @Autowired
    IPaymentService paymentService;
    @Autowired
    PaymentInfoDao paymentInfoDao;
    @Autowired
    Properties settings;
    
    @Test
    public void test(){
    	AppPaymentCallDto callDto = new AppPaymentCallDto();
    	callDto.setGateway("weixin");
    	callDto.setPaymentId(17288L);
    	Map<String,Object> map = new HashMap<>();
    	map.put("openid", "oNU-xs5XMOK1jqtfKdNXJCzjORYg");
    	callDto.setParams(map);
    	callDto.setSdk(false);
    	System.out.println(new Gson().toJson(appPaymentService.getAppData(callDto)));
    }
    @Test
    public void query(){
    	AlipaySubmit alipay = new AlipaySubmit();
    	try {
			System.out.println(alipay.singleTradeQuery(settings, "1100042610000028", true));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @Test
    public void testRefund(){
    	paymentService.refund("weixin", 2016012500000001L);
    }
    @Test
    public void testWeixinQuery () {
    	boolean forApp = false;
    	String queryUrl = settings.getProperty("weixinPay.queryUrl");
		Map<String, String> queryFormData =  new TreeMap<String, String>();
		queryFormData.put("appid", settings.getProperty("weixinPay.appid"));// 公众账号ID 
		queryFormData.put("mch_id", settings.getProperty("weixinPay.mch_id"));// 微信支付分配的商户号 
		if(forApp){
			queryFormData.put("appid", settings.getProperty("weixinPay.appAppid"));// 公众账号ID 
			queryFormData.put("mch_id", settings.getProperty("weixinPay.appMch_id"));// 微信支付分配的商户号 
		}
//		queryFormData.put("transaction_id", paymentInfoDO.getGatewayTradeNo());// 微信的订单号，优先使用 
		queryFormData.put("out_trade_no", "1016062310000945");// 商户系统内部的订单号，当没提供transaction_id时需要传这个。 
		queryFormData.put("nonce_str", String.valueOf(Math.random()));// 随机字符串，不长于32位。
		
		String sign = createSignature(queryFormData, forApp);
		queryFormData.put("sign", sign);
		
		log.info("query to {} param:{}",  queryUrl, queryFormData);
		Map<String, String> queryResult = submitUrl(queryFormData,queryUrl, false, forApp);
		System.out.println("query result={}"+ queryResult);
		
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
    }
    @Test
    public void testAlipayRefund() {
    	Map<String, String> sParam = new HashMap<String, String>();
    	RefundPayinfo refundPayinfoDO = new RefundPayinfo();
    	///////////////////////
    	refundPayinfoDO.setPaymentId(196L);
    	refundPayinfoDO.setAmount(0.01);
    	///////////////////////
		
		AliPayRefundPostData refundData = new AliPayRefundPostData(settings);
		Double amount = refundPayinfoDO.getAmount();
		PaymentInfo paymentInfoDO = paymentInfoDao.queryById(refundPayinfoDO.getPaymentId());
		refundData.setDetailData(paymentInfoDO.getGatewayTradeNo()+ "^" + amount + "^" +"订单退款");
		refundData.setBatchNo(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
		refundData.setBatchNum("1");
		sParam.put("out_trade_no", paymentInfoDO.getPaymentTradeNo());
		sParam.put("trade_no", paymentInfoDO.getGatewayTradeNo());
		sParam.put("payType", "支付宝网关");
		sParam.put("payUser", "alipay");
		
		String sHtmlText;
		try {
			log.info("支付宝退款参数：{}", refundData);
			sHtmlText = AlipaySubmit.refund(refundData);
			log.info("支付宝退款结果：{}", sHtmlText);
			Document doc_notify_data = DocumentHelper.parseText(sHtmlText);
			String isSuccess = doc_notify_data.selectSingleNode("//alipay/is_success").getText();
			sParam.put("is_success", isSuccess);
			if("F".equals(isSuccess)){
				sParam.put("error", doc_notify_data.selectSingleNode("//alipay/error").getText());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sParam.put("is_success", "F");
			sParam.put("error", e.getMessage());
		}
		System.out.println(sParam);
		System.out.println(new Gson().toJson(sParam));
    }
    
    private Map<String, String> submitUrl(Map<String, String> submitFromData,String requestUrl, boolean needSSL, boolean forApp){
    	CloseableHttpClient httpClientWithSSL = null;
    	CloseableHttpClient appHttpClientWithSSL = null;
    	CloseableHttpClient httpClient = null;
    	try {
//			String sslPassword = settings.getProperty("weixinPay.sslPassword");
//			String sslFilePath = settings.getProperty("weixinPay.sslFilePath");
    		httpClient = HttpClients.custom().build();
			String appSslPassword = settings.getProperty("weixinPay.appSslPassword");
			String appSslFilePath = settings.getProperty("weixinPay.appSslFilePath"); 
//			KeyStore keyStore  = KeyStore.getInstance("PKCS12");
			KeyStore appKeyStore = KeyStore.getInstance("PKCS12");
//			FileInputStream instream = new FileInputStream(new File(sslFilePath));
			File appSslFile = new File(appSslFilePath);
			if(!appSslFile.exists()){
				log.error("\r\n===============\r\n\t\t{}\t文件不存在,初始化证书失败\r\n===============",appSslFilePath);
				return null;
			} 
			FileInputStream appInstream = new FileInputStream(appSslFile);
			try {
//			    keyStore.load(instream, sslPassword.toCharArray());
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
//			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, sslPassword.toCharArray()).build();
	        SSLContext appSslcontext = SSLContexts.custom().loadKeyMaterial(appKeyStore, appSslPassword.toCharArray()).build();
	        // Allow TLSv1 protocol only
//	        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
	        SSLConnectionSocketFactory appSslsf = new SSLConnectionSocketFactory(appSslcontext, new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
//	        httpClientWithSSL = HttpClients.custom().setSSLSocketFactory(sslsf).build();
	        appHttpClientWithSSL = HttpClients.custom().setSSLSocketFactory(appSslsf).build();
		} catch (Exception e) {
			log.error("初始化证书失败", e);
		}
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
            }
        } catch(Exception e) {
        	log.error("", e);
        }
        
        return result;
    }

	private Map<String, String> convertXml2Map(String xml) {
		Map<String, String> map = new HashMap<String, String>();
		Pattern p = Pattern.compile("<(\\w+)><!\\[CDATA\\[([^]]*)\\]\\]></");
		Matcher m = p.matcher(xml);
		while (m.find()) {
			map.put(m.group(1), m.group(2));
		}
		p = Pattern.compile("<(\\w+)>([^<]*)</");
		m = p.matcher(xml);
		while (m.find()) {
			map.put(m.group(1), m.group(2));
		}
	
		return map;
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
	
	
	@Test
	public void testVerifyNofity() {
		String p = "currency=USD&notify_id=850fd05d0bd794784867a4df048ce5ehgu&notify_time=2016-05-13 09:19:50&notify_type=trade_status_sync&out_trade_no=1116051210000575&sign=cCBbPd9G/GlFRZeSQotTzpIapR6kgsaZNeRjDlkUb3Er1Z7gF8iLEIHUa09Mzl0R2dL69RqQyeyu2UHYDSV8is2TNSltpSZZiCV9BQ0hRcSlK/hfkttwltbjWOIeOIgy/EPglc/c2osnSodj2VIwuHTFhDEaUZadn35H7TL+oIA=&sign_type=RSA&total_fee=20.42&trade_no=2016051221001003190252818476&trade_status=TRADE_FINISHED";
		String[] ps = p.split("&");
		String []keyValue;
		Map<String, String> map = new HashMap<>();
		for(String s : ps) {
			
			map.put(s.split("=")[0], s.split("=")[1]);
			if(s.split("=")[0].equals("sign")) {
				map.put(s.split("=")[0], s.split("=")[1]+"=");
			}
		}
		System.out.println(map);
		System.out.println(AlipayNotify.verify(map, settings, "mergeAlipay"));
		
	}
	
	@Test
	public void testPutCustoms() {
		PaymentInfo paymentInfo = paymentInfoDao.queryById(17277);
		mergeAlipayPayPlatformService.operateAfterCallbackSuccess(paymentInfo);
	}
}
