package com.tp.dto.cms;

import java.io.Serializable;

public class ActivityIndexDTO implements Serializable {

	private static final long serialVersionUID = 1199652299905552084L;
	
	private String title;
	
	private String description;
	
	private String key;
	
	private String content;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
