package com.tp.dto.cms.result;

import java.io.Serializable;


/**
 * 系统提交结果结果类型
 * @author szy
 * @param <T>
 *
 */
public class SubmitResultInfo<T>  implements Serializable{

	private static final long serialVersionUID = 7845217922136531419L;

	private String msg;
	private int code;
	private T obj;
	
	public SubmitResultInfo(String msg, int code) {
		this.msg = msg;
		this.code = code;
	}
	
	public SubmitResultInfo(String msg, int code, T obj) {
		this.msg = msg;
		this.code = code;
		this.obj = obj;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getObj() {
		return obj;
	}

	public void setObj(T obj) {
		this.obj = obj;
	}
	
}
