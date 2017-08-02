package com.tp.exception;


/**
 * 无效appkey异常
 * 
 */
public class InvalidAppkeyException extends RuntimeException {

	private static final long serialVersionUID = 5990096995330692817L;
	
	private static final String MESSAGE = "appkey[%d]不存在或是无效的";
	
	public InvalidAppkeyException(String appkey) {
		super(String.format(MESSAGE, appkey));
	}
}
