package com.tp.dto.wx;

import java.io.Serializable;

public class KVDto implements Serializable{

	private static final long serialVersionUID = -3695727917306889149L;

	private String key;
	private String value;
	
	
	public KVDto() {
		super();
	}
	public KVDto(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
