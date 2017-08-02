/**
 * 
 */
package com.tp.dto.mmp.enums;

/**
 * @author szy
 *
 */
public enum InventoryOperType {
	NEW(1,"新增"),
	EDIT(2,"编辑"),
	DELETE(3,"删除"),
	TERMINATE(4,"终止"),
	ROLL_BACK(5,"回滚"),
	ROLL_BACK_TERM(6,"回滚终止");
	
	private int key;
	
	private String value;

	private InventoryOperType(int key, String value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * @return the key
	 */
	public int getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(int key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
}
