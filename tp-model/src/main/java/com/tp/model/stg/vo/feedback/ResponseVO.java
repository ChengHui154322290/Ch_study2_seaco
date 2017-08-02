package com.tp.model.stg.vo.feedback;

import java.io.Serializable;

/**
 * <Response> 
 * 		<success>true/false</success>
 * 		<code>000</code>
 * 		<desc>说明</desc>
 * <Response>
 *
 * @author
 *
 */
public class ResponseVO implements Serializable {

	private static final long serialVersionUID = 4206892198957047763L;

	private String success;

	private String code;

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
		return "success="+this.success+
				" code="+this.getCode()+
				" desc="+this.getDesc();
	}
}
