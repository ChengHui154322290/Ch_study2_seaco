package com.tp.dto.wx;

import java.io.Serializable;
import java.util.List;

public class MenuButtonDto implements Serializable{

	private static final long serialVersionUID = -4881926122276319668L;

	private String name;
	private String type;
	private String key;
	private String url;
	
	private List<MenuButtonDto> sub_button;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<MenuButtonDto> getSub_button() {
		return sub_button;
	}

	public void setSub_button(List<MenuButtonDto> sub_button) {
		this.sub_button = sub_button;
	}
}
