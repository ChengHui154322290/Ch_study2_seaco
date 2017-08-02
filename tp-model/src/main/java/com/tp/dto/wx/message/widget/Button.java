package com.tp.dto.wx.message.widget;

import java.io.Serializable;

/**
 * 
 * @ClassName: Button 
 * @description: 菜单按钮基类
 * @author: zhuss 
 * @date: 2015年8月3日 下午5:04:10 
 * @version: V1.0
 *
 */
public class Button implements Serializable{
	private static final long serialVersionUID = -4738045025551628555L;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
