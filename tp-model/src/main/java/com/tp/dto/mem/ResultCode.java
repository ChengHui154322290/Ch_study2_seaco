package com.tp.dto.mem;

import java.io.Serializable;

public class ResultCode implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResultCode(int code) {
		this.code = code;
	}
	
	private int code = 0;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
}
