package com.tp.service.wx;

import java.util.List;

import com.tp.dto.wx.Oauth2UserInfoDto;

/**
 * 微信API - 用户管理
 * @author zhuss
 * @2016年4月27日 下午5:12:27
 */
public interface IUserManagerService {

	/**
	 * 获取公众号关注用户列表
	 * @return
	 */
	public List<String> queryUserList();
//	public List<String> queryUserListNew(Integer choise);
	
	/**
	 * 网页授权第一步获取CODE
	 * @return
	 */
	public String getCodeUrl(String url,String scope,String param);
	
	/**
	 * 网页授权获取用户OPENID
	 * @param code
	 * @return
	 */
	public String getOpenId(String code);
	
	/**
	 * 网页授权获取用户信息
	 * @param code
	 * @return
	 */
	public Oauth2UserInfoDto getUserInfoByOauth(String code);
}
