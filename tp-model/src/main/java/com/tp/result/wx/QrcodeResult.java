package com.tp.result.wx;

import java.io.Serializable;

public class QrcodeResult implements Serializable{

	private static final long serialVersionUID = 1563736068011974314L;
	private String ticket;
	private String url;
	private int expire_seconds;
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getExpire_seconds() {
		return expire_seconds;
	}
	public void setExpire_seconds(int expire_seconds) {
		this.expire_seconds = expire_seconds;
	}
}