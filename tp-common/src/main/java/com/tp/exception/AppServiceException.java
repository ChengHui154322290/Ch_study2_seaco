package com.tp.exception;

import com.tp.common.vo.app.PushConstant;

/**
 * 移动APP后台配置管理错误信息
 * @author zhuss
 * @2016年3月8日 下午6:07:28
 */
public class AppServiceException extends RuntimeException {

	private static final long serialVersionUID = 1271128862064293296L;
	/** 错误编号 */
	private Integer errorCode = -1;

	
	public AppServiceException() {
		super();
	}

	public AppServiceException(Integer errorCode) {
		this(errorCode, null);
	}
	
	public AppServiceException(Integer errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public AppServiceException(String message) {
		super(message);
		this.errorCode = -1;
	}

	public AppServiceException(Throwable cause) {
		super(cause);
	}

	public AppServiceException(PushConstant.ERROR_CODE errorCode) {
		super(errorCode.message);
		this.errorCode = errorCode.code;
	}
	
	public AppServiceException(String message, Throwable cause) {
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
