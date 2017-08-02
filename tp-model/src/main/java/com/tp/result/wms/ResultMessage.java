/**
 * 
 */
package com.tp.result.wms;

import java.io.Serializable;

/**
 * @author Administrator
 * wms入库出库请求接口返回结果信息
 */
public class ResultMessage implements Serializable{

	private static final long serialVersionUID = -1152596121084002346L;

	/** 消息体 **/
	private String body;
	
	/** 是否成功 **/
	private boolean success;
	
	/** 错误信息 **/
	private String error;
	
	public ResultMessage(){
		success = true;
		body = "";
		error = "";
	}
	
	public ResultMessage(boolean isSuccess) {
		this.success = isSuccess;
		this.error = "";
		this.body = "";
	}

	public ResultMessage(boolean isSuccess, String error) {
		this.success = isSuccess;
		this.error = error;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	public String toString(){
		return isSuccess() ? "成功：" + body : "失败：" + error + ";" + body;
	}
}
