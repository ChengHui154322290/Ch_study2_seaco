package com.tp.dto.wx.message.widget;
/**
 * 
 * @ClassName: ClickButton 
 * @description: 点击按钮
 * @author: zhuss 
 * @date: 2015年8月3日 下午5:04:23 
 * @version: V1.0
 *
 */
public class ClickButton extends Button {
	private static final long serialVersionUID = 6780821073172067535L;
	private String type;
	private String key;
	
	public ClickButton() {
		super();
	}

	public ClickButton(String type, String key) {
		super();
		this.type = type;
		this.key = key;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}