package com.tp.shop.controller.user;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.m.base.MResultVO;
import com.tp.m.exception.MobileException;
import com.tp.m.query.user.QueryUser;
import com.tp.m.util.JsonUtil;
import com.tp.m.vo.user.AccountVO;
import com.tp.shop.ao.user.HhbUsergroupAO;
import com.tp.shop.helper.RequestHelper;

/**
 * 
 * @author chenghui
 * Date:   2016年11月18日 下午17:10:52
 */
@Controller
@RequestMapping("/hhbgroup")
public class HhbGroupControl {
	private static final Logger log = LoggerFactory.getLogger(HhbGroupControl.class);
	
	@Autowired
    private HhbUsergroupAO hhbUsergroupAO;
	
	/**
	 * HH商城用户登录
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/loginorregist", method = RequestMethod.GET)
    @ResponseBody
    public String login(HttpServletRequest request) {
		try {
			String channelCode = RequestHelper.getChannelCode(request);
			String openId = request.getParameter("openId");
			MResultVO<AccountVO> result = hhbUsergroupAO.loginOrRegist(openId,channelCode);
			if (log.isInfoEnabled()) {
                log.info("[API接口 - 用户登录注册 返回值] = {}", JsonUtil.convertObjToStr(result));
            }
			return JsonUtil.convertObjToStr(result);
        } catch (MobileException me) {
            log.error("[API接口 - 用户登录注册  MobileException] = {}", me.getMessage());
            log.error("[API接口 - 用户登录注册 返回值] = {}", JsonUtil.convertObjToStr(new MResultVO<>(me)));
            return JsonUtil.convertObjToStr(new MResultVO<>(me));
        }
	}
}
