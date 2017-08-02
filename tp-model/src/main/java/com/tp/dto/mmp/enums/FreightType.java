package com.tp.dto.mmp.enums;


public enum FreightType {
	
	INTERNAL(0, "国内"),
	EXTERNAL(1, "海淘");
	
	private int type;
	private String description;
	
	private FreightType(int type, String description) {
		this.type = type;
		this.description = description;
	}
	
	public int getValue() {
		return this.type;
	}
	
	public String getDescription() {
		return this.description;
	}
	
}
