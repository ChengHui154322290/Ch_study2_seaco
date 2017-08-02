package com.tp.dto.sys.apilog;

import java.io.Serializable;

import com.tp.dto.sys.ApicallResendData;
import com.tp.model.sys.ApicallLog;

public class STOApicallResendData extends ApicallResendData implements Serializable{

	private static final long serialVersionUID = 6420084967490491557L;

	private String content;
	
	public STOApicallResendData(){
	}
	
	public STOApicallResendData(ApicallLog log){
		initResendData(log);
	}
	
	@Override
	public void initResendData(ApicallLog log) {
		setContent(log.getContent());
		//设置请求类型
		setContentType(log.getContentType());
		//设置URL
		setUrl(RESEND_HOST + log.getUri());
		//设置请求参数
		setRequestParams(null);
		//设置请求内容
		setRequestContent(getContent());
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
