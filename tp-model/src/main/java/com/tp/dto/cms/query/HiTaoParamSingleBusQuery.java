package com.tp.dto.cms.query;

import java.io.Serializable;

public class HiTaoParamSingleBusQuery implements Serializable{
	
	private static final long serialVersionUID = 7378176908143134318L;
	
	/** static的服务器地址 */
	private String staticDomain;

	public String getStaticDomain() {
		return staticDomain;
	}

	public void setStaticDomain(String staticDomain) {
		this.staticDomain = staticDomain;
	}
	
}
