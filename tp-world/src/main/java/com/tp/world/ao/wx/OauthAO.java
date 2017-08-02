package com.tp.world.ao.wx;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.common.ResultInfo;
import com.tp.dto.wx.ConfigDto;
import com.tp.dto.wx.Oauth2UserInfoDto;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.util.JsonUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.wx.ConfigVO;
import com.tp.m.vo.wx.OauthVO;
import com.tp.proxy.wx.OauthProxy;
import com.tp.proxy.wx.UserManagerProxy;

/**
 * 微信认证业务层
 * @author zhuss
 * @2016年1月15日 下午8:47:46
 */
@Service
public class OauthAO {
	
	private static final Logger log = LoggerFactory.getLogger(OauthAO.class);
	
	@Autowired
	private UserManagerProxy userManagerProxy;

	@Autowired
	private OauthProxy oauthProxy;
	
	/**
	 * 微信网页授权第一步,获取CODE的URL
	 * @return
	 */
	public MResultVO<OauthVO> getCodeUrl(String url,String scope,String param){
		try{
			ResultInfo<String> result = userManagerProxy.getCodeUrl(url, scope, param);
			if(result.isSuccess()){
				OauthVO vo = new OauthVO();
				vo.setCodeurl(result.getData());
				return new MResultVO<>(MResultInfo.OPERATION_SUCCESS,vo);
			}
			log.info("[调用proxy(getCodeUrl) 微信网页授权获取CODE 返回值] = {}",JsonUtil.convertObjToStr(result));
			return new MResultVO<>(MResultInfo.OPERATION_FAILED);
		}catch(MobileException me){
			log.error("[API接口 - 微信网页授权获取CODE MobileException] = {}",me.getMessage());
			return new MResultVO<>(me);
		}catch(Exception ex){
			log.error("[API接口 - 微信网页授权获取CODE Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.OPERATION_FAILED);
		}
	}
	
	/**
	 * 微信获取OPENID
	 * @param code:微信认证第一步获取的CODE
	 * @return
	 */
	public MResultVO<OauthVO> getOpenId(String code){
		try{
			ResultInfo<String> result = userManagerProxy.getOpenID(code);
			if(result.isSuccess()){
				return new MResultVO<>(MResultInfo.OPERATION_SUCCESS,new OauthVO(result.getData()));
			}
			log.info("[调用proxy(getOpenID) 网页授权获取OPENID 返回值] = {}",JsonUtil.convertObjToStr(result));
			return new MResultVO<>(MResultInfo.OPERATION_FAILED);
		}catch(MobileException me){
			log.error("[API接口 - 微信获取OPENID MobileException] = {}",me.getMessage());
			return new MResultVO<>(me);
		}catch(Exception ex){
			log.error("[API接口 - 微信获取OPENID Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.OPERATION_FAILED);
		}
	}
	
	/**
	 * 根据网页授权获取用户信息
	 * @param code:微信认证第一步获取的CODE
	 * @return
	 */
	public MResultVO<OauthVO> getUserInfoByOauth(String code){
		try{
			ResultInfo<Oauth2UserInfoDto> result = userManagerProxy.getUserInfoByOauth(code);
			if(result.isSuccess()){
				Oauth2UserInfoDto dto = result.getData();
				if(null != dto)
					return new MResultVO<>(MResultInfo.OPERATION_SUCCESS,new OauthVO(dto.getNickname(),dto.getHeadimgurl(),dto.getOpenid()));
			}
			log.info("[调用proxy(getOpenID) 网页授权获取OPENID 返回值] = {}",JsonUtil.convertObjToStr(result));
			return new MResultVO<>(MResultInfo.OPERATION_FAILED);
		}catch(MobileException me){
			log.error("[API接口 - 微信获取OPENID MobileException] = {}",me.getMessage());
			return new MResultVO<>(me);
		}catch(Exception ex){
			log.error("[API接口 - 微信获取OPENID Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.OPERATION_FAILED);
		}
	}
	
	/**
	 * 通过config接口注入权限验证配置
	 * @return
	 */
	public MResultVO<ConfigVO> config(String url){
		try{
			ResultInfo<ConfigDto> result = oauthProxy.config(url);
			if(result.isSuccess()){
				ConfigDto cdto = result.getData();
				ConfigVO vo = new ConfigVO();
				if(null != cdto){
					vo.setAppid(cdto.getAppid());
					vo.setNonceStr(cdto.getNonceStr());
					vo.setSignature(cdto.getSignature());
					vo.setTimestamp(StringUtil.getStrByObj(cdto.getTimestamp()));
				}
				return new MResultVO<>(MResultInfo.OPERATION_SUCCESS,vo);
			}
			log.info("[调用proxy(config) 接口注入权限验证配置 返回值] = {}",JsonUtil.convertObjToStr(result));
			return new MResultVO<>(MResultInfo.OPERATION_FAILED);
		}catch(MobileException me){
			log.error("[API接口 - 注入权限验证配置 MobileException] = {}",me.getMessage());
			return new MResultVO<>(me);
		}catch(Exception ex){
			log.error("[API接口 - 注入权限验证配置 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.FAILED);
		}
	}
}
