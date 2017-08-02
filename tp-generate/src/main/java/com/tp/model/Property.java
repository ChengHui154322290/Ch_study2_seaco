package com.tp.model;

import java.io.Serializable;
/**
 * bean属性
 * @author szy
 *
 */
public class Property implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3705761936530083867L;

	/**属性名称*/
	private String name;
	/**属性表字段*/
	private String column;
	/**属性说明*/
	private String mark;
	/**属性类型*/
	private String type;
	/**是否是主键*/
	private Boolean primary;
	
	private String methodName;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Boolean getPrimary() {
		return primary;
	}
	public void setPrimary(Boolean primary) {
		this.primary = primary;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
}
