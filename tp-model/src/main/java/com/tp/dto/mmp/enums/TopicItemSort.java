package com.tp.dto.mmp.enums;

public enum TopicItemSort {

	DEFAULT("sort_index"),
	PRICE("topic_price");
	
	private String value;
	
	private TopicItemSort(String value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
