/**
 * 
 */
package com.tp.dto.sys;

import java.util.Map;

import com.tp.model.sys.ApicallLog;

/**
 * @author Administrator
 *
 */
public abstract class ApicallResendData {
	
	public static final String RESEND_HOST = "http://m.51seaco.com";
	
	
	public abstract void initResendData(ApicallLog log);
	
	private String url;
	
	private Map<String, String> requestParams;
	
	private String requestContent;
	
	private String contentType;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(Map<String, String> requestParams) {
		this.requestParams = requestParams;
	}

	public String getRequestContent() {
		return requestContent;
	}

	public void setRequestContent(String requestContent) {
		this.requestContent = requestContent;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
