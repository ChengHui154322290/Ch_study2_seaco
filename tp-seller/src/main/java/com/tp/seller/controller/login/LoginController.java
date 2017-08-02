package com.tp.seller.controller.login;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.sup.SupplierUser;
import com.tp.seller.ao.base.CaptchaServiceAO;
import com.tp.seller.ao.login.LoginAO;
import com.tp.seller.constant.CommonUtils;
import com.tp.seller.constant.SellerConstant;
import com.tp.seller.util.SessionUtils;

@Controller
@RequestMapping("/")
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LoginAO loginAO;

    @Autowired
    private CaptchaServiceAO captchaServiceAO;

    @RequestMapping(value = "tologin", method = RequestMethod.GET)
    public ModelAndView login(final ModelMap model, final HttpServletRequest request) {
        final ModelAndView mav = new ModelAndView();
        return toLoginPageInfo(mav);
    }

    /**
     * 登陆提交
     * 
     * @return
     */
    @RequestMapping(value = "loginSubmit", method = RequestMethod.POST)
    public ModelAndView loginSubmit(final HttpServletRequest request, @RequestParam("userName") final String userName,
        @RequestParam("password") final String password, @RequestParam("code") final String code) {
        final ModelAndView mav = new ModelAndView();
        if ("".equals(password)) {
            // 用户名或密码错误
            mav.addObject("passwordIsNull", "true");
            return toLoginPageInfo(mav);
        }
        boolean isTrue = false;
        isTrue = captchaServiceAO.checkAuthorCode(code, request.getSession());
        if (!isTrue) {
            // 用户名或密码错误
            mav.addObject("hasError", "true");
            return toLoginPageInfo(mav);
        }
        Integer checkRes = loginAO.checkUser(userName, password);
        if(LoginAO.IS_TRUE.equals(checkRes)){
        	isTrue = true;
        } else {
        	isTrue = false;
        	if(LoginAO.SUPPLIER_STATUS.equals(checkRes)){
        		mav.addObject("auditStatusError", "true");
                return toLoginPageInfo(mav);
        	}
        	if(LoginAO.IS_DISABLED.equals(checkRes)){
        		mav.addObject("useStatusError", "true");
                return toLoginPageInfo(mav);
        	}
        }
        
        if (!isTrue) {
            // 用户名或密码错误
            mav.addObject("pwdError", "true");
            return toLoginPageInfo(mav);
        }
        // 设置session信息
        final SupplierUser userInfo = loginAO.getUserInfo(userName);
        if (null != userInfo) {
        	SessionUtils.setSession(SellerConstant.IS_HAITAO_KEY, loginAO.isHaitaoSupplier(userInfo.getSupplierId()), request);
            SessionUtils.setSession(SellerConstant.USER_ID_KEY, userInfo.getId(), request);
            SessionUtils.setSession(SellerConstant.USER_NAME_KEY, userInfo.getLoginName(), request);
            SessionUtils.setSession(SellerConstant.SUPPLIER_ID_KEY, userInfo.getSupplierId(), request);
            
        } else {
            logger.error("用户登陆{}异常", userName);
        }
        mav.setViewName("redirect:/index");
        return mav;
    }

    /**
     * 登陆页面
     * 
     * @param mav
     * @return
     */
    private ModelAndView toLoginPageInfo(final ModelAndView mav) {
        mav.setViewName("login");
        return mav;
    }

    @RequestMapping(value = "tologout", method = RequestMethod.GET)
    public ModelAndView toLogoutPageInfo(final HttpServletRequest request) {
        final ModelAndView mav = new ModelAndView();
        SessionUtils.clearSession(request);
        return toLoginPageInfo(mav);
    }

    @RequestMapping("/seller/pop_userinfo")
    public String toPopUser(final ModelMap model, final HttpServletRequest request, @RequestParam("userName") final String userName) {
        final SupplierUser supplierUserDO = loginAO.getUserInfo(userName);
        if (supplierUserDO != null) {
            model.addAttribute("supplieruserDo", supplierUserDO);
        }
        model.addAttribute("passwordDefault", SellerConstant.PASSWORD_DEFAULT);
        return "seller/pop_userinfo";
    }

    /**
     * 修改密码
     */
    @RequestMapping(value = "/seller/pop_updatepassword", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo<Boolean> updatePassword(final ModelMap model, final HttpServletRequest request, @RequestParam("loginName") final String loginName,
        @RequestParam("password") final String password,@RequestParam("oldpassword") final String oldpassword) {
        Boolean isTrue = true;
        SupplierUser supplierUserDO = loginAO.getUserInfo(loginName);
        String getoldpassword = supplierUserDO.getPassword();
        if(getoldpassword.equals(CommonUtils.toMD5(oldpassword))){
        	 if (!SellerConstant.PASSWORD_DEFAULT.equals(password)) {
                 isTrue = loginAO.updatePassword(loginName, password);
             }
        	 return new ResultInfo<Boolean>(isTrue);
        }
        return new ResultInfo<Boolean>(new FailInfo("密码不正确"));
    }

}
