package com.tp.dto.mmp.enums;


public enum FreightCalculateType {
	
	MBY(0, "满包邮"),
	TYYZ(1, "统一邮资");
	
	private int type;
	private String description;
	
	private FreightCalculateType(int type, String description) {
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
