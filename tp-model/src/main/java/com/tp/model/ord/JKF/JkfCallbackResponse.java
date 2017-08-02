package com.tp.model.ord.JKF;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.tp.common.vo.customs.JKFConstant.JKFResultError;


@XmlRootElement(name="response")
@XmlAccessorType(XmlAccessType.FIELD)
public class JkfCallbackResponse extends JkfBaseDO{

	private static final long serialVersionUID = -638579743451195402L;
	
	private boolean success = true;
	private String errorCode;
	private String errorMsg;
	
	public JkfCallbackResponse(){
		
	}
	
	public JkfCallbackResponse(JKFResultError error){
		this(error.code, error.name);
	}
	
	public JkfCallbackResponse(String errorCode, String errorMsg){
		this.success = false;
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
