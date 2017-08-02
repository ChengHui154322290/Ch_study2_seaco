package com.tp.world.controller.GroupCoupon;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.dto.ord.GroupCouponExchangeDto;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.query.promotion.QueryCoupon;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.AssertUtil;
import com.tp.m.util.JsonUtil;
import com.tp.model.ord.OrderRedeemItem;
import com.tp.world.ao.GroupCoupon.GroupCouponAO;
import com.tp.world.helper.AuthHelper;
import com.tp.world.helper.RequestHelper;

/**
 * 团购券兑换核销
 * @author szy
 *
 */
@Controller
@RequestMapping("/order/redeem/")
public class GroupCouponExchangeController {
	
	private static final Logger log = LoggerFactory.getLogger(GroupCouponExchangeController.class);
	
	@Autowired
	private GroupCouponAO groupCouponAO;
	@Autowired
	private AuthHelper authHelper;

	@RequestMapping(value="exchange",method = RequestMethod.POST)
	@ResponseBody
	public String exchange(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryCoupon coupon = (QueryCoupon) JsonUtil.getObjectByJsonStr(jsonStr, QueryCoupon.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 -兑换团购券 入参] = {}",JsonUtil.convertObjToStr(coupon));
			}
			AssertUtil.notBlank(coupon.getCcode(), MResultInfo.COUPON_CODE_NULL);
			TokenCacheTO usr = authHelper.authToken(coupon.getToken());
			
			GroupCouponExchangeDto groupCouponExchange = new GroupCouponExchangeDto();
			groupCouponExchange.setUserMobile(usr.getTel());
			groupCouponExchange.setUserName(usr.getName());
			if("2".equals(coupon.getSendtype())){
				MResultVO<String> result = groupCouponAO.enCode(coupon.getCcode());
				if(result.success()){
					groupCouponExchange.setExchangeCode(result.getData());
				}else{
					log.info("[API接口 -兑换团购券 返回值] = {}",JsonUtil.convertObjToStr(result));
					return JsonUtil.convertObjToStr(result);
				}
			}else{
				groupCouponExchange.setExchangeCode(coupon.getCcode());
			}
			MResultVO<OrderRedeemItem> result = groupCouponAO.exchange(groupCouponExchange);
			if(log.isInfoEnabled()){
				log.info("[API接口 -兑换团购券 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 兑换团购券  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 兑换团购券  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
}
