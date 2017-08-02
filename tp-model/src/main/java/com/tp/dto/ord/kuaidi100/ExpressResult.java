/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.dto.ord.kuaidi100;

import java.io.Serializable;

/**
 * <pre>
 * 快递100平台推送快递信息处理结果类
 * </pre>
 * 
 * @author szy
 * @time 2015-2-3 上午11:37:10
 */
public class ExpressResult implements Serializable {
	private static final long serialVersionUID = 580018509452311339L;
	private Boolean result;
	/**
	 * 结果码
	 * 
	 * <pre>
	 * 200: 提交成功
	 * 500: 服务器错误
	 * </pre>
	 * 
	 */
	private String returnCode;
	/** 结果信息 */
	private String message;

	public ExpressResult() {
	}

	public ExpressResult(Boolean result, String returnCode, String message) {
		super();
		this.result = result;
		this.returnCode = returnCode;
		this.message = message;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
