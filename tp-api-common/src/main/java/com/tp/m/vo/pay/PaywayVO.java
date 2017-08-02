package com.tp.m.vo.pay;

import com.tp.m.base.BaseVO;

/**
 * 支付方式
 * @author zhuss
 * @2016年1月7日 下午9:12:30
 */
public class PaywayVO implements BaseVO{

	private static final long serialVersionUID = -7265682173857176227L;

	private String code;
	private String name;
	
	public PaywayVO() {
		super();
	}
	public PaywayVO(String code, String name) {
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
