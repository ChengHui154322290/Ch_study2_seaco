package com.tp.world.controller.GroupCoupon;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.common.vo.FastConstant;
import com.tp.m.base.MResultVO;
import com.tp.m.exception.MobileException;
import com.tp.m.query.user.QueryUser;
import com.tp.m.query.user.ShopUserInfo;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.JsonUtil;
import com.tp.model.dss.FastUserInfo;
import com.tp.world.ao.GroupCoupon.FastUserInfoAO;
import com.tp.world.helper.AuthHelper;
import com.tp.world.helper.RequestHelper;

/**
 * 线下店铺人员管理
 * @author szy
 *
 */
@Controller
@RequestMapping("/order/redeem/user/")
public class ShopUserManagerController {

	private static final Logger log = LoggerFactory.getLogger(ShopUserManagerController.class);
	
	@Autowired
	private FastUserInfoAO fastUserInfoAO;
	@Autowired
	private AuthHelper authHelper;
	
	@RequestMapping(value="/list",method = RequestMethod.POST)
	@ResponseBody
	public String getUserInfoList(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryUser user = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 店员列表 入参] = {}",JsonUtil.convertObjToStr(user));
			}
			TokenCacheTO usr = authHelper.authToken(user.getToken());
			MResultVO<List<FastUserInfo>> result = fastUserInfoAO.queryUserInfoListByManagerMobile(usr.getTel());
			if(log.isInfoEnabled()){
				log.info("[API接口 - 店员列表 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 店员列表  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 店员列表 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	@RequestMapping(value="/save",method = RequestMethod.POST)
	@ResponseBody
	public String saveUserInfo(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			ShopUserInfo user = (ShopUserInfo) JsonUtil.getObjectByJsonStr(jsonStr, ShopUserInfo.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 店员信息 入参] = {}",JsonUtil.convertObjToStr(user));
			}
			TokenCacheTO usr = authHelper.authToken(user.getToken());
			FastUserInfo fastUserInfo =new FastUserInfo();
			fastUserInfo.parentUserMobile=usr.getTel();
			fastUserInfo.setUpdateUser(usr.getName());
			fastUserInfo.setMobile(user.getUserMoible());
			MResultVO<FastUserInfo> result = fastUserInfoAO.insertUserInfo(fastUserInfo);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 新增店员 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 新增店员  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 新增店员 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
		
	@RequestMapping(value="/delete",method = RequestMethod.POST)
	@ResponseBody
	public String deleteUserInfo(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			ShopUserInfo user = (ShopUserInfo) JsonUtil.getObjectByJsonStr(jsonStr, ShopUserInfo.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 删除店员 入参] = {}",JsonUtil.convertObjToStr(user));
			}
			TokenCacheTO usr = authHelper.authToken(user.getToken());
			FastUserInfo fastUserInfo =new FastUserInfo();
			fastUserInfo.parentUserMobile=usr.getTel();
			fastUserInfo.setUpdateUser(usr.getName());
			fastUserInfo.setFastUserId(user.getUserid());
			fastUserInfo.setShopType(FastConstant.SHOP_TYPE.GROUP_COUPON.code);
			MResultVO<Integer> result = fastUserInfoAO.deleteUserInfo(fastUserInfo);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 删除店员 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 删除店员  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 删除店员 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
}
