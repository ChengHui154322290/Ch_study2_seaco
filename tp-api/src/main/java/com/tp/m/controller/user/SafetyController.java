package com.tp.m.controller.user;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.m.ao.user.SafetyAO;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.enums.ValidFieldType;
import com.tp.m.exception.MobileException;
import com.tp.m.helper.AuthHelper;
import com.tp.m.helper.RequestHelper;
import com.tp.m.query.user.QueryUser;
import com.tp.m.query.user.QueryUserAuth;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.AssertUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.vo.user.AccountVO;
import com.tp.m.vo.user.UserAuthVO;

/**
 * 用户 - 帐号安全控制器
 * @author zhuss
 * @2016年1月3日 下午2:01:38
 */
@Controller
@RequestMapping("/user/safety")
public class SafetyController {

	private static final Logger log = LoggerFactory.getLogger(SafetyController.class);
	
	@Autowired
	private SafetyAO safetyAO;
	
	@Autowired
	private AuthHelper authHelper;
	
	/**
	 * 绑定手机号
	 * @param userTO
	 * @return
	 */
	@RequestMapping(value="/bindtel",method = RequestMethod.POST)
	@ResponseBody
	public String bindTel(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryUser userTO = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 绑定手机号 入参] = {}",JsonUtil.convertObjToStr(userTO));
			}
			AssertUtil.notValid(userTO.getTel(), ValidFieldType.TELEPHONE);
			AssertUtil.notValid(userTO.getCaptcha(), ValidFieldType.CAPTCHA);
			authHelper.authToken(userTO.getToken());
			MResultInfo result = MResultInfo.BIND_SUCCESS;
			if(log.isInfoEnabled()){
				log.info("[API接口 - 绑定手机号 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(result)));
			}
			return JsonUtil.convertObjToStr(new MResultVO<>(result));
		}catch(MobileException me){
			log.error("[API接口 - 绑定手机号  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 绑定手机号  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 绑定联合账户
	 * @param userTO
	 * @return
	 */
	@RequestMapping(value="/bindunion",method = RequestMethod.POST)
	@ResponseBody
	public String bindUnion(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryUser userTO = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 绑定联合账户 入参] = {}",JsonUtil.convertObjToStr(userTO));
			}
			AssertUtil.notValid(userTO.getTel(), ValidFieldType.TELEPHONE);
			AssertUtil.notValid(userTO.getCaptcha(), ValidFieldType.CAPTCHA);
			TokenCacheTO user = authHelper.authToken(userTO.getToken());
			userTO.setUserid(user.getUid());
			MResultVO<AccountVO> result = safetyAO.bindUnion(userTO);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 绑定联合账户 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 绑定联合账户  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 绑定联合账户  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 用户-检查是否实名认证
	 * @return
	 */
	@RequestMapping(value="/checkauth",method = RequestMethod.POST)
	@ResponseBody
	public String checkAuth(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryUser userTO = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 检查是否实名认证 入参] = {}",JsonUtil.convertObjToStr(userTO));
			}
			TokenCacheTO usr = authHelper.authToken(userTO.getToken());
			MResultVO<UserAuthVO> result = safetyAO.checkauth(usr.getUid());
			if(log.isInfoEnabled()){
				log.info("[API接口 - 检查是否实名认证 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 检查是否实名认证  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 检查是否实名认证   返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 用户-实名认证 
	 * @return
	 */
	@RequestMapping(value="/auth",method = RequestMethod.POST)
	@ResponseBody
	public String authRealName(HttpServletRequest request) { 
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryUserAuth userTO = (QueryUserAuth) JsonUtil.getObjectByJsonStr(jsonStr, QueryUserAuth.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 用户实名认证 入参] = {}",JsonUtil.convertObjToStr(userTO));
			}
			AssertUtil.notBlank(userTO.getName(), MResultInfo.NAME_NULL);
			AssertUtil.notValid(userTO.getCode(), ValidFieldType.ID);
			TokenCacheTO usr = authHelper.authToken(userTO.getToken());
			userTO.setUserid(usr.getUid());
			MResultVO<UserAuthVO> result = safetyAO.auth(userTO);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 用户实名认证 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 用户实名认证  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 用户实名认证  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
}
