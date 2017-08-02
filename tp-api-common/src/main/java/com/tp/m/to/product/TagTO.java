package com.tp.m.to.product;

import com.tp.m.base.BaseTO;

public class TagTO implements BaseTO{

	private static final long serialVersionUID = -9117656864811285695L;

	private String tag;
	private String bgcolor; //背景色
	private String fontcolor; //字体颜色
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getBgcolor() {
		return bgcolor;
	}
	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}
	public String getFontcolor() {
		return fontcolor;
	}
	public void setFontcolor(String fontcolor) {
		this.fontcolor = fontcolor;
	}
}
