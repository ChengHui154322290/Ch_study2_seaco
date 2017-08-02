package com.tp.exception;

/** 库存操作异常 */
public class InventoryServiceException extends ServiceException {
	private static final long serialVersionUID = 3970954200980114773L;
	public InventoryServiceException() {
		super();
	}

	public InventoryServiceException(Integer errorCode) {
		this(errorCode, null);
	}
	
	public InventoryServiceException(Integer errorCode, String message) {
		super(message, errorCode);
	}

	public InventoryServiceException(String message) {
		super(message);
	}

	public InventoryServiceException(Throwable cause) {
		super(cause);
	}

	public InventoryServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
