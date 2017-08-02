package com.tp.common.util.mem;

public class SmsException extends Exception {
	
	private static final long serialVersionUID = -183070443245145777L;

    
	private Integer errorCode;
	
	public SmsException() {
    	super();
    }
    
    public SmsException(String message) {
    	super(message);
    }
    
	public SmsException(Integer errorCode) {
		this(errorCode, null);
	}
	
	public SmsException(Integer errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
    
    public SmsException(Throwable cause) {
    	super(cause);
    }
    
    public SmsException(String message, Throwable cause) {
    	super(message, cause);
    }

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
}
