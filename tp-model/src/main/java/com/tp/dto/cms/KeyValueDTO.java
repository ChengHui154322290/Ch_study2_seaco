package com.tp.dto.cms;

import java.io.Serializable;

public class KeyValueDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer key;
	
	private String value;

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	


}
