package com.tp.exception;

/**
 * Created by ldr on 2015/12/31.
 */
public class ServiceException extends RuntimeException {

    private Integer errorCode;

    public ServiceException(){
        super();
    }

    public ServiceException(Throwable throwable){
        super(throwable);
    }

    public ServiceException(String message){
        super(message);
    }

    public ServiceException(String message,Integer errorCode){
        super(message);
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

}
