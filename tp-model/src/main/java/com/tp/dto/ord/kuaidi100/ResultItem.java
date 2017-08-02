package com.tp.dto.ord.kuaidi100;

import java.io.Serializable;

/**
 * 
 * <pre>
 * 具体运单跟踪日志信息
 * </pre>
 * 
 * @author szy
 * @time 2015-2-3 下午12:00:08
 */
public class ResultItem implements Serializable {
	private static final long serialVersionUID = -8034688387460349468L;

	/** 内容 */
	private String context;
	/** 时间，原始格式 */
	private String time;
	/** 格式化后时间 */
	private String ftime;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getFtime() {
		return ftime;
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}

}
