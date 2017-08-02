package com.tp.exception;

public class UserDaoException extends Exception{
	
	private static final long serialVersionUID = 5122280185969230388L;

	public UserDaoException() {
		super();
	}

	public UserDaoException(String msg) {
		super(msg);
	}

	public UserDaoException(Throwable cause) {
		super(cause);
	}

	public UserDaoException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
