package com.tp.service.pay.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.util.MD5;
import com.tp.util.RSA;

/* *
 *类名：AlipayNotify
 *功能：支付宝通知处理类
 *详细：处理支付宝各接口通知返回
 *版本：3.3
 *日期：2012-08-17
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考

 *************************注意*************************
 *调试通知返回时，可查看或改写log日志的写入TXT里的数据，来检查通知返回是否正常
 */
public class AlipayNotify {
	private static Logger logger = LoggerFactory.getLogger(AlipayNotify.class);
	
	private static final String HTTPS_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&";

    /**
     * 验证消息是否是支付宝发出的合法消息
     * @param params 通知返回来的参数数组
     * @param type 支付类型(普通支付宝，国际支付宝，分账国际支付宝)
     * @return 验证结果
     */
    public static boolean verify(Map<String, String> params,Properties paymentConfig, String type) {

        //判断responsetTxt是否为true，isSign是否为true
        //responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
        //isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
    	String responseTxt = "true";
		if(params.get("notify_id") != null) {
			String notify_id = params.get("notify_id");
			boolean isInterational = false;
			if(params.containsKey("currency")){
				isInterational = true;
			}
			responseTxt = verifyResponse(notify_id,paymentConfig, isInterational, type);
		}
		logger.info("回调验证notify_id:"+responseTxt);
		if(responseTxt == null)
			return false;
	    String sign = "";
	    if(params.get("sign") != null) {sign = params.get("sign");}
	    
		//支付宝wap验签
	    if(params.containsKey("notify_data")){
	    	return wapGetSignWapVeryfy(params, paymentConfig);
	    }
	    
	    boolean isSign = getSignVeryfy(params, sign,paymentConfig, type);
	    logger.info("回调验证参数签名:"+isSign);
        //写日志记录（若要调试，请取消下面两行注释）
        String sWord = "responseTxt=" + responseTxt + "\n isSign=" + isSign + "\n 返回回来的参数：" + AlipayCore.createLinkString(params);
        logger.info(sWord);

        if (isSign && responseTxt.equals("true")) {
            return true;
        } else {
            return false;
        }
    }
    
     
    
    /**
     * 获取远程服务器ATN结果,验证返回URL
     * @param notify_id 通知校验ID
     * @return 服务器ATN结果
     * 验证结果集：
     * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 
     * true 返回正确信息
     * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
     public static String wapVerifyResponse(String notify_id,Properties paymentConfig) {
         //获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求

         String partner = paymentConfig.getProperty("ALIPAY_PARTNER");
         String veryfy_url = HTTPS_VERIFY_URL + "partner=" + partner + "&notify_id=" + notify_id;

         return checkUrl(veryfy_url, 2);
     }
     
    /**
     * 
     * <pre>
     * 验证消息是否是支付宝发出的合法消息，验证服务器异步通知
     * </pre>
     *
     * @param params 通知返回来的参数数组
     * @return 验证结果
     * @throws Exception
     */
    public static boolean verifyWapNotify(Map<String, String> params,Properties paymentConfig) throws Exception {
    	
    	//获取是否是支付宝服务器发来的请求的验证结果
    	String responseTxt = "true";
    	//XML解析notify_data数据，获取notify_id
    	Document document = DocumentHelper.parseText(params.get("notify_data"));
    	String notify_id = document.selectSingleNode( "//notify/notify_id" ).getText();
		responseTxt = wapVerifyResponse(notify_id,paymentConfig);
		
		if(responseTxt == null)
			return false;
    	
    	//获取返回时的签名验证结果
	    String sign = "";
	    if(params.get("sign") != null) {sign = params.get("sign");}
	    boolean isSign = getWapSignVeryfy(params ,sign ,false ,paymentConfig );

        //写日志记录（若要调试，请取消下面两行注释）
	    logger.info("verifyWapNotify.isSign:{}, responseTxt:{}",isSign ,responseTxt);
	    
        //判断responsetTxt是否为true，isSign是否为true
        //responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
        //isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
        if (isSign && responseTxt.equals("true")) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 
     * <pre>
     * 支付宝wap验签
     * </pre>
     *
     * @param Params
     * @param sign
     * @param paymentConfig
     * @return
     */
//    private static String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOQem0altV7eCvEPXDqSbFEWWO7HSpRV04z2Z0/vO8WoHqSjjUurTwpJ4wzLKhk/E4HcQRQCEt/pZdfhfmOsYraaiVy1umr+28RvkKbMJQ04t/DBERwOpDhijcgZ+masmft4CYfqwBadHlUpaSmhI8zc1P+NapdEtD+sB0X7VoZfAgMBAAECgYB7mjHcR7FMY9dd4krA2dGi5g7t4kIAubhUqrYz05Z5dKyV3KCUnY/ILVy+894tq8WwGV2Rr8X2WQp7vIy1CRS6goCTiHqR3P3ASeOr2wII2SeC8Z6WBGFlI2hy3EqeipcS8YXEC3cQ1v2ZzaY755myJ/dlyIK8r0nrSuFZRmWrMQJBAPQs/iTnKZxrivuWCJ/uxz2ELLln+VI4G1IX7bdm7HdfzxAkmOL1BBMuLEHERqdm1FCB5kb4l5cuqcVS8GmC5gUCQQDvKpFXCQT5asAnLAvVsAJzcx6rs/sizDdUNztQrA4L1aa2pJKgw+d8Hz6q8SnfU48myv16Hgu3kTAwANobJ+QTAkEAmMV9zUEEe+Wdc4OPqeEPr6F/ChHPoG2SSm7GQLWYxaegOg1ryrRZ9FYzQoK3Yg2oUSa1GTp4PhLoDZmy6ZEIsQJAA+egA9nD2QajKICRmOWMxLHGeGsx2HmZFajpMQH+ILdWR3kcY7+PEfm4njXdxyWoGqwJw2qmKlMWhFIfPX5DPQJBAJDLwl95QrOEPI/YVxbYCdssxagFO0GX3VTpsI8myaN55DNsBLJvEJAnyJH1r3rJM0kdg/cxRUt7fS7LImQSUSc=";
    public static boolean wapGetSignWapVeryfy(Map<String, String> Params, Properties paymentConfig){
    	try {
			//RSA签名解密

			final String notifyData = RSA.decrypt(Params.get("notify_data"), paymentConfig.getProperty("ALIPAY_RSA_PRIVATE_KEY"),  paymentConfig.getProperty("ALIPAY_INPUT_CHARSET"));
			Params.put("notify_data", notifyData);

			//XML解析notify_data数据
			Document doc_notify_data = DocumentHelper.parseText(Params.get("notify_data"));
			
			//商户订单号
			String out_trade_no = doc_notify_data.selectSingleNode( "//notify/out_trade_no" ).getText();

			//支付宝交易号
			String trade_no = doc_notify_data.selectSingleNode( "//notify/trade_no" ).getText();

			//交易状态
			String trade_status = doc_notify_data.selectSingleNode( "//notify/trade_status" ).getText();

			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
			if(AlipayNotify.verifyWapNotify(Params,paymentConfig)){//验证成功
				
				if(trade_status.equals("TRADE_FINISHED")){
					//该种交易状态只在两种情况下出现
					//1、开通了普通即时到账，买家付款成功后。
					//2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
					return true;

				} else if (trade_status.equals("TRADE_SUCCESS")){
					return true;
					//该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。
				}

			}else{//验证失败
					return false;
			}
			
		} catch (DocumentException e) {
			logger.error("getWapSignVeryfy:", e);
		} catch (Exception e) {
			logger.error("getWapSignVeryfy:", e);
		}
    	
    	return false;
    }

    /**
     * 
     * <pre>
     * 根据反馈回来的信息，生成签名结果 
     * </pre>
     *
     * @param Params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @param isSort 是否排序
     * @param paymentConfig
     * @return 生成的签名结果
     */
	private static boolean getWapSignVeryfy(Map<String, String> Params, String sign, boolean isSort, Properties paymentConfig) {
    	//过滤空值、sign与sign_type参数
    	Map<String, String> sParaNew = AlipayCore.paraFilter(Params);
    	String notifyData = sParaNew.get("notify_data");
    	logger.info("notifyData:{}", notifyData);
//    	try {
//			String nd = URLEncoder.encode(notifyData, "utf-8");
//			sParaNew.put("notify_data", nd);
//		} catch (UnsupportedEncodingException e) {
//			logger.error("", e);
//		}
    	
        //获取待签名字符串
    	String preSignStr = "";
    	if(isSort) {
    		preSignStr = AlipayCore.createLinkString(sParaNew);
    	} else {
    		preSignStr = AlipayCore.createLinkStringNoSort(sParaNew);
    	}
        //获得签名验证结果
        boolean isSign = false;
        logger.info("preSignStr:" + preSignStr);
        logger.info("sign:" + sign);
        logger.info("ALIPAY_RSA_PUBLIC_KEY:" + paymentConfig.getProperty("ALIPAY_RSA_PUBLIC_KEY") );
        isSign = RSA.verify(preSignStr, sign, paymentConfig.getProperty("ALIPAY_RSA_PUBLIC_KEY"),"utf-8");
//        isSign = RSA.verify(preSignStr, sign, privateKey,"utf-8");
        
        return isSign;
    }
	
    /**
     * 根据反馈回来的信息，生成签名结果
     * @param Params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @param type 支付类型(普通支付宝，国际支付宝，分账国际支付宝)
     * @return 生成的签名结果
     */
	private static boolean getSignVeryfy(Map<String, String> Params, String sign,Properties paymentConfig, String type) {
		
    	//过滤空值、sign与sign_type参数
		String signType = Params.get("sign_type");
    	Map<String, String> sParaNew = AlipayCore.paraFilter(Params);
    	
        //获取待签名字符串
        String preSignStr = AlipayCore.createLinkString(sParaNew);
        //获得签名验证结果
        boolean isSign = false;
		if("MD5".equalsIgnoreCase(signType) ) {
			if("alipay".equals(type) && Params.containsKey("currency")){
				logger.info("ALIPAY_INTERNATIONAL_KEY:" + paymentConfig.getProperty("ALIPAY_INTERNATIONAL_KEY") );
	        	isSign = MD5.verify(preSignStr, sign, paymentConfig.getProperty("ALIPAY_INTERNATIONAL_KEY"), paymentConfig.getProperty("ALIPAY_INPUT_CHARSET"));
			}
			else if("alipay".equals(type)){
				logger.info("ALIPAY_KEY:" + paymentConfig.getProperty("ALIPAY_KEY") );
	        	isSign = MD5.verify(preSignStr, sign, paymentConfig.getProperty("ALIPAY_KEY"), paymentConfig.getProperty("ALIPAY_INPUT_CHARSET"));
			}
			else if("mergeAlipay".equals(type)){
				logger.info("ALIPAY_MERGEALIPAY_KEY:" + paymentConfig.getProperty("ALIPAY_MERGEALIPAY_KEY") );
	        	isSign = MD5.verify(preSignStr, sign, paymentConfig.getProperty("ALIPAY_MERGEALIPAY_KEY"), paymentConfig.getProperty("ALIPAY_INPUT_CHARSET"));
			}
        } else if("RSA".equalsIgnoreCase(signType)) {
        	if("alipay".equals(type) && Params.containsKey("currency")){
        		//支付宝国际app sdk签名
        		logger.info("ALIPAY_INTERNATIONAL_RSA_PUBLIC_KEY:" + paymentConfig.getProperty("ALIPAY_INTERNATIONAL_RSA_PUBLIC_KEY") );
        		isSign = RSA.verify(preSignStr, sign, paymentConfig.getProperty("ALIPAY_INTERNATIONAL_RSA_PUBLIC_KEY"), paymentConfig.getProperty("ALIPAY_INPUT_CHARSET"));
        	}
        	else if("alipay".equals(type)){
        		//支付宝app sdk签名
        		logger.info("ALIPAY_IINTERNATIONAL_RSA_PUBLIC_KEY_WAP:" + paymentConfig.getProperty("ALIPAY_RSA_PUBLIC_KEY_WAP") );
             	isSign = RSA.verify(preSignStr, sign, paymentConfig.getProperty("ALIPAY_RSA_PUBLIC_KEY_WAP"), paymentConfig.getProperty("ALIPAY_INPUT_CHARSET"));
        	}
        	else if("mergeAlipay".equals(type)){
        		//分账支付宝app sdk签名
        		logger.info("ALIPAY_MERGEALIPAY_RSA_PUBLIC_KEY:" + paymentConfig.getProperty("ALIPAY_MERGEALIPAY_RSA_PUBLIC_KEY") );
        		isSign = RSA.verify(preSignStr, sign, paymentConfig.getProperty("ALIPAY_MERGEALIPAY_RSA_PUBLIC_KEY"), paymentConfig.getProperty("ALIPAY_INPUT_CHARSET"));
        	}
        } else if(signType==null) {
            logger.info("ALIPAY_RSA_PUBLIC_KEY_WAP:" + paymentConfig.getProperty("ALIPAY_RSA_PUBLIC_KEY_WAP") );
        	isSign = RSA.verify(preSignStr, sign, paymentConfig.getProperty("ALIPAY_RSA_PUBLIC_KEY_WAP"), paymentConfig.getProperty("ALIPAY_INPUT_CHARSET"));
        }
        return isSign;
    }

    /**
    * 获取远程服务器ATN结果,验证返回URL
    * @param notify_id 通知校验ID
    * @return 服务器ATN结果
    * 验证结果集：
    * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 
    * true 返回正确信息
    * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
    */
    public static String verifyResponse(String notify_id,Properties paymentConfig, boolean isInternational, String type) {
        //获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求

        String partner = paymentConfig.getProperty("ALIPAY_PARTNER");
        if(isInternational && "alipay".equals(type)){
        	partner = paymentConfig.getProperty("ALIPAY_INTERNATIONAL_PARTNER");
        }
        else if(isInternational){
        	partner = paymentConfig.getProperty("ALIPAY_MERGEALIPAY_PARTNER");
        }
        String veryfy_url = paymentConfig.getProperty("ALIPAY_VERIFY_URL") + "partner=" + partner + "&notify_id=" + notify_id;

        return checkUrl(veryfy_url, 0);
    }

    /**
    * 获取远程服务器ATN结果
    * @param urlvalue 指定URL路径地址
    * @return 服务器ATN结果
    * 验证结果集：
    * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 
    * true 返回正确信息
    * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
    */
    private static String checkUrl(String urlvalue, int time) {
        String inputLine = "";
        BufferedReader in = null;
        try {
            URL url = new URL(urlvalue);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlConnection
                .getInputStream()));
            inputLine = in.readLine();
            
            if(time == 2)
        		return inputLine;
            if(!"true".equals(inputLine) && !"false".equals(inputLine)){
            	Thread.sleep(2000);
				return checkUrl(urlvalue, time++);
            }
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
            inputLine = "";
            try {
            	if(time == 2)
            		return inputLine;
				Thread.sleep(2000);
				logger.warn("重试链接第{}次", time);
				return checkUrl(urlvalue, time++);
			} catch (InterruptedException e1) {
			}
        }finally{
        	if(in != null){
        		try {
					in.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
        	}
        }
        
        return inputLine;
    }
}
