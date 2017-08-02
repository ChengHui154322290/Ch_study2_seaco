/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.util;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * HTTP请求工具类
 * </pre>
 * 
 * @author szy
 */
public class HttpClientUtil {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	private static String DEFAULT_CHARSET = "UTF-8";
	private static int DEFAULT_CONNECTION_TIMEOUT = 10 * 1000;
	private static int DEFAULT_SO_TIMEOUT = 10 * 1000;

	public static String addUrl(String head, String tail) {
		if (head.endsWith("/")) {
			if (tail.startsWith("/")) {
				return head.substring(0, head.length() - 1) + tail;
			} else {
				return head + tail;
			}
		} else {
			if (tail.startsWith("/")) {
				return head + tail;
			} else {
				return head + "/" + tail;
			}
		}
	}

	/**
	 * 
	 * <pre>
	 * post请求数据
	 * </pre>
	 * 
	 * @param url
	 * @param params
	 * @param codePage
	 * @return
	 * @throws Exception
	 */
	public synchronized static String postData(String url, Map<String, String> params, String charset) throws Exception {

		final HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(DEFAULT_SO_TIMEOUT);

		final PostMethod method = new PostMethod(url);
		charset = StringUtils.isBlank(charset) ? DEFAULT_CHARSET : charset;
		if (params != null) {
			method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
			method.setRequestBody(assembleRequestParams(params));
		}
		String result = "";
		try {
			httpClient.executeMethod(method);
			result = new String(method.getResponseBody(), charset);
		} catch (final Exception e) {
			logger.error("HTTP以POST请求数据异常", e);
			throw e;
		} finally {
			method.releaseConnection();
		}
		return result;
	}
	
	/**
	 * 
	 * <pre>
	 * post请求数据
	 * </pre>
	 * 
	 * @param url
	 * @param params
	 * @param codePage
	 * @return
	 * @throws Exception
	 */
	public synchronized static String postData(String url, String content, String contentType, Map<String, String> headers) throws Exception {

		final HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(DEFAULT_SO_TIMEOUT);

		final PostMethod method = new PostMethod(url);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, DEFAULT_CHARSET);
		method.setRequestEntity(new StringRequestEntity(content, contentType, DEFAULT_CHARSET));
		
		if (null != headers && !headers.isEmpty()) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				method.setRequestHeader(entry.getKey(), entry.getValue());
			}
		}
		String result = "";
		try {
			httpClient.executeMethod(method);
			result = new String(method.getResponseBody(), DEFAULT_CHARSET);
		} catch (final Exception e) {
			logger.error("HTTP以POST请求数据异常", e);
			throw e;
		} finally {
			method.releaseConnection();
		}
		return result;
	}

	/**
	 * 
	 * <pre>
	 * get请求数据
	 * </pre>
	 * 
	 * @param url
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public synchronized static String getData(String url, String charset) throws Exception {
		final HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(DEFAULT_SO_TIMEOUT);

		final GetMethod method = new GetMethod(url);
		String result = "";
		try {
			
			method.setRequestHeader("Accept-Language","zh-CN,zh;q=0.8");
			httpClient.executeMethod(method);
			charset = StringUtils.isBlank(charset) ? DEFAULT_CHARSET : charset;
			result = new String(method.getResponseBody(), charset);
		} catch (final Exception e) {
			logger.error("HTTP以GET请求数据异常", e);
			throw e;
		} finally {
			method.releaseConnection();
		}
		return result;
	}

	/**
	 * 组装http请求参数
	 * 
	 * @param params
	 * @param menthod
	 * @return
	 */
	private synchronized static NameValuePair[] assembleRequestParams(Map<String, String> data) {
		final List<NameValuePair> nameValueList = new ArrayList<NameValuePair>();

		Iterator<Map.Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
			nameValueList.add(new NameValuePair((String) entry.getKey(), (String) entry.getValue()));
		}

		return nameValueList.toArray(new NameValuePair[nameValueList.size()]);
	}
}
