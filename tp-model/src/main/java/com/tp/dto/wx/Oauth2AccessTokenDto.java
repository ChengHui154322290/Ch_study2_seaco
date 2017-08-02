package com.tp.dto.wx;

import java.io.Serializable;

/**
 * 获取网页授权的access_token
 * @author zhuss
 *
 */
public class Oauth2AccessTokenDto implements Serializable{

	private static final long serialVersionUID = 6251177201950133988L;

	private String access_token ;//网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
	private String refresh_token;//用户刷新access_token
	private String openid;//用户唯一标识
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
}
