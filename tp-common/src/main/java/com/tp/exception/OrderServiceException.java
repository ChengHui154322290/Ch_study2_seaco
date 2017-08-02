package com.tp.exception;

public class OrderServiceException extends RuntimeException {

	private static final long serialVersionUID = -183070443245145777L;
	
	/** 错误编号 */
	private Integer errorCode;

	
	public OrderServiceException() {
		super();
	}

	public OrderServiceException(Integer errorCode) {
		this(errorCode, null);
	}
	
	public OrderServiceException(Integer errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public OrderServiceException(String message) {
		super(message);
	}

	public OrderServiceException(Throwable cause) {
		super(cause);
	}

	public OrderServiceException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		if(null == errorCode ){
			this.errorCode = 2008;//通用异常，正常不会出现的异常
		}else{
			this.errorCode = errorCode;
		}		
	}
}
