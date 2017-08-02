package com.tp.m.to.order;

import com.tp.m.base.BaseTO;

/**
 * 物流详细对象
 * @author zhuss
 * @2016年1月7日 下午8:52:04
 */
public class LogDetailTO implements BaseTO{

	private static final long serialVersionUID = 1235444086493105335L;

	private String title;//状态
	private String time;//时间
	
	
	public LogDetailTO() {
		super();
	}
	public LogDetailTO(String title, String time) {
		super();
		this.title = title;
		this.time = time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
