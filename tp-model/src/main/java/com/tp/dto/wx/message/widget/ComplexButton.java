package com.tp.dto.wx.message.widget;
/**
 * 
 * @ClassName: ComplexButton 
 * @description: 复合类型的按钮 
 * @author: zhuss 
 * @date: 2015年8月3日 下午5:04:49 
 * @version: V1.0
 *
 */
public class ComplexButton extends Button {
	private static final long serialVersionUID = 3944254660105303419L;
	private Button[] sub_button;

	public Button[] getSub_button() {
		return sub_button;
	}

	public void setSub_button(Button[] sub_button) {
		this.sub_button = sub_button;
	}
}
