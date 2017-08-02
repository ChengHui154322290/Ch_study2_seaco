package com.tp.common.util.mem.emay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class EmayClient {

	private String name;
	private String password;
	private String url;
	private Integer smsPriority;
	
	public EmayClient(String url, String name, String password, Integer smsPriority) {
		this.name = name;
		this.password = password;
		this.url = url;
		this.smsPriority = smsPriority;
	}
	
	public Integer sendSms(String mobile, String content){
		String param = "";
		try {
			content = URLEncoder.encode(content, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String code = "";
		long seqId = System.currentTimeMillis();
		param = "cdkey=" + this.name + "&password=" + password + "&phone=" + mobile + "&message=" + content + "&addserial=" + code + "&seqid=" + seqId + "&smspriority=" + smsPriority;
		String url = this.url + "sendsms.action";
		String ret = SDKHttpClient.sendSMS(url, param);
		return Integer.valueOf(ret);
	}
	
	public Integer sendScheduledSMS(String mobile, String content, String time){
		String param = "";
		try {
			content = URLEncoder.encode(content, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String code = "";
		long seqId = System.currentTimeMillis();
		param = "cdkey=" + this.name + "&password=" + password + "&phone=" + mobile + "&message=" + content + "&addserial=" + code + "&seqid=" + seqId + "&sendtime=" + time + "&smspriority=" + smsPriority;
		String url = this.url + "sendtimesms.action";
		String ret = SDKHttpClient.sendSMS(url, param);
		return Integer.valueOf(ret);
	}
	
	public Float queryBalance(){
		String param = "";
		param = "cdkey=" + name + "&password=" + password;
		String url = this.url + "querybalance.action";
		String balance = SDKHttpClient.getBalance(url, param);
		return Float.valueOf(balance);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
