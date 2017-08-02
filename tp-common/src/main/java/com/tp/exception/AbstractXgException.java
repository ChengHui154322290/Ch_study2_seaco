package com.tp.exception;

import com.tp.constant.common.ExceptionConstant;

public abstract class AbstractXgException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3017693704840322933L;
	/**
	 * 异常级别 3
	 */
	public static final Integer LEVEL = ExceptionConstant.EXCEPTION_LEVEL.BUSINESS.code;
	
	protected Integer code;
	
	protected String errorMessage;
	
	public void setError(Integer code,String errorMessage){
		this.code = code;
		this.errorMessage = errorMessage;
	}
	public abstract String getModelType();
	
	public abstract Integer getCode();
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
