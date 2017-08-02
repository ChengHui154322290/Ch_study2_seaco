package com.tp.shop.controller.user;

import java.util.List;

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
import com.tp.model.mem.MemberSignPoint;
import com.tp.shop.ao.user.MemberSignPointAO;
import com.tp.shop.helper.RequestHelper;

/**
 * 用户签到
 * @author szy
 *
 */
@Controller
@RequestMapping("/member/sign/")
public class MemberSignController {

	 private static final Logger log = LoggerFactory.getLogger(MemberSignController.class);
	 
	@Autowired
	private MemberSignPointAO memberSignPointAO;
	
	@RequestMapping(value = "show", method = RequestMethod.POST)
	@ResponseBody
	public String showSignDate(HttpServletRequest request){
		try {
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            QueryUser userTO = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 用户签到 入参] = {}", JsonUtil.convertObjToStr(userTO));
            }
            MResultVO<List<MemberSignPoint>> result = memberSignPointAO.queryMemberSignPointListByMemberId(userTO.getUserid());
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 用户签到 返回值] = {}", JsonUtil.convertObjToStr(result));
            }
            return JsonUtil.convertObjToStr(result);
		} catch (MobileException me) {
	            log.error("[API接口 - 用户签到  MobileException] = {}", me.getMessage());
	            log.error("[API接口 - 用户签到 返回值] = {}", JsonUtil.convertObjToStr(new MResultVO<>(me)));
	            return JsonUtil.convertObjToStr(new MResultVO<>(me));
	       }   
	}
	
	@RequestMapping(value = "signed", method = RequestMethod.POST)
	@ResponseBody
	public String insertMemberSignPoint(HttpServletRequest request){
		try {
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            QueryUser userTO = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 用户签到 入参] = {}", JsonUtil.convertObjToStr(userTO));
            }
            MResultVO<MemberSignPoint> result = memberSignPointAO.insertMemberSignPoint(userTO.getUserid(),userTO.getNickname());
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 用户签到 返回值] = {}", JsonUtil.convertObjToStr(result));
            }
            return JsonUtil.convertObjToStr(result);
		} catch (MobileException me) {
	            log.error("[API接口 - 用户签到  MobileException] = {}", me.getMessage());
	            log.error("[API接口 - 用户签到 返回值] = {}", JsonUtil.convertObjToStr(new MResultVO<>(me)));
	            return JsonUtil.convertObjToStr(new MResultVO<>(me));
	       }   
	}
}
