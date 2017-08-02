package com.tp.world.controller.user;


import javax.servlet.http.HttpServletRequest;

import com.tp.m.base.MResultVO;
import com.tp.m.exception.MobileException;
import com.tp.m.query.user.QueryUser;
import com.tp.m.util.JsonUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.user.AccountVO;
import com.tp.world.ao.user.UnionAO;
import com.tp.world.helper.AuthHelper;
import com.tp.world.helper.RequestHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 用户 - 帐号绑定控制器(对接第三方，比如绑定微信等等)
 * @author zhuss
 * @2016年1月3日 下午2:01:38
 */
@Controller
@RequestMapping("/user/union")
public class UnionController {
	
	private static final Logger log = LoggerFactory.getLogger(UnionController.class);
	
	@Autowired
	private UnionAO unionAO;

	/**
	 * 用户联合登录
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/logon",method = RequestMethod.POST)
	@ResponseBody
	public String unionLogin(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryUser userTO = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 用户联合登录 入参] = {}",JsonUtil.convertObjToStr(userTO));
			}
			AuthHelper.authSignature(userTO); //验证签名
			userTO.setIp(RequestHelper.getIpAddr(request));
			if(!RequestHelper.isAPP(userTO.getApptype())){
				if(StringUtil.isBlank(userTO.getNickname()))userTO.setNickname("西客会员");
			}
			MResultVO<AccountVO> result = unionAO.unionLogin(userTO);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 用户联合登录 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 用户联合登录  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 用户联合登录 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
}
