package com.tp.m.vo.user;

import com.tp.m.base.BaseVO;

public class ModifyMobileVO implements BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tel;//手机号

	private String token;//用户凭证
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	
	


}
