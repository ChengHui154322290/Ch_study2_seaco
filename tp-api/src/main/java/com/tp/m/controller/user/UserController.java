package com.tp.m.controller.user;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.exception.UserServiceException;
import com.tp.m.ao.dss.PromoterAO;
import com.tp.m.ao.user.UserAO;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.CaptchaType;
import com.tp.m.enums.MResultInfo;
import com.tp.m.enums.ValidFieldType;
import com.tp.m.exception.MobileException;
import com.tp.m.geetest.GeetestProcess;
import com.tp.m.helper.AuthHelper;
import com.tp.m.helper.RequestHelper;
import com.tp.m.helper.cache.CaptchaCacheHelper;
import com.tp.m.helper.cache.TokenCacheHelper;
import com.tp.m.query.geetest.Geetest;
import com.tp.m.query.order.QueryAfterSales;
import com.tp.m.query.user.QueryUser;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.AssertUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.geetest.GeetestResult;
import com.tp.m.vo.user.AccountVO;
import com.tp.m.vo.user.CaptchaVO;
import com.tp.m.vo.user.ModifyMobileVO;
import com.tp.m.vo.user.UserSupVO;
import com.tp.proxy.dss.PromoterInfoProxy;
import com.tp.service.dss.IPromoterInfoService;

/**
 * 用户 - 帐户控制器
 *
 * @author zhuss
 * @2016年1月3日 下午2:01:38
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserAO userAO;

    @Autowired
    private AuthHelper authHelper;

    @Autowired
    private TokenCacheHelper cacheHelper;

    @Autowired
    private CaptchaCacheHelper captchaCacheHelper;

    @Autowired
    private GeetestProcess geetestProcess;
    @Autowired
	private PromoterInfoProxy promoterInfoProxy;
	@Autowired
	IPromoterInfoService promoterInfoService;
	@Autowired
	private PromoterAO promoterAO;

    /**
     * 用户登录
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/logon", method = RequestMethod.POST)
    @ResponseBody
    public String logon(HttpServletRequest request) {
        try {
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            QueryUser userTO = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 用户登录 入参] = {}", JsonUtil.convertObjToStr(userTO));
            }
            if (StringUtils.isBlank(userTO.getToken())) {
                if (StringUtil.isBlank(userTO.getUnionval())) {
                    AssertUtil.notValid(userTO.getLoginname(), ValidFieldType.LOGONNAME);
                    //AssertUtil.notValid(userTO.getPwd(), ValidFieldType.PASSWORD);
                }
                userTO.setIp(RequestHelper.getIpAddr(request));
                
                MResultVO<AccountVO> result = userAO.logon(userTO);
                //重新设置token值
                
                if (log.isInfoEnabled()) {
                    log.info("[API接口 - 用户登录 返回值] = {}", JsonUtil.convertObjToStr(result));
                }
                return JsonUtil.convertObjToStr(result);
            } else {
                TokenCacheTO ve = authHelper.authToken(userTO.getToken());
                AccountVO rs = new AccountVO(userTO.getToken(), ve.getTel(), ve.getName());
                rs.setIsneedbindtel(StringUtil.isBlank(ve.getTel()) ? StringUtil.ONE : StringUtil.ZERO);
                rs.setPromoterinfomobile(userAO.dsslogon(ve.getUid(), userTO.getToken()));

                return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.LOGIN_SUCCESS, rs));
            }
        } catch (MobileException me) {
            log.error("[API接口 - 用户登录  MobileException] = {}", me.getMessage());
            log.error("[API接口 - 用户登录 返回值] = {}", JsonUtil.convertObjToStr(new MResultVO<>(me)));
            return JsonUtil.convertObjToStr(new MResultVO<>(me));
        }
    }

    /**
     * 用户-验证码
     *
     * @return
     */
    @RequestMapping(value = "/getcaptcha", method = RequestMethod.POST)
    @ResponseBody
    public String getCaptcha(HttpServletRequest request) {
//        return JsonUtil.convertObjToStr(new MResultVO<>("-1", "请下载最新版本进行注册操作"));

        try {
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            QueryUser userTO = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 获取手机验证码 入参] = {}", JsonUtil.convertObjToStr(userTO));
            }
            if (RequestHelper.isAPP(userTO.getApptype())) AuthHelper.authSignature(userTO); //验证签名
            AssertUtil.notValid(userTO.getTel(), ValidFieldType.TELEPHONE);
            AssertUtil.notScope(userTO.getType(), CaptchaType.class, MResultInfo.TYPE_ERROR);
            userTO.setIp(RequestHelper.getIpAddr(request));
            //wap图形验证码
//            if (RequestHelper.isWAP(userTO.getApptype())) {
//                captchaCacheHelper.auth(CaptchaType.getName(userTO.getType()), userTO.getTel(), userTO.getGraphic());
//            }
            MResultVO<CaptchaVO> result = userAO.getCaptcha(userTO);
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 获取手机验证码 返回值] = {}", JsonUtil.convertObjToStr(result));
            }
            return JsonUtil.convertObjToStr(result);
        } catch (MobileException me) {
            log.error("[API接口 - 获取手机验证码  MobileException] = {}", me.getMessage());
            log.error("[API接口 - 获取手机验证码 返回值] = {}", JsonUtil.convertObjToStr(new MResultVO<>(me)));
            return JsonUtil.convertObjToStr(new MResultVO<>(me));
        }
    }
    /**
     * 用户-验证码
     *
     * @return
     */
    @RequestMapping(value = "/getcaptchaNew", method = RequestMethod.POST)
    @ResponseBody
    public String getCaptchaNew(HttpServletRequest request) {
//        return JsonUtil.convertObjToStr(new MResultVO<>("-1", "请下载最新版本进行注册操作"));

        try {
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            QueryUser userTO = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 获取手机验证码 入参] = {}", JsonUtil.convertObjToStr(userTO));
            }
            if (RequestHelper.isAPP(userTO.getApptype())) AuthHelper.authSignature(userTO); //验证签名
            AssertUtil.notValid(userTO.getTel(), ValidFieldType.TELEPHONE);
            AssertUtil.notScope(userTO.getType(), CaptchaType.class, MResultInfo.TYPE_ERROR);
            userTO.setIp(RequestHelper.getIpAddr(request));
            //wap图形验证码
//            if (RequestHelper.isWAP(userTO.getApptype())) {
//                captchaCacheHelper.auth(CaptchaType.getName(userTO.getType()), userTO.getTel(), userTO.getGraphic());
//            }
            MResultVO<CaptchaVO> result = userAO.getCaptchaNew(userTO);
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 获取手机验证码 返回值] = {}", JsonUtil.convertObjToStr(result));
            }
            return JsonUtil.convertObjToStr(result);
        } catch (MobileException me) {
            log.error("[API接口 - 获取手机验证码  MobileException] = {}", me.getMessage());
            log.error("[API接口 - 获取手机验证码 返回值] = {}", JsonUtil.convertObjToStr(new MResultVO<>(me)));
            return JsonUtil.convertObjToStr(new MResultVO<>(me));
        }
    }

    /**
     * 用户-极验 pre
     *
     * @return
     */
    @RequestMapping(value = "/pregeetest", method = RequestMethod.POST)
    @ResponseBody
    public String preGeetest(HttpServletRequest request) {
        try {

            String randId = UUID.randomUUID().toString();
            MResultVO<GeetestResult> result = geetestProcess.preProcess(randId);

            if (log.isInfoEnabled()) {
                log.info("[GEETEST_PRE_PROCESS.PARAM] = {}", JsonUtil.convertObjToStr(result));
            }
            if (result.success()) {
                result.getData().setRandid(randId);
            }
            return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.SUCCESS, result.getData()));
        } catch (MobileException me) {
            log.error("[GEETEST_PRE_PROCESS.  MobileException] = {}", me);
            log.error("[GEETEST_PRE_PROCESS. RESULT] = {}", JsonUtil.convertObjToStr(new MResultVO<>(me)));
            return JsonUtil.convertObjToStr(new MResultVO<>(me));
        }
    }

    /**
     * 用户-极验 二次校验 并发送手机验证码
     *
     * @return
     */
    @RequestMapping(value = "/sendgeecode", method = RequestMethod.POST)
    @ResponseBody
    public String sendGeetestCode(HttpServletRequest request) {
        try {
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            Geetest geetest = JsonUtil.parse(jsonStr, Geetest.class);
            if (log.isInfoEnabled()) {
                log.info("GEETEST_SEND_GEETEST_CODE.PARAM] = {}", JsonUtil.convertObjToStr(geetest));
            }
            if (RequestHelper.isAPP(geetest.getApptype())) AuthHelper.authSignature(geetest); //验证签名
            AssertUtil.notValid(geetest.getTel(), ValidFieldType.TELEPHONE);
            AssertUtil.notNull(geetest.getRandid(), MResultInfo.PARAM_ERROR);
            AssertUtil.notScope(geetest.getType(), CaptchaType.class, MResultInfo.TYPE_ERROR);
            geetest.setIp(RequestHelper.getIpAddr(request));
            boolean success = geetestProcess.doProcess(geetest);
            if (log.isInfoEnabled()) {
                log.info("[GEETEST_SEND_GEETEST_CODE.GEETEST_RESULT] = {}", JsonUtil.convertObjToStr(success));
            }
            if (!success) {
                return JsonUtil.convertObjToStr(new MResultVO<>("-1", "校验失败"));
            }
            QueryUser queryUser = new QueryUser();
            queryUser.setTel(geetest.getTel());
            queryUser.setType(geetest.getType());
            MResultVO<CaptchaVO> result = userAO.getCaptcha(queryUser);
            if (log.isInfoEnabled()) {
                log.info("[GEETEST_SEND_GEETEST_CODE.SEND_CODE_RESULT] = {}", JsonUtil.convertObjToStr(result));
            }
            return JsonUtil.convertObjToStr(result);

        } catch (MobileException me) {
            log.error("[GEETEST_SEND_GEETEST_CODE.  MobileException] ", me);
            log.error("[GEETEST_SEND_GEETEST_CODE.RESULT] = {}", JsonUtil.convertObjToStr(new MResultVO<>(me)));
            return JsonUtil.convertObjToStr(new MResultVO<>(me));
        }
    }


    /**
     * 用户-注册
     *
     * @return
     */
    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    @ResponseBody
    public String regist(HttpServletRequest request) {
        try {
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            QueryUser userTO = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 用户注册 入参] = {}", JsonUtil.convertObjToStr(userTO));
            }
            //一期只允许手机注册
            AssertUtil.notValid(userTO.getLoginname(), ValidFieldType.TELEPHONE);
            AssertUtil.notValid(userTO.getPwd(), ValidFieldType.PASSWORD);
            AssertUtil.notValid(userTO.getCaptcha(), ValidFieldType.CAPTCHA);
            userTO.setIp(RequestHelper.getIpAddr(request));
            MResultVO<AccountVO> result = userAO.regist(userTO);
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 用户注册 返回值] = {}", JsonUtil.convertObjToStr(result));
            }
			//默认开通扫码
            //关闭默认开通扫码功能
//            userAO.saveScanPromoter(userTO);
            return JsonUtil.convertObjToStr(result);
        } catch (MobileException me) {
            log.error("[API接口 - 用户注册  MobileException] = {}", me.getMessage());
            log.error("[API接口 - 用户注册  返回值] = {}", JsonUtil.convertObjToStr(new MResultVO<>(me)));
            return JsonUtil.convertObjToStr(new MResultVO<>(me));
        }
    }

    /**
     * 用户-忘记密码
     *
     * @return
     */
    @RequestMapping(value = "/modifypwd", method = RequestMethod.POST)
    @ResponseBody
    public String modifyPwd(HttpServletRequest request) {
        try {
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            QueryUser userTO = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 忘记密码入参] = {}", JsonUtil.convertObjToStr(userTO));
            }
            AssertUtil.notValid(userTO.getLoginname(), ValidFieldType.TELEPHONE);
            AssertUtil.notValid(userTO.getPwd(), ValidFieldType.PASSWORD);
            AssertUtil.notValid(userTO.getCaptcha(), ValidFieldType.CAPTCHA);
            userTO.setIp(RequestHelper.getIpAddr(request));
            MResultVO<AccountVO> result = userAO.modifyPwd(userTO);
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 忘记密码 返回值] = {}", JsonUtil.convertObjToStr(result));
            }
            return JsonUtil.convertObjToStr(result);
        } catch (MobileException me) {
            log.error("[API接口 - 忘记密码  MobileException] = {}", me.getMessage());
            log.error("[API接口 - 忘记密码   返回值] = {}", JsonUtil.convertObjToStr(new MResultVO<>(me)));
            return JsonUtil.convertObjToStr(new MResultVO<>(me));
        }
    }


    /**
     * 用户-退出
     *
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public String logout(HttpServletRequest request) {
        try {
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            QueryUser userTO = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 用户退出 入参] = {}", JsonUtil.convertObjToStr(userTO));
            }
            authHelper.authToken(userTO.getToken());
            //接触相关绑定
            if (StringUtil.isNotBlank(userTO.getUniontype()) && StringUtil.isNotBlank(userTO.getUnionval()))
                userAO.relieveUnion(userTO);
            //删除缓存
            cacheHelper.delToken(userTO.getToken());
            return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.LOGOUT_SUCCESS));
        } catch (MobileException me) {
            log.error("[API接口 - 用户退出  MobileException] = {}", me.getMessage());
            log.error("[API接口 - 用户退出 返回值] = {}", JsonUtil.convertObjToStr(new MResultVO<>(me)));
            return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.LOGOUT_FAILED));
        }
    }

    /**
     * 用户- 初始化角标数量
     *
     * @return
     */
    @RequestMapping(value = "/supcount", method = RequestMethod.POST)
    @ResponseBody
    public String initCount(HttpServletRequest request) {
        try {
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            QueryUser userTO = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 获取用户中心角标 入参] = {}", JsonUtil.convertObjToStr(userTO));
            }
            TokenCacheTO usr = authHelper.authToken(userTO.getToken());
            MResultVO<UserSupVO> result = userAO.supCount(usr.getUid());
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 获取用户中心角标 返回值] = {}", JsonUtil.convertObjToStr(result));
            }
            return JsonUtil.convertObjToStr(result);
        } catch (MobileException me) {
            log.error("[API接口 - 获取用户中心角标  MobileException] = {}", me.getMessage());
            log.error("[API接口 - 获取用户中心角标 返回值] = {}", JsonUtil.convertObjToStr(new MResultVO<>(me)));
            return JsonUtil.convertObjToStr(new MResultVO<>(me));
        }
    }

    /**
     * 用户- 初始化用户
     *
     * @return
     */
    @RequestMapping(value = "/init", method = RequestMethod.POST)
    @ResponseBody
    public String initUser(HttpServletRequest request) {
        try {
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            QueryUser userTO = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 初始化用户 入参] = {}", JsonUtil.convertObjToStr(userTO));
            }
            TokenCacheTO usr = authHelper.authToken(userTO.getToken());
            MResultVO<AccountVO> result = userAO.initUser(usr.getUid());
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 初始化用户 返回值] = {}", JsonUtil.convertObjToStr(result));
            }
            return JsonUtil.convertObjToStr(result);
        } catch (MobileException me) {
            log.error("[API接口 - 初始化用户  MobileException] = {}", me.getMessage());
            log.error("[API接口 - 初始化用户 返回值] = {}", JsonUtil.convertObjToStr(new MResultVO<>(me)));
            return JsonUtil.convertObjToStr(new MResultVO<>(me));
        }
    }
    
    /**
     * 用户修改手机号
     * 
     * @return
     */
    @RequestMapping(value = "/modifymobile", method = RequestMethod.POST)
    @ResponseBody
    public String modifyMobile(HttpServletRequest request) {
    	try {
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            QueryUser userTO = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
            QueryAfterSales afterSales = (QueryAfterSales) JsonUtil.getObjectByJsonStr(jsonStr, QueryAfterSales.class);
            TokenCacheTO usr = authHelper.authToken(afterSales.getToken());
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 用户修改手机号 入参] = {}", JsonUtil.convertObjToStr(userTO));
            }
            
//            AssertUtil.notValid(userTO.getLoginname(), ValidFieldType.TELEPHONE);
//            AssertUtil.notValid(userTO.getPwd(), ValidFieldType.PASSWORD);
//            AssertUtil.notValid(userTO.getCaptcha(), ValidFieldType.CAPTCHA);
            AssertUtil.notValid(userTO.getTel(), ValidFieldType.TELEPHONE);
            
            userTO.setIp(RequestHelper.getIpAddr(request));
            MResultVO<AccountVO> result = userAO.modifyMobile(userTO,usr.getUid());
            ModifyMobileVO modifyMobileVO = new ModifyMobileVO();
            if(null!=result.getData()){
            	modifyMobileVO.setTel(result.getData().getTel());
                modifyMobileVO.setToken(result.getData().getToken());
            }
            MResultVO<ModifyMobileVO> retResult = new MResultVO<ModifyMobileVO>("");
            retResult.setCode(result.getCode());
            retResult.setMsg(result.getMsg());
            retResult.setData(modifyMobileVO);
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 用户修改手机号 返回值] = {}", JsonUtil.convertObjToStr(retResult));
            }
            return JsonUtil.convertObjToStr(retResult);
        } catch (MobileException me) {
            log.error("[API接口 - 用户修改手机号  MobileException] = {}", me.getMessage());
            log.error("[API接口 - 用户修改手机号  返回值] = {}", JsonUtil.convertObjToStr(new MResultVO<>(me)));
            return JsonUtil.convertObjToStr(new MResultVO<>(me));
        } catch (UserServiceException use) {
            log.error("[API接口 - 用户修改手机号 UserServiceException] = {}", use.getMessage());
            log.error("[API接口 - 用户修改手机号  返回值] = {}", JsonUtil.convertObjToStr(new MResultVO<>(use.getMessage())));
            return JsonUtil.convertObjToStr(new MResultVO<>(use.getMessage()));
        } catch (Exception ex) {
            log.error("[API接口 - 用户修改手机号 Exception] = {}", ex.getMessage());
            log.error("[API接口 - 用户修改手机号  返回值] = {}", JsonUtil.convertObjToStr(new MResultVO<>(ex.getMessage())));
            return JsonUtil.convertObjToStr(new MResultVO<>(ex.getMessage()));
        }
    	
    }
    /**
     * 用户验证原手机号
     * 
     * @return
     */
    @RequestMapping(value = "/checkmobile", method = RequestMethod.POST)
    @ResponseBody
    public String checkMobile(HttpServletRequest request) {
    	try {
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            QueryUser userTO = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 用户验证原手机号 入参] = {}", JsonUtil.convertObjToStr(userTO));
            }
//            AssertUtil.notValid(userTO.getLoginname(), ValidFieldType.TELEPHONE);
//            AssertUtil.notValid(userTO.getPwd(), ValidFieldType.PASSWORD);
//            AssertUtil.notValid(userTO.getCaptcha(), ValidFieldType.CAPTCHA);
            AssertUtil.notValid(userTO.getTel(), ValidFieldType.TELEPHONE);
            
            userTO.setIp(RequestHelper.getIpAddr(request));
            MResultVO<Boolean> result = userAO.checkMobile(userTO);
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 用户验证原手机号 返回值] = {}", JsonUtil.convertObjToStr(result));
            }
            return JsonUtil.convertObjToStr(result);
        } catch (MobileException me) {
            log.error("[API接口 - 用户验证原手机号  MobileException] = {}", me.getMessage());
            log.error("[API接口 - 用户验证原手机号  返回值] = {}", JsonUtil.convertObjToStr(new MResultVO<>(me)));
            return JsonUtil.convertObjToStr(new MResultVO<>(me));
        } catch (UserServiceException use) {
            log.error("[API接口 - 用户验证原手机号 UserServiceException] = {}", use.getMessage());
            log.error("[API接口 - 用户验证原手机号  返回值] = {}", JsonUtil.convertObjToStr(new MResultVO<>(use.getMessage())));
            return JsonUtil.convertObjToStr(new MResultVO<>(use.getMessage()));
        } catch (Exception ex) {
            log.error("[API接口 - 用户验证原手机号 Exception] = {}", ex.getMessage());
            log.error("[API接口 - 用户验证原手机号  返回值] = {}", JsonUtil.convertObjToStr(new MResultVO<>(ex.getMessage())));
            return JsonUtil.convertObjToStr(new MResultVO<>(ex.getMessage()));
        }
    }
//    /**
//     * 用户修改密码
//     * 
//     * @return
//     */
////    @RequestMapping(value = "/modifyPassword", method = RequestMethod.POST)
////    @ResponseBody
//    public String modifyPassword(HttpServletRequest request) {
//    	try{
//    		String jsonStr = RequestHelper.getJsonStrByIO(request);
//	        QueryUser userTO = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
//	        QueryAfterSales afterSales = (QueryAfterSales) JsonUtil.getObjectByJsonStr(jsonStr, QueryAfterSales.class);
//	        TokenCacheTO usr = authHelper.authToken(afterSales.getToken());
//	        if (log.isInfoEnabled()) {
//	            log.info("[API接口 - 用户修改密码 入参] = {}", JsonUtil.convertObjToStr(userTO));
//	        }
//	        
//	        AssertUtil.notValid(userTO.getLoginname(), ValidFieldType.TELEPHONE);
//	        AssertUtil.notValid(userTO.getPwd(), ValidFieldType.PASSWORD);
//	        MResultVO<AccountVO> result = userAO.modifyMobile(userTO,usr.getUid());
//	        ModifyMobileVO modifyMobileVO = new ModifyMobileVO();
//	        if(null!=result.getData()){
//	        	modifyMobileVO.setTel(result.getData().getTel());
//	            modifyMobileVO.setToken(result.getData().getToken());
//	        }
//	        MResultVO<ModifyMobileVO> retResult = new MResultVO<ModifyMobileVO>("");
//	        retResult.setCode(result.getCode());
//	        retResult.setMsg(result.getMsg());
//	        retResult.setData(modifyMobileVO);
//	        if (log.isInfoEnabled()) {
//	            log.info("[API接口 - 用户修改密码 返回值] = {}", JsonUtil.convertObjToStr(retResult));
//	        }
//	        return JsonUtil.convertObjToStr(retResult);
//	    } catch (MobileException me) {
//	        log.error("[API接口 - 用户修改密码  MobileException] = {}", me.getMessage());
//	        log.error("[API接口 - 用户修改密码  返回值] = {}", JsonUtil.convertObjToStr(new MResultVO<>(me)));
//	        return JsonUtil.convertObjToStr(new MResultVO<>(me));
//	    } catch (UserServiceException use) {
//	        log.error("[API接口 - 用户修改密码 UserServiceException] = {}", use.getMessage());
//	        log.error("[API接口 - 用户修改密码  返回值] = {}", JsonUtil.convertObjToStr(new MResultVO<>(use.getMessage())));
//	        return JsonUtil.convertObjToStr(new MResultVO<>(use.getMessage()));
//	    } catch (Exception ex) {
//	        log.error("[API接口 - 用户修改密码 Exception] = {}", ex.getMessage());
//	        log.error("[API接口 - 用户修改密码  返回值] = {}", JsonUtil.convertObjToStr(new MResultVO<>(ex.getMessage())));
//	        return JsonUtil.convertObjToStr(new MResultVO<>(ex.getMessage()));
//	    }
//    }
}
