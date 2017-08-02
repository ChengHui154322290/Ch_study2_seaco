/**
 * 
 */
package com.tp.dto.sys.apilog;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.tp.dto.sys.ApicallResendData;
import com.tp.model.sys.ApicallLog;

/**
 * @author Administrator
 *
 */
public class JKFApicallResendData extends ApicallResendData implements Serializable{

	private static final long serialVersionUID = 3450246428289107376L;

	/** 消息内容 */
	private String content;
	
	/** 消息类型 */
	private String msgType;
	
	/** 签名 */
	private String dataDigest;
	
	public JKFApicallResendData(){}
	
	public JKFApicallResendData(ApicallLog log){
		initResendData(log);
	}
	
	@Override
	public void initResendData(ApicallLog log) {
		String json = log.getParam();
		Map<String, String[]> requestParam = JSONObject.parseObject(json, new TypeReference<Map<String, String[]>>(){});
		
		if (requestParam.get("content") != null){
			this.content = requestParam.get("content")[0];
		}
		if (requestParam.get("msg_type") != null){
			this.msgType = requestParam.get("msg_type")[0];
		}
		if (requestParam.get("data_digest") != null){
			this.dataDigest = requestParam.get("data_digest")[0];
		}
		//设置host
		setUrl(RESEND_HOST + log.getUri());
		//设置请求参数
		setRequestParams(requestParams());
		//设置请求内容
		setRequestContent(requestContent());
	}
	
	private Map<String, String> requestParams(){
		Map<String, String> map = new HashMap<>();
		map.put("content", content);
		map.put("msg_type", msgType);
		map.put("data_digest", dataDigest);
		return map;
	}
	
	private String requestContent(){
		return null;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getDataDigest() {
		return dataDigest;
	}

	public void setDataDigest(String dataDigest) {
		this.dataDigest = dataDigest;
	}
}
