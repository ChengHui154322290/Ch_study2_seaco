package com.tp.mq.exception;

public class MqClientException extends Exception {

	private static final long serialVersionUID = -7348226175632572914L;

	public MqClientException() {
	}

	public MqClientException(String s) {
		super(s);
	}

	public MqClientException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public MqClientException(Throwable throwable) {
		super(throwable);
	}

	@Override
	public Throwable fillInStackTrace() {
		return this;
	}
}
