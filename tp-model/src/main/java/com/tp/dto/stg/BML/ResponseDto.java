/**
 * 
 */
package com.tp.dto.stg.BML;

/**
 * @author szy
 * 标杆系统返回信息
 */
public class ResponseDto {
	public static final String SUCCESS="true";
	
	public static final String FAIL="false";
	
	/** 是否成功 true/false */
	private String success;
	/** 错误码*/
	private String code;
	/** 错误信息*/
	private String desc;
	
	
	public String getSuccess() {
		return success;
	}


	public void setSuccess(String success) {
		this.success = success;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public String toString(){
		return "code="+code+" success="+success+" desc="+desc;
	}
}
