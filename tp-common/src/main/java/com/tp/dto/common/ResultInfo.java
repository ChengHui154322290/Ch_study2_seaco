package com.tp.dto.common;

import java.io.Serializable;
/**
 * 返回信息封闭类
 * @author szy
 *
 */
public class ResultInfo<E> implements Serializable {

	private static final long serialVersionUID = -662934615048314145L;
	
	/**返回值是否成功*/
	public boolean success = Boolean.TRUE;
	/**返回值*/
	public E data;
	/**提示信息*/
	public FailInfo msg;
	
	public ResultInfo(){
	}
	
	public ResultInfo(E data){
		this.data = data;
	}
	
	public ResultInfo(FailInfo msg){
		success = Boolean.FALSE;
		this.msg = msg;
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public E getData() {
		return data;
	}
	public void setData(E data) {
		this.success = Boolean.TRUE;
		this.data = data;
	}
	public FailInfo getMsg() {
		return msg;
	}
	public void setMsg(FailInfo msg) {
		this.success = Boolean.FALSE;
		this.msg = msg;
	}
}
