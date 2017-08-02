package com.tp.exception;

/**
 * 
 * <pre>
 * Excel解析异常
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 *          Exp $
 */
public class ExcelParseException extends Exception {
	/***
	 * 0整体
	 * 1必填
	 * 2长度超出了
	 */
	private int type = 0; 
	private static final long serialVersionUID = 1L;
	private String message = null;

	public ExcelParseException(Exception e) {
		initCause(e);
	}


	public ExcelParseException(int type ,String message) {
		this.message = message;
		this.type = type;
	}

	public ExcelParseException(int type ,String message, Exception e) {
		initCause(e);
		this.message = message;
		this.type = type;
	}

	@Override
	public String getMessage() {
		if (message == null) {
			return super.getMessage();
		}
		return message;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}