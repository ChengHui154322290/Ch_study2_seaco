package com.tp.dto.wx.message.widget;

import java.io.Serializable;

/**
 * 
 * @ClassName: Menu 
 * @description: 菜单
 * @author: zhuss 
 * @date: 2015年8月3日 下午5:05:01 
 * @version: V1.0
 *
 */
public class Menu implements Serializable{
	private static final long serialVersionUID = 2820760839461657256L;
	private Button[] button;

	public Button[] getButton() {
		return button;
	}

	public void setButton(Button[] button) {
		this.button = button;
	}
}
