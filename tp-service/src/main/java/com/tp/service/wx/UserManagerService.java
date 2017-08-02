package com.tp.service.wx;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.wx.RequestUrlConstant;
import com.tp.common.vo.wx.WeiXinConstant;
import com.tp.dto.wx.Oauth2AccessTokenDto;
import com.tp.dto.wx.Oauth2UserInfoDto;
import com.tp.exception.ServiceException;
import com.tp.service.wx.IUserManagerService;
import com.tp.service.wx.cache.VoucherCache;
import com.tp.service.wx.manager.UserManager;
import com.tp.util.StringUtil;

@Service
public class UserManagerService implements IUserManagerService{

	@Autowired
	private VoucherCache voucherCache;
	
	@Autowired
	Properties settings;
	
	@Override
	public List<String> queryUserList() {
		return UserManager.queryUserList(voucherCache.getAccessTokenCache());
	}
	
//	@Override
//	public List<String> queryUserListNew(Integer choise) {
//		return UserManager.queryUserList(voucherCache.getAccessTokenCacheNew(choise));
//	}
	
	@Override
	public String getCodeUrl(String url, String scope, String param) {
		if(StringUtil.isBlank(scope)) scope = WeiXinConstant.OAUTH_CODE_SCOPE_BASE;
		if(StringUtil.isBlank(param))param = WeiXinConstant.OAUTH_CODE_STATE;
		String codeUrl = RequestUrlConstant.OAUTH2_CODE_URL.replace("APPID", settings.getProperty("WX_APPID"))
				.replace("REDIRECT_URI", url).replace("SCOPE", scope)
				.replace("STATE", param);
		return codeUrl;
	}

	@Override
	public String getOpenId(String code) {
		if(StringUtil.isBlank(code))throw new ServiceException("微信网页授权CODE不能为空");
		Oauth2AccessTokenDto o = UserManager.getOauthAccessToken(code,settings.getProperty("WX_APPID"),settings.getProperty("WX_APPSECRET"));
		return o.getOpenid();
	}
	
	@Override
	public Oauth2UserInfoDto getUserInfoByOauth(String code) {
		if(StringUtil.isBlank(code))throw new ServiceException("微信网页授权CODE不能为空");
		Oauth2AccessTokenDto o = UserManager.getOauthAccessToken(code,settings.getProperty("WX_APPID"),settings.getProperty("WX_APPSECRET"));
		//获取网页授权的access_token和openid
		if(StringUtil.isNotBlank(o.getAccess_token()) && StringUtil.isNotBlank(o.getOpenid())){
			return UserManager.getOauthUserInfo(o.getAccess_token(), o.getOpenid());
		}
		return new Oauth2UserInfoDto(o.getOpenid());
	}
}
