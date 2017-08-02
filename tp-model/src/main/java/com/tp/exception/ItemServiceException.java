package com.tp.exception;

public class ItemServiceException extends RuntimeException {

	private static final long serialVersionUID = -183070443245145777L;

	public ItemServiceException() {
		super();
	}

	public ItemServiceException(String message) {
		super(message);
	}

	public ItemServiceException(Throwable cause) {
		super(cause);
	}

	public ItemServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
