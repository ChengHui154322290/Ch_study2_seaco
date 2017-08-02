package com.tp.m.vo.order;

import com.tp.m.base.BaseVO;

/**
 * 物流公司
 * @author zhuss
 * @2016年2月29日 下午4:36:17
 */
public class LogisticCompanyVO implements BaseVO{

	private static final long serialVersionUID = 3504418016130896190L;

	private String code;
	private String name;
	
	public LogisticCompanyVO() {
		super();
	}
	
	public LogisticCompanyVO(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}

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
}
