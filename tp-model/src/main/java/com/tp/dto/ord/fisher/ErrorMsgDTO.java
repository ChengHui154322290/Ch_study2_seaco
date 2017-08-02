/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.dto.ord.fisher;

import java.io.Serializable;

/**
 * <pre>
 * 错误信息
 * </pre>
 * 
 * @author szy
 * @time 2015-4-17 下午3:44:30
 */
public class ErrorMsgDTO implements Serializable {
	private static final long serialVersionUID = 7654967919367259853L;

	public ErrorMsgDTO() {
	}

	public ErrorMsgDTO(String code, String errorMsg) {
		this.code = code;
		this.errorMsg = errorMsg;
	}

	/** 订单编码 1005结果 code可为””, 回传errorMsg） */
	private String code;
	/** 失败异常信息 */
	private String errorMsg;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
