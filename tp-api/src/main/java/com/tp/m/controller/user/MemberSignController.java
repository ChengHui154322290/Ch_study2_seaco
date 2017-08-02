package com.tp.m.controller.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.m.ao.user.MemberSignPointAO;
import com.tp.m.base.MResultVO;
import com.tp.m.exception.MobileException;
import com.tp.m.helper.AuthHelper;
import com.tp.m.helper.RequestHelper;
import com.tp.m.query.order.QueryAfterSales;
import com.tp.m.query.user.QueryUser;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.JsonUtil;
import com.tp.model.mem.MemberSignPoint;
import com.tp.model.mmp.PointMember;
import com.tp.model.mmp.PointMemberResponse;

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
	
	@Autowired
	private AuthHelper authHelper;
	
//	@RequestMapping(value = "show", method = RequestMethod.POST)
//	@ResponseBody
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
	
	
	@RequestMapping(value = "showcalendar", method = RequestMethod.POST)
	@ResponseBody
	public String showAndSignMemberSignPoint(HttpServletRequest request){
		try {
            String jsonStr = RequestHelper.getJsonStrByIO(request);
//            QueryUser userTO = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
            QueryAfterSales afterSales = (QueryAfterSales) JsonUtil.getObjectByJsonStr(jsonStr, QueryAfterSales.class);
            TokenCacheTO usr = authHelper.authToken(afterSales.getToken());
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 用户签到 入参] = {}", JsonUtil.convertObjToStr(afterSales));
            }
            if(StringUtils.isEmpty(usr.getName())){
            	usr.setName(usr.getTel());
            }
            MResultVO<List<MemberSignPoint>> result = memberSignPointAO.showAndSignPoint(usr.getUid(),usr.getName());
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
	
	@RequestMapping(value = "show", method = RequestMethod.POST)
	@ResponseBody
	public String showSignPointDate(HttpServletRequest request){
		try {
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            QueryAfterSales afterSales = (QueryAfterSales) JsonUtil.getObjectByJsonStr(jsonStr, QueryAfterSales.class);
            TokenCacheTO usr = authHelper.authToken(afterSales.getToken());
//            QueryUser userTO = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 用户签到 入参] = {}", JsonUtil.convertObjToStr(afterSales));
            }
            MResultVO<List<MemberSignPoint>> signResult = memberSignPointAO.showSignPointDate(usr.getUid());
            MResultVO<PointMemberResponse> result = new MResultVO<PointMemberResponse>( signResult.getCode(), signResult.getMsg());
            if (log.isInfoEnabled()) {
                log.info("[API接口 - 用户签到 返回值] = {}", JsonUtil.convertObjToStr(signResult));
                MResultVO<List<PointMember>> pointResult = memberSignPointAO.querytotalPointListByMemberId(usr.getUid());
                log.info("[API接口 - 用户签到 返回值] = {}", JsonUtil.convertObjToStr(pointResult));
                PointMemberResponse responseResult = new PointMemberResponse();
                responseResult.setSignResult(signResult.getData());
                responseResult.setPointResult(pointResult.getData());
                result = new MResultVO<PointMemberResponse>(signResult.getCode(), signResult.getMsg());
                result.setData(responseResult);
                return JsonUtil.convertObjToStr(result);
            }
            return JsonUtil.convertObjToStr(result);
		} catch (MobileException me) {
            log.error("[API接口 - 用户签到  MobileException] = {}", me.getMessage());
            log.error("[API接口 - 用户签到 返回值] = {}", JsonUtil.convertObjToStr(new MResultVO<>(me)));
            return JsonUtil.convertObjToStr(new MResultVO<>(me));
       }catch (Exception m1e) {
           log.error("[API接口 - 用户签到  MobileException] = {}", m1e.getMessage());
           log.error("[API接口 - 用户签到 返回值] = {}", JsonUtil.convertObjToStr(new MResultVO<>(m1e.getMessage())));
           return JsonUtil.convertObjToStr(new MResultVO<>(m1e.getMessage()));
      } 
	}
}
