package com.tp.seller.util;

import java.io.Serializable;
import java.util.Map;

/**
 * 
 * @author 喻文杰 2014年11月28日14:29:52
 *
 */
public class AjaxBean implements Serializable{


	
	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = -6546784836024146227L;
	private boolean state;
	private String title;
	private String message;
	private String url;
	private String mobile;
	private  Map<String,Object>  obj;
	public boolean getState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Map<String, Object> getObj() {
		return obj;
	}
	public void setObj(Map<String, Object> obj) {
		this.obj = obj;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
}
