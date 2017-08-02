package com.tp.mq.exception;

public class MqConfigException extends RuntimeException {

	private static final long serialVersionUID = 3104860824777269954L;

	public MqConfigException() {
	}

	public MqConfigException(String message) {
		super(message);
	}

	public MqConfigException(Throwable cause) {
		super(cause);
	}

	public MqConfigException(String message, Throwable cause) {
		super(message, cause);

	}

}
