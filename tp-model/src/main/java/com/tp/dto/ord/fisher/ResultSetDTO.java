/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.dto.ord.fisher;

import java.io.Serializable;

/**
 * <pre>
 * 结果集
 * </pre>
 * 
 * @author szy
 * @time 2015-4-17 下午3:42:31
 */
public class ResultSetDTO implements Serializable {
	private static final long serialVersionUID = -8737053323798246961L;

	public ResultSetDTO() {
		this.resultCode = "1000";
		this.resultMsg = "全部处理成功";
	}

	public ResultSetDTO(String resultCode, String resultMsg) {
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
	}

	/** 处理结果 1000：全部成功 1005：全部失败 1015：部分失败 1025：其他异常 */
	private String resultCode;
	/** 处理消息 */
	private String resultMsg;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
