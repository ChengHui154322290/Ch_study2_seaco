package com.tp.backend.controller.permission;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.BackendConstant.SessionKey;
import com.tp.proxy.CaptchaServiceProxy;
import com.tp.util.StringUtil;

/**
 * 
 * <pre>
 * 获取验证码controller
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Controller
public class SecurityCodeImageController extends AbstractBaseController {

	@Autowired
	private CaptchaServiceProxy captchaServiceProxy;

	@RequestMapping(value = "securityCode/{type}")
	public void securityCode(HttpServletResponse response,
			HttpServletRequest request, 
			OutputStream os,
			HttpSession session,
			@PathVariable("type") Integer type) throws IOException {
		try {
			String key = SessionKey.getValue(type);
			if(StringUtil.isNullOrEmpty(key)) key = SessionKey.OTHER.getValue();
			captchaServiceProxy.getSecurityCode(session, response, key, 4);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
