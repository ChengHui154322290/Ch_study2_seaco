package com.tp.exception;

/**
 * 
 * <pre>
 * 内容规则校验异常
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 *          qunxi.shao Exp $
 */
public class ExcelContentInvalidException extends Exception {
	private static final long serialVersionUID = 1L;
	private String message = null;

	public ExcelContentInvalidException(Exception e) {
		initCause(e);
	}

	public ExcelContentInvalidException() {
	}

	public ExcelContentInvalidException(String message) {
		this.message = message;
	}

	public ExcelContentInvalidException(String message, Exception e) {
		initCause(e);
		this.message = message;
	}

	@Override
	public String getMessage() {
		if (message == null) {
			return super.getMessage();
		}
		return message;
	}
}