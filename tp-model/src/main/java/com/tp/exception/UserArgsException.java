package com.tp.exception;

public class UserArgsException extends UserServiceException {

	private static final long serialVersionUID = 1L;
	
	public UserArgsException(Integer errorcode, String message) {
		super(errorcode, message);
	}

}
