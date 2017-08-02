package com.tp.exception;


public class PlatformServiceException extends RuntimeException {

	private static final long serialVersionUID = -183070443245145777L;

	/** 错误编号 */
	private Integer errorCode;

	public PlatformServiceException() {
		super();
	}

	public PlatformServiceException(Integer errorCode) {
		this(errorCode, null);
	}

	public PlatformServiceException(Integer errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public PlatformServiceException(String message) {
		super(message);
	}

	public PlatformServiceException(Throwable cause) {
		super(cause);
	}

	public PlatformServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
}
