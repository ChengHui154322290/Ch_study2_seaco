package com.tp.exception;


import org.apache.commons.lang3.StringUtils;

/**
 * 系统内部异常
 * 
 * @date 2015年8月12日上午11:46:08
 */
public class PlatformException extends RuntimeException {

	private static final long serialVersionUID = 73951957376962427L;
	/** 错误枚举 **/
	protected PlatformErrorCode error;
	/** 错误描述 **/
	private String errorMsg;

	public PlatformException() {
		super();
	}

	public PlatformException(PlatformErrorCode error) {
		super();
		this.error = error;
	}

	public PlatformException(PlatformErrorCode error, String errorMsg) {
		super();
		this.errorMsg = errorMsg;
		this.error = error;
	}

	public String getErrorCode() {
		if (this.getError() != null) {
			return this.getError().getErrorCode();
		}
		return null;
	}

	public String getErrorNumCode() {
		if (this.getError() != null) {
			return this.getError().getErrorNumCode();
		}
		return null;
	}

	public String getErrorMsg() {
		if (StringUtils.isNotBlank(errorMsg)) {
			return errorMsg;
		}
		if (this.getError() != null) {
			return this.getError().getErrorMsg();
		}
		return null;
	}

	/**
	 * @return the error
	 */
	public PlatformErrorCode getError() {
		return error;
	}

	/**
	 * @param error
	 *            the error to set
	 */
	public void setError(PlatformErrorCode error) {
		this.error = error;
	}

}
