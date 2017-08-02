package com.tp.m.exception;

import com.tp.m.enums.MResultInfo;


/**
 * 
 * @ClassName: BaseServiceException 
 * @description: 服务接口异常处理基类 
 * @author: zhuss 
 * @date: 2015年1月4日 上午11:19:17 
 * @version: V1.0
 *
 */
public class MobileException extends RuntimeException {
	
	private static final long serialVersionUID = -1191204169983764045L;
	
	/** 错误编号 */
	private String errorCode;

	public MobileException() {
		super();
	}

	public MobileException(MResultInfo error) {
		this(error.code, error.message);
	}
	
	public MobileException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public MobileException(String message) {
		super(message);
	}

	public MobileException(Throwable cause) {
		super(cause);
	}

	public MobileException(String message, Throwable cause) {
		super(message, cause);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
