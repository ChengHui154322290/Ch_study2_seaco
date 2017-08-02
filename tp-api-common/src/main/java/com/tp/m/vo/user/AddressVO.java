package com.tp.m.vo.user;

import com.tp.m.base.BaseVO;

/**
 * 用户收货地址
 * @author zhuss
 * @2016年1月4日 下午3:33:50
 */
public class AddressVO implements BaseVO{
	private static final long serialVersionUID = -3811419235493284974L;
	private String aid;//地址id

	
	public AddressVO() {
		super();
	}

	public AddressVO(String aid) {
		super();
		this.aid = aid;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}
}
