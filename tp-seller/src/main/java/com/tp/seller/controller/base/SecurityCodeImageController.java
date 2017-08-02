package com.tp.seller.controller.base;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.dto.common.ResultInfo;
import com.tp.seller.ao.base.CaptchaServiceAO;
import com.tp.seller.constant.SellerConstant;

//import com.tp.user.constants.UserConstant;

/**
 * <pre>
 * 获取验证码controller
 * </pre>
 */
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Controller
public class SecurityCodeImageController extends BaseController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CaptchaServiceAO captchaServiceAO;

    @RequestMapping(value = "securityCode")
    public void securityCode(final HttpServletResponse response, final HttpServletRequest request, final OutputStream os, final HttpSession session)
        throws IOException {
        try {
            captchaServiceAO.getSecurityCode(session, response, SellerConstant.SECURITE_CODE, 4);
        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 校验
     * 
     * @return
     */
    @RequestMapping("/checkSecuriteCode")
    @ResponseBody
    public ResultInfo<Boolean> checkCode(String code, final HttpSession session) {
    	return new ResultInfo<Boolean>(captchaServiceAO.checkAuthorCode(code, session));
    }
}
