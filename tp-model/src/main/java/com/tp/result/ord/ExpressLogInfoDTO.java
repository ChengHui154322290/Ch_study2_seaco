/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.result.ord;

import java.io.Serializable;

/**
 * <pre>
 * 物流日志记录信息实体
 * </pre>
 * 
 * @author szy
 * @time 2015-2-3 下午6:12:13
 */
public class ExpressLogInfoDTO implements Serializable {
	private static final long serialVersionUID = 3483340835083693975L;

	/** 时间 */
	private String dataTime;
	/** 内容 */
	private String context;

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getDataTime() {
		return dataTime;
	}

	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}

}
