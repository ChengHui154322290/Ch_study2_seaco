/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.dto.ord.kuaidi100;

import java.io.Serializable;

/**
 * <pre>
 * 推送快递单号相关信息给快递100的结果实体类
 * </pre>
 * 
 * @author szy
 * @time 2015-2-2 上午10:19:05
 */
public class SubscribeResult implements Serializable {
	private static final long serialVersionUID = -5453585503301217887L;
	/** 结果："true"表示成功，false表示失败 */
	private Boolean result;
	/**
	 * 结果码
	 * <pre>
	 * 200: 提交成功
	 * 701: 拒绝订阅的快递公司
	 * 700: 订阅方的订阅数据存在错误（如不支持的快递公司、单号为空、单号超长等）
	 * 600: 您不是合法的订阅者（即授权Key出错）
	 * 500: 服务器错误（即快递100的服务器出理间隙或临时性异常，有时如果因为不按规范提交请求，比如快递公司参数写错等，也会报此错误）
	 * 501: 重复订阅
	 * </pre>
	 * 
	 */
	private String returnCode;
	/** 结果信息 */
	private String message;

	public SubscribeResult() {
	}

	public SubscribeResult(Boolean result, String returnCode, String message) {
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
