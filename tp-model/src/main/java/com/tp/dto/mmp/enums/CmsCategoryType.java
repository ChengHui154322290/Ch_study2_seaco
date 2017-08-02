/**
 * 
 */
package com.tp.dto.mmp.enums;

/**
 * 类目枚举
 * 
 * @author szy
 *
 */
public enum CmsCategoryType {
	LV1_CATE(1, "一级类目"), LV2_CATE(2, "二级类目"), LV3_CATE(3, "三级类目");

	private int key;

	private String value;

	private CmsCategoryType(int key, String value) {
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
	 * @param key
	 *            the key to set
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
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
