package com.tp.m.to.system;

import com.tp.m.base.BaseTO;

public class CityTO implements BaseTO{

	private static final long serialVersionUID = -2981776796143404731L;
	private String name;
	private String code;
	
	public CityTO() {
		super();
	}
	public CityTO(String name, String code) {
		super();
		this.name = name;
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
}
