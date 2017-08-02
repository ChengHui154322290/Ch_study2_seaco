package com.tp.shop.helper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.enums.common.PlatformEnum;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.util.StringUtil;
import com.tp.m.util.VerifyUtil;

/**
 * 请求的工具类
 * @author zhuss
 * @2016年1月3日 下午2:36:28
 */
public class RequestHelper {

	private static Logger log = LoggerFactory.getLogger(RequestHelper.class);
	
	public static final String DETAIL_IP = "127.0.0.1";
	
	public static final String POST_PAREMENTS_JSON_KEY = "POST_PAREMENTS_JSON_KEY";
	
	/**
	 * 请求Request转换成JSON格式字符串
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String getJsonStrByIO(HttpServletRequest request){
		//如果经过拦截器的直接从request中读取JSON格式数据
		String jsonStr = (String)request.getAttribute(POST_PAREMENTS_JSON_KEY);
		if(StringUtil.isNotBlank(jsonStr)){
			return jsonStr;
		}
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (Exception ex) {
			log.error("[请求Request转换成JSON格式字符串 Exception]", ex);
			throw new MobileException(MResultInfo.PARAM_ERROR);
		}finally{
			IOUtils.closeQuietly(bufferedReader);
		}
		if(stringBuilder.toString().equals("null") || stringBuilder.toString().equals(""))throw new MobileException(MResultInfo.PARAM_ERROR);
		request.setAttribute(RequestHelper.POST_PAREMENTS_JSON_KEY, stringBuilder.toString());
		stringBuilder.insert(1, "\"channelcode\":\""+getChannelCode(request)+"\",");
		return stringBuilder.toString();
	}
	
	/**
	 * 获取平台
	 * @param name
	 * @return
	 */
	public static PlatformEnum getPlatformByName(String name) {
		if(StringUtil.isBlank(name)) throw new MobileException(MResultInfo.PLATFORM_NULL);
		for (PlatformEnum platformEnum : PlatformEnum.values()) {
			if (platformEnum.name().equalsIgnoreCase(name)) {
				return platformEnum;
			}
		}
		throw new MobileException(MResultInfo.PLATFORM_NO_EXIST);
	}
	
	/**
	 * 校验平台是否是APP
	 * @param appType
	 * @return
	 */
	public static boolean isAPP(String appType){
		if(StringUtil.isBlank(appType))return false;
		if(StringUtil.equals(appType, PlatformEnum.IOS.name()))return true;
		if(StringUtil.equals(appType, PlatformEnum.ANDROID.name()))return true;
		return false;
	}
	
	/**
	 * 校验平台是否是APP
	 * @param appType
	 * @return
	 */
	public static boolean isWAP(String appType){
		if(StringUtil.isBlank(appType))return false;
		if(StringUtil.equals(appType, PlatformEnum.WAP.name()))return true;
		return false;
	}
	
	/**
	 *  获取请求链接的IP
	 * @param request
	 * @return
	 */
	 public static String getIpAddr(HttpServletRequest request) {  
         String ip = request.getHeader("X-Forwarded-For");  
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
             ip = request.getHeader("Proxy-Client-IP");  
         }  
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
             ip = request.getHeader("WL-Proxy-Client-IP");  
         }  
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
             ip = request.getHeader("HTTP_CLIENT_IP");  
         }  
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
             ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
         }  
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
             ip = request.getRemoteAddr();  
         }
         log.info("[GET_IP old_data ] ={}",ip);
         if(StringUtils.isNotBlank(ip)){
        	 if(ip.indexOf(',') != -1){
        		//如果通过了多级反向代理的话,是多个IP用逗号分隔,验证是否是符合规范的IP地址
            	 String[] ips = ip.split(",");
            	 ip = ips[0];
        	 } 
        	 boolean isIP = VerifyUtil.verifyIP(ip);
        	 if(isIP) {
        		 log.info("[GET_IP return_data] = {}",ip);
        		 return ip; 
        	 }
         }
         return DETAIL_IP;  
     } 
	 
	 /**
	  * 获取请求头中的channelcode 
	  */
	 public static String getChannelCode(HttpServletRequest request){
		 try {
				StringBuffer sbBuffer = request.getRequestURL();
				if (sbBuffer == null)  return null;
				URL url = new URL(sbBuffer.toString());
				return url.getHost().split("\\.")[0];
			} catch (Exception e) {
				log.error("[获取请求HOST exception]", e);
				throw new MobileException(MResultInfo.PARAM_ERROR);
			}
	 }
}
