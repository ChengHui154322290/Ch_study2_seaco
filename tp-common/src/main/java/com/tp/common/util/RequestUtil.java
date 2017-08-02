package com.tp.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.tp.exception.ServiceException;

/**
 * 请求工具类
 * @author zhuss
 * @2016年4月27日 下午2:50:29
 */
public class RequestUtil {
	
	private static Logger log = LoggerFactory.getLogger(RequestUtil.class);
	
	/**
	 * 发送https请求
	 * @param requestUrl:请求地址
	 * @param requestMethod:请求方式（GET、POST）
	 * @param outputStr:提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpsRequest(String requestUrl,
			String requestMethod, String outputStr) {
		log.info("[发起HTTPS--{}请求  入参] url = {} param = {}",requestMethod,requestUrl,outputStr);
		JSONObject jsonObject = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		OutputStream outputStream = null;
		HttpsURLConnection conn = null;
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			//conn.setConnectTimeout(1000);//单位是毫秒
			// 设置请求方式（GET/POST）
			conn.setRequestMethod(requestMethod);
			// conn.setRequestProperty("content-type","application/x-www-form-urlencoded");
			if (null != outputStr) {
				outputStream = conn.getOutputStream();
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// 从输入流读取返回内容
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) inputStream = conn.getInputStream();
			else inputStream = conn.getErrorStream();
			inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			jsonObject = JSONObject.parseObject(buffer.toString());
			log.info("[发起HTTPS{}请求  返回值] = {}",requestMethod,buffer.toString());
		} catch (ConnectException ce) {
			log.error("连接超时：{}", ce);
			throw new ServiceException("连接超时");
		} catch (Exception e) {
			log.error("https请求异常：{}", e);
			throw new ServiceException("HTTPS请求异常");
		}
		try {
			if (null != bufferedReader)bufferedReader.close();
			if (null != inputStreamReader)inputStreamReader.close();
			if (null != inputStream)inputStream.close();
			if (null != outputStream)outputStream.close();
			if (null != conn)conn.disconnect();
		} catch (IOException e) {
			log.error("[HTTPS请求释放资源  error] = {}", e);
			throw new ServiceException("HTTPS释放资源异常");
		}
		return jsonObject;
	}
	
	/**
	 * 发起http请求并获取结果
	 * @param requestUrl 请求地址
	 * @param requestMethod  请求方式（GET、POST）
	 * @param outputStr 提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl,
			String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		HttpURLConnection httpUrlConn = null;
		OutputStream outputStream = null;
		try {
			URL url = new URL(requestUrl);
			httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				outputStream = httpUrlConn.getOutputStream();
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// 将返回的输入流转换成字符串
			inputStream = httpUrlConn.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			if (null == buffer || buffer.length() == 0)
				return null;
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.parseObject(buffer.toString());
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		} finally {
			try {
				if (null != bufferedReader)
					bufferedReader.close();
				if (null != inputStreamReader)
					inputStreamReader.close();
				if (null != inputStream)
					inputStream.close();
				if (null != outputStream)
					outputStream.close();
				if (null != httpUrlConn)
					httpUrlConn.disconnect();
			} catch (IOException e) {
				log.error("[HTTP请求释放资源  error] = {}", e);
			}
		}
		return jsonObject;
	}
}
