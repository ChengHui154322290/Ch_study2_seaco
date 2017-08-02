package com.tp.m.controller.wx;


import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.m.ao.wx.OauthAO;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.helper.PropertiesHelper;
import com.tp.m.util.AssertUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.wx.ConfigVO;
import com.tp.m.vo.wx.OauthVO;

/**
 * 微信 - 认证控制器
 * @author zhuss
 * @2016年1月15日 下午8:36:50
 */
@Controller
@RequestMapping("/wx/oauth")
public class OauthController {

	private static final Logger log = LoggerFactory.getLogger(OauthController.class);
	
	@Autowired
	private OauthAO oauthAO;
	
	
	/**
	 * 微信网页授权第一步,获取CODE的URL
	 * @return
	 */
	@RequestMapping(value = "/getcodeurl",method=RequestMethod.GET)
	@ResponseBody
	public String getCodeUrl(String url,String scope,String param){
		try{
			log.info("[API接口 - 微信网页授权获取CODE URL 入参] url= {} scope= {} param={}",url,scope,param);
			AssertUtil.notBlank(url, MResultInfo.URL_NULL);
			url = URLEncoder.encode(url,"UTF-8");
			MResultVO<OauthVO> result = oauthAO.getCodeUrl(url, scope, param);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 微信网页授权获取CODE 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 微信网页授权获取CODE  MobileException] = {}",me.getMessage());
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}catch (Exception e) {
			log.error("[API接口 - 微信网页授权获取CODE  Exception] = {}",e);
			return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.FAILED));
		}
	}
	
	
	/**
	 * 微信获取OPENID
	 * @return
	 */
	@RequestMapping(value = "/getopenid",method=RequestMethod.GET)
	@ResponseBody
	public String getOpenid(String code){
		try{
			log.info("[API接口 - 微信获取IPENID 入参] code= {}",code);
			AssertUtil.notBlank(code, MResultInfo.CODE_NULL);
			if(StringUtil.equals(code, PropertiesHelper.WX_ERROR_CODE))throw new MobileException(MResultInfo.CODE_NO_VALID);
			MResultVO<OauthVO> result = oauthAO.getOpenId(code);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 微信获取IPENID 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 微信获取IPENID  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 微信获取IPENID  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	} 
	
	/**
	 * 网页授权用户信息
	 * @return
	 */
	@RequestMapping(value = "/getuserinfo",method=RequestMethod.GET)
	@ResponseBody
	public String getUserInfoByOauth(String code){
		try{
			log.info("[API接口 - 网页授权用户信息 入参] code= {}",code);
			AssertUtil.notBlank(code, MResultInfo.CODE_NULL);
			if(StringUtil.equals(code, PropertiesHelper.WX_ERROR_CODE))throw new MobileException(MResultInfo.CODE_NO_VALID);
			MResultVO<OauthVO> result = oauthAO.getUserInfoByOauth(code);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 网页授权用户信息 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 网页授权用户信息  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 网页授权用户信息  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	} 
	
	/**
	 * 通过config接口注入权限验证配置
	 * @return
	 */
	@RequestMapping(value = "/config",method=RequestMethod.GET)
	@ResponseBody
	public String config(String url){
		try{
			AssertUtil.notBlank(url, MResultInfo.CONFIG_URL_NULL);
			MResultVO<ConfigVO> result = oauthAO.config(url);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 注入权限验证配置 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 注入权限验证配置  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 注入权限验证配置  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	} 
}
