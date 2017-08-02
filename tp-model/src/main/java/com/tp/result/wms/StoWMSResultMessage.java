/**
 * 
 */
package com.tp.result.wms;

import java.io.Serializable;

import com.tp.util.StringUtil;

/**
 * @author Administrator
 *
 */
public class StoWMSResultMessage implements Serializable{

	private static final long serialVersionUID = 709610296748042196L;

	/** 请求状态success或者fail */
	private String result;
	
	/** 请求的提示信息 */
	private String msg;

	public boolean isSuccess(){
		return "success".equals(StringUtil.lowerCase(result));
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
