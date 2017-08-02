package com.tp.common.vo;

public class MessageBean implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;

	private Boolean success;
	
	private String msg;
	
	private String code;
	
	private Object content;
	
	public MessageBean( Boolean success){
		this.success=success;
	}
	public MessageBean( Boolean success,String msg){
		this.success=success;
		this.msg=msg;
	}

	public MessageBean( Boolean success,String msg,String code){
		this.success=success;
		this.msg=msg;
		this.code = code;
	}
	
	public MessageBean(Boolean success,String msg,String code,Object content){
		this.success=success;
		this.msg=msg;
		this.code = code;
		this.content = content;
	}
	
	public Boolean getSuccess() {
		return success;
	}

	public String getMsg() {
		return msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public Object getContent() {
		return content;
	}
	
	public void setContent(Object content) {
		this.content = content;
	}

}
