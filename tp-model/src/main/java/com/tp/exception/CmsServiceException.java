package com.tp.exception;

public class CmsServiceException extends RuntimeException {

	private static final long serialVersionUID = -183070443245145777L;

	public CmsServiceException() {
		super();
	}

	public CmsServiceException(String message) {
		super(message);
	}

	public CmsServiceException(Throwable cause) {
		super(cause);
	}

	public CmsServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
