/**
 * 
 */
package com.tp.dto.mmp;

import java.io.Serializable;

/**
 * 异步调用时，反馈的执行结果
 * 
 * @author szy
 *
 */
public class ReturnData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -175111162086724508L;

	/**
	 * 反馈执行是否成功
	 */
	private boolean isSuccess;

	/**
	 * 出错编号
	 */
	private Integer errorCode;

	/**
	 * 跳转url
	 */
	private String returnUrl;

	/**
	 * 附带传回数据
	 */
	private Object data;

	public ReturnData(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public ReturnData(boolean isSuccess, Object data) {
		this.isSuccess = isSuccess;
		this.data = data;
	}

	public ReturnData(boolean isSuccess, Integer errorCode) {
		this.isSuccess = isSuccess;
		this.errorCode = errorCode;
	}

	public ReturnData(boolean isSuccess, Integer errorCode, Object data) {
		this.isSuccess = isSuccess;
		this.errorCode = errorCode;
		this.data = data;
	}

	public ReturnData(boolean isSuccess, Integer errorCode, Object data,
			String returnUrl) {
		this.isSuccess = isSuccess;
		this.errorCode = errorCode;
		this.data = data;
		this.returnUrl = returnUrl;
	}

	/**
	 * @return the isSuccess
	 */
	public boolean isSuccess() {
		return isSuccess;
	}

	/**
	 * @return the errorCode
	 */
	public Integer getErrorCode() {
		return errorCode;
	}

	/**
	 * @return the returnUrl
	 */
	public String getReturnUrl() {
		return returnUrl;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

}
