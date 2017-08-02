package com.tp.proxy.wx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.wx.Oauth2UserInfoDto;
import com.tp.exception.ServiceException;
import com.tp.service.wx.IUserManagerService;
import com.tp.util.StringUtil;

/**
 * 用户管理
 * @author zhuss
 *
 */
@Service
public class UserManagerProxy {
	
	private static final Logger log = LoggerFactory.getLogger(UserManagerProxy.class);

	@Autowired
	private IUserManagerService userManagerService;
	
	/**
	 * 网页授权第一步获取CODE的URL
	 * @param url：当前页面
	 * @param scope：空间
	 * @param param：参数
	 * @return
	 */
	public ResultInfo<String> getCodeUrl(String url,String scope,String param){
		try{
			if(StringUtil.isBlank(url))return new ResultInfo<>(new FailInfo("网页授权获取CODE的当前页面不能为空"));
			return new ResultInfo<>(userManagerService.getCodeUrl(url, scope, param));
		}catch(Exception e){
			log.error("[网页授权第一步获取CODE的URL EXCEPTION ] = {}",e);
			return new ResultInfo<>(new FailInfo("操作失败"));
		}
	}
	
	/**
	 * 网页授权获取用户OPENID
	 * @param code
	 * @return
	 */
	public ResultInfo<String> getOpenID(String code){
		try{
			if(StringUtil.isBlank(code))throw new ServiceException("网页授权获取的CODE为空");
			return new ResultInfo<>(userManagerService.getOpenId(code));
		}catch(ServiceException se){
			log.error("[获取微信用户OPENID ServiceException ] = {}",se.getMessage());
			return new ResultInfo<>(new FailInfo(se.getMessage()));
		}catch(Exception e){
			log.error("[获取微信用户OPENID EXCEPTION ] = {}",e);
			return new ResultInfo<>(new FailInfo("操作失败"));
		}
	}
	
	/**
	 * 网页授权获取用户信息
	 * @param code
	 * @return
	 */
	public ResultInfo<Oauth2UserInfoDto> getUserInfoByOauth(String code){
		try{
			if(StringUtil.isBlank(code))throw new ServiceException("网页授权获取的CODE为空");
			return new ResultInfo<>(userManagerService.getUserInfoByOauth(code));
		}catch(ServiceException se){
			log.error("[网页授权获取用户信息 ServiceException ] = {}",se.getMessage());
			return new ResultInfo<>(new FailInfo(se.getMessage()));
		}catch(Exception e){
			log.error("[网页授权获取用户信息 EXCEPTION ] = {}",e);
			return new ResultInfo<>(new FailInfo("操作失败"));
		}
	}
}
