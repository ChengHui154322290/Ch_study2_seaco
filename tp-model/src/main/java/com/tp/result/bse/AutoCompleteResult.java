package com.tp.result.bse;

import java.io.Serializable;

/**
 * 
 * <pre>
 *  自动联想封装类
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class AutoCompleteResult implements Serializable {

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = -5505075869909839277L;

	private Integer id;
	/**
	 * 标签名称
	 */
	private String label;
	/**
	 * value值
	 */
	private String value;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
