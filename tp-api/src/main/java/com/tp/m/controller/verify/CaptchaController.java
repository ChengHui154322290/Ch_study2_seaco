package com.tp.m.controller.verify;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tp.m.ao.verify.CaptchaAO;
import com.tp.m.enums.CaptchaType;
import com.tp.m.enums.MResultInfo;
import com.tp.m.enums.ValidFieldType;
import com.tp.m.exception.MobileException;
import com.tp.m.query.verify.QueryCaptcha;
import com.tp.m.util.AssertUtil;

/**
 * 验证码控制器
 * @author zhuss
 */
@Controller
@RequestMapping("/captcha")
public class CaptchaController {
	
	private static final Logger log = LoggerFactory.getLogger(CaptchaController.class);
	
	@Autowired
	private CaptchaAO captchaAO;
	
	/**
	 * 生成验证码图片
	 * @return
	 */
	@RequestMapping(value = "/graphic", method=RequestMethod.GET)
	public void securityCode(QueryCaptcha queryCaptcha,HttpServletResponse response){
		try {
			AssertUtil.notScope(queryCaptcha.getType(), CaptchaType.class, MResultInfo.CAPTCHA_TYPE_NULL);
			AssertUtil.notValid(queryCaptcha.getTel(), ValidFieldType.TELEPHONE);
			String key = new StringBuffer(CaptchaType.getName(queryCaptcha.getType())).append(queryCaptcha.getTel()).toString();
			captchaAO.getSecurityCode(response, key);
		} catch (MobileException e) {
			log.error("[API接口 -生成图形验证码 MobileException] = {}",e.getMessage());
		}
	}
}
