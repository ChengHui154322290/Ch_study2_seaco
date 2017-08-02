/**
 * 
 */
package com.tp.result.wms;

import java.io.Serializable;

/**
 * @author Administrator
 * 
 */
public class WaybillResultInfo implements Serializable{
	
	private static final long serialVersionUID = 7569499359595319734L;

	/** 结果编码 **/
	private String resultCode;
	
	/** 结果内容 **/
	private String resultMsg;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
}
