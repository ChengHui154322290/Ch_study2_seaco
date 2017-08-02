package com.tp.m.controller.GroupCoupon;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.common.vo.PageInfo;
import com.tp.m.ao.GroupCoupon.OrderRedeemItemAO;
import com.tp.m.base.MResultVO;
import com.tp.m.base.OptionVO;
import com.tp.m.exception.MobileException;
import com.tp.m.helper.AuthHelper;
import com.tp.m.helper.RequestHelper;
import com.tp.m.query.order.QueryRedeemItem;
import com.tp.m.query.user.QueryUser;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.JsonUtil;
import com.tp.model.ord.OrderRedeemItem;
import com.tp.result.ord.OrderRedeemItemStatistics;

/**
 * 团购券统计
 * @author szy
 *
 */
@Controller
@RequestMapping("/order/redeem/")
public class GroupCouponController {

	private static final Logger log = LoggerFactory.getLogger(GroupCouponExchangeController.class);
	@Autowired
	private OrderRedeemItemAO orderRedeemItemAO;
	@Autowired
	private AuthHelper authHelper;
	
	@RequestMapping(value="list",method = RequestMethod.POST)
	@ResponseBody
	public String getRedeemPage(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryRedeemItem redeemItemQuery = (QueryRedeemItem) JsonUtil.getObjectByJsonStr(jsonStr, QueryRedeemItem.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 团购券列表 入参] = {}",JsonUtil.convertObjToStr(redeemItemQuery));
			}
			TokenCacheTO usr = authHelper.authToken(redeemItemQuery.getToken());
			redeemItemQuery.setMobile(usr.getTel());
			 MResultVO<PageInfo<OrderRedeemItem>> result = orderRedeemItemAO.queryListByParam(redeemItemQuery);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 团购券列表 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 团购券列表  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 团购券列表 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	@RequestMapping(value="count",method = RequestMethod.POST)
	@ResponseBody
	public String queryStatisticsByQuery(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryRedeemItem redeemItemQuery = (QueryRedeemItem) JsonUtil.getObjectByJsonStr(jsonStr, QueryRedeemItem.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 统计团购券列表 入参] = {}",JsonUtil.convertObjToStr(redeemItemQuery));
			}
			TokenCacheTO usr = authHelper.authToken(redeemItemQuery.getToken());
			redeemItemQuery.setMobile(usr.getTel());
			MResultVO<OrderRedeemItemStatistics> result = orderRedeemItemAO.queryStatisticsByQuery(redeemItemQuery);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 统计团购券列表 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 统计团购券列表  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 统计团购券列表 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	@RequestMapping(value="param/skulist",method = RequestMethod.POST)
	@ResponseBody
	public String queryBySkuListByShop(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryUser queryUser = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 查询条件团购项目 入参] = {}",JsonUtil.convertObjToStr(queryUser));
			}
			TokenCacheTO usr = authHelper.authToken(queryUser.getToken());
			queryUser.setShopMobile(usr.getTel());
			MResultVO<List<OptionVO>> result = orderRedeemItemAO.queryBySkuListByShop(usr.getTel());
			if(log.isInfoEnabled()){
				log.info("[API接口 - 查询条件团购项目 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 查询条件团购项目  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 查询条件团购项目 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	@RequestMapping(value="param/codestatus",method = RequestMethod.POST)
	@ResponseBody
	public String queryByRedeemStatusListByShop(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryUser queryUser = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 查询条件兑换券状态 入参] = {}",JsonUtil.convertObjToStr(queryUser));
			}
			TokenCacheTO usr = authHelper.authToken(queryUser.getToken());
			queryUser.setShopMobile(usr.getTel());
			MResultVO<List<OptionVO>> result = orderRedeemItemAO.convartRedeemCodeStateList();
			if(log.isInfoEnabled()){
				log.info("[API接口 - 查询条件兑换券状态 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 查询条件兑换券状态  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 查询条件兑换券状态 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
}
