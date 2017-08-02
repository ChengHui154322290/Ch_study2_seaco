package com.tp.exception;

public class UserServiceException extends RuntimeException {
	
	private static final long serialVersionUID = -183070443245145777L;

	private Integer errorCode;
	
	public UserServiceException() {
    	super();
    }
    
    public UserServiceException(String message) {
    	super(message);
    }
    
	public UserServiceException(Integer errorCode) {
		this(errorCode, null);
	}
	
	public UserServiceException(Integer errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
    
    public UserServiceException(Throwable cause) {
    	super(cause);
    }
    
    public UserServiceException(String message, Throwable cause) {
    	super(message, cause);
    }

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
    
}
