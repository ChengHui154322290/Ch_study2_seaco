package com.tp.dto.common;

import java.io.Serializable;

public class Option implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5105090063870504336L;

	public Integer id;//ID
	public String code;//编码
	public String cnName;//选项名称
	public Option(){
		
	}
	public Option(Integer id,String cnName){
		this.id = id;
		this.cnName = cnName;
	}
	public Option(String code,String cnName){
		this.code = code;
		this.cnName = cnName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
}
