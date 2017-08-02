package com.tp.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.tp.common.annotation.Virtual;

/**
 * bean对象基本属性
 * @author szy
 *
 */
public class BaseDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2458767624829119526L;
	public static final String VALIDATE_CODE = "validateCode";

	public static final String PLATFORM_TYPE = "platformType";

	@Virtual
	public transient String validateCode;
	@Virtual
	public transient Integer platformLevel;
	@Virtual
	private  Integer startPage = 1;
	@Virtual
	private  Integer pageSize = 10;


	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public Integer getPlatformLevel() {
		return platformLevel;
	}

	public void setPlatformLevel(Integer platformLevel) {
		this.platformLevel = platformLevel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Integer getStartPage() {
		return startPage;
	}

	public void setStartPage(Integer startPage) {
		this.startPage = startPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getStart() {
		if (startPage < 0 || pageSize < 0) {
			return 0;
		} else {
			return ((startPage - 1) * pageSize);
		}
	}
	
    @Override
    public String toString(){
        return ReflectionToStringBuilder.toString(this);
    }

	public void setPage(Integer page) {
		startPage = page;
	}

	public void setRows(Integer rows) {
		pageSize = rows;
	}
}
