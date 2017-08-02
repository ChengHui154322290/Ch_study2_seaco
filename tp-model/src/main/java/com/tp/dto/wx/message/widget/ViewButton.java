package com.tp.dto.wx.message.widget;

/**
 * 
 * @ClassName: ViewButton 
 * @description: view类型的按钮
 * @author: zhuss 
 * @date: 2015年8月3日 下午5:05:12 
 * @version: V1.0
 *
 */
public class ViewButton extends Button {
	private static final long serialVersionUID = -3953621752696039654L;
	private String type;
	private String url;
	
	

	public ViewButton() {
		super();
	}

	public ViewButton(String type, String url) {
		super();
		this.type = type;
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
