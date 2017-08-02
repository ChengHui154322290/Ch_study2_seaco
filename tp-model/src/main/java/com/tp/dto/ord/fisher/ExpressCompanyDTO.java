/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.dto.ord.fisher;

import java.io.Serializable;

/**
 * <pre>
 * 快递公司信息
 * </pre>
 * 
 * @author szy
 * @time 2015-4-17 下午5:17:26
 */
public class ExpressCompanyDTO implements Serializable {
	private static final long serialVersionUID = -1912408418881757179L;

	/** 快递公司编码 */
	private String code;
	/** 快递公司名称 */
	private String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
