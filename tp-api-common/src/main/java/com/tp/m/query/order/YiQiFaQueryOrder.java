package com.tp.m.query.order;

import java.io.Serializable;

public class YiQiFaQueryOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1733443180486767677L;
	
	private String cid;
	
	private String d;
	
	private String ud;
	
	private String os;
	
	private String token;
	
	private String requestIp;
	
	private String sign;

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public String getUd() {
		return ud;
	}

	public void setUd(String ud) {
		this.ud = ud;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

}
