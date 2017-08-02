package com.tp.shop.controller.promotion;

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
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.JsonUtil;
import com.tp.m.vo.point.PointMemberVO;
import com.tp.shop.ao.promotion.PointDetailAO;
import com.tp.shop.helper.AuthHelper;
import com.tp.shop.helper.RequestHelper;

/**
 * 积分（西客币）会员管理
 * @author szy
 *
 */
@Controller
@RequestMapping("/point")
public class PointMemberController{

	private static final Logger log = LoggerFactory.getLogger(PointMemberController.class);

	@Autowired
	private AuthHelper authHelper;
	@Autowired
	private PointDetailAO pointDetailAO;
	
	@RequestMapping(value="/list", method=RequestMethod.POST)
	@ResponseBody
	public String pointDetailList(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryUser queryUser = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
			if(log.isInfoEnabled()){
				log.info("[API接口- 积分详情 入参] = {}",JsonUtil.convertObjToStr(queryUser));
			}
			TokenCacheTO usr = authHelper.authToken(queryUser.getToken());
			queryUser.setUserid(usr.getUid());
			MResultVO<PointMemberVO> result = pointDetailAO.queryPointDetailByMemberId(queryUser);
			if(log.isInfoEnabled()){
				log.info("[API接口 积分详情 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 积分详情  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 积分详情  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
}
