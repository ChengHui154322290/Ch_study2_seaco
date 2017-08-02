package com.tp.exception;

/**
 * Created by ldr on 2015/12/31.
 */
public class ServiceException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6462592686663560798L;
	private Integer errorCode = -1;

    public ServiceException() {
        super();
    }

    public ServiceException(Throwable throwable) {
        super(throwable);
    }

    public ServiceException(String message, Throwable throwable) {
        super(message,throwable);
    }
    
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

}
