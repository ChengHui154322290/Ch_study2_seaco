package com.tp.result.bse;

import java.io.Serializable;

/**
 * 
 * @author szy
 *
 */
public class ExpressInfoDTO implements Serializable{
	
	private static final long serialVersionUID = 2557303052756276111L;

	/** 公司名称 */
	private String name;

	/** 编号 */
	private String code;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

}
