/**
 * 
 */
package com.tp.dto.cms;

import java.io.Serializable;

/**
 * 与前台交互的返回数据
 * 
 */
public class ReturnData implements Serializable {

	private static final long serialVersionUID = -1620952831373453144L;

	/** 是否成功 */
	private final Boolean isSuccess;
	/** 错误编号 */
	private final Integer errorCode;
	/** 附带数据 */
	private final Object data;
	/** 返回路径 */
	private final String returnUrl;
	
	public ReturnData(Boolean isSuccess) {
		this(isSuccess, null, null, null);
	}
	public ReturnData(Boolean isSuccess, Integer errorCode) {
		this(isSuccess, errorCode, null, null);
	}
	public ReturnData(Boolean isSuccess, Object data) {
		this(isSuccess, null, data, null);
	}
	public ReturnData(Boolean isSuccess, Integer errorCode, Object data) {
		this(isSuccess, errorCode, data, null);
	}
	public ReturnData(Boolean isSuccess, Integer errorCode, String returnUrl) {
		this(isSuccess, errorCode, null, returnUrl);
	}
	public ReturnData(Boolean isSuccess, Integer errorCode, Object data, String returnUrl) {
		this.isSuccess = isSuccess;
		this.errorCode = errorCode;
		this.data = data;
		this.returnUrl = returnUrl;
	}
	
	public Boolean getIsSuccess() {
		return isSuccess;
	}
	public Integer getErrorCode() {
		return errorCode;
	}
	public Object getData() {
		return data;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
}
