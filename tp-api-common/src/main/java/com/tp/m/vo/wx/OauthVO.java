package com.tp.m.vo.wx;

import com.tp.m.base.BaseVO;

/**
 * 微信认证
 * @author zhuss
 * @2016年1月15日 下午8:40:54
 */
public class OauthVO implements BaseVO{

	private static final long serialVersionUID = 4183088945055632728L;
	//private String accessToken;//网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
	//private int expiresIn; //access_token接口调用凭证超时时间，单位（秒）
	//private String refreshToken; //用户刷新access_token
	private String openid; //用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
	//private String scope; //用户授权的作用域，使用逗号（,）分隔
    private String code;//用户认证第一步获取的CODE
    private String codeurl;
    
    //网页授权获取用户信息
    private String nickname; //昵称
    private String headimgurl; //头像
    
	public OauthVO() {
		super();
	}
	public OauthVO(String openid) {
		super();
		this.openid = openid;
	}
	
	public OauthVO(String nickname, String headimgurl,String openId) {
		super();
		this.nickname = nickname;
		this.headimgurl = headimgurl;
		this.openid = openId;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCodeurl() {
		return codeurl;
	}
	public void setCodeurl(String codeurl) {
		this.codeurl = codeurl;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
}
