package com.tp.world.controller.promotion;

import java.util.ArrayList;
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

import com.tp.common.vo.mem.SessionKey;
import com.tp.m.base.MResultVO;
import com.tp.m.base.Page;
import com.tp.m.enums.CouponEnum;
import com.tp.m.enums.MResultInfo;
import com.tp.m.enums.ValidFieldType;
import com.tp.m.exception.MobileException;
import com.tp.m.query.promotion.QueryCoupon;
import com.tp.m.query.user.QueryUser;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.AssertUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.coupon.CouponCountVO;
import com.tp.m.vo.coupon.CouponVO;
import com.tp.m.vo.coupon.OfflineCouponCodeVO;
import com.tp.world.ao.promotion.CouponAO;
import com.tp.world.helper.AuthHelper;
import com.tp.world.helper.RequestHelper;
import com.tp.world.helper.cache.SMSCacheHelper;

/**
 * 优惠券控制器
 * @author zhuss
 * @2016年1月4日 下午8:46:41
 */
@Controller
@RequestMapping("/coupon")
public class CouponController {
	
	private static final Logger log = LoggerFactory.getLogger(CouponController.class);
	
	@Autowired
	private CouponAO couponAO;
	
	@Autowired
	private AuthHelper authHelper;
	
	@Autowired
	private SMSCacheHelper sMSCacheHelper;

	/**
	 * 兑换优惠券
	 * @param topic
	 * @return
	 */
	@RequestMapping(value="/exchange", method=RequestMethod.POST)
	@ResponseBody
	public String exchange(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryCoupon coupon = (QueryCoupon) JsonUtil.getObjectByJsonStr(jsonStr, QueryCoupon.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 -兑换优惠券 入参] = {}",JsonUtil.convertObjToStr(coupon));
			}
			if(StringUtils.isBlank(coupon.getCid())){
				AssertUtil.notBlank(coupon.getCcode(), MResultInfo.COUPON_CODE_NULL);
			}
			
			TokenCacheTO usr = authHelper.authToken(coupon.getToken());
			coupon.setTel(usr.getTel());
			coupon.setUserid(usr.getUid());
			MResultVO<?> result = couponAO.exchange(coupon);
			if(log.isInfoEnabled()){
				log.info("[API接口 -兑换优惠券 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 兑换优惠券  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 兑换优惠券  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	
	
	
	/**
	 * 领取优惠券:可以根据用户token领取，也可以是手机号(用户不需要登录)
	 * @param topic
	 * @return
	 */
	@RequestMapping(value="/receive", method=RequestMethod.POST)
	@ResponseBody
	public String receive(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryCoupon coupon = (QueryCoupon) JsonUtil.getObjectByJsonStr(jsonStr, QueryCoupon.class);
			if(log.isInfoEnabled()){
				log.info("[API接口- 领取优惠券 入参] = {}",JsonUtil.convertObjToStr(coupon));
			}
			AssertUtil.notBlank(coupon.getCcode(), MResultInfo.COUPON_CODE_NULL);
			if(StringUtil.isBlank(coupon.getTel()) && StringUtil.isBlank(coupon.getToken())) throw new MobileException(MResultInfo.PARAM_ERROR);
			if(StringUtil.isNotBlank(coupon.getTel())){
				AssertUtil.notValid(coupon.getTel(), ValidFieldType.TELEPHONE);
				AssertUtil.notValid(coupon.getCaptcha(), ValidFieldType.CAPTCHA);
				sMSCacheHelper.compareCode(coupon.getTel(), SessionKey.RECEIVE_COUPON, coupon.getCaptcha());
			}
			if(StringUtil.isNotBlank(coupon.getToken())){
				TokenCacheTO usr = authHelper.authToken(coupon.getToken());
				coupon.setUserid(usr.getUid());
			}
			MResultVO<MResultInfo> result = couponAO.receive(coupon);
			if(log.isInfoEnabled()){
				log.info("[API接口 -领取优惠券 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 领取优惠券  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 领取优惠券  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 领取多张优惠券
	 * @param topic
	 * @return
	 */
	@RequestMapping(value="/receivemany", method=RequestMethod.POST)
	@ResponseBody
	public String receivemany(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryCoupon coupon = (QueryCoupon) JsonUtil.getObjectByJsonStr(jsonStr, QueryCoupon.class);
			if(log.isInfoEnabled()){
				log.info("[API接口- 领取多张优惠券 入参] = {}",JsonUtil.convertObjToStr(coupon));
			}
			AssertUtil.notEmpty(coupon.getCcodes(), MResultInfo.COUPON_CODE_NULL);
			AssertUtil.notValid(coupon.getTel(), ValidFieldType.TELEPHONE);
			AssertUtil.notValid(coupon.getCaptcha(), ValidFieldType.CAPTCHA);
			if(coupon.getCcodes().size() > 5) throw new MobileException(MResultInfo.PARAM_ERROR);
			sMSCacheHelper.compareCode(coupon.getTel(), SessionKey.RECEIVE_COUPON, coupon.getCaptcha());
			MResultVO<MResultInfo> result = couponAO.receiveCouponsByTel(coupon);
			if(log.isInfoEnabled()){
				log.info("[API接口 -领取多张优惠券 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 领取多张优惠券  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 领取多张优惠券  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 优惠券列表
	 * 当type为1的时候表示获取的是 用户中心的优惠券列表，此时的status是必传字段;
	 * 当type为3的时候表示提交订单页面选择可用优惠券，如果是通过立即购买进入的提交订单页面需要传入uuid，其他不需要传
	 * @param topic
	 * @return
	 */
	@RequestMapping(value="/list", method=RequestMethod.POST)
	@ResponseBody
	public String couponList(HttpServletRequest request){
		String optName = "";
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryCoupon coupon = (QueryCoupon) JsonUtil.getObjectByJsonStr(jsonStr, QueryCoupon.class);
			AssertUtil.notScope(coupon.getType(), CouponEnum.ListFromType.class,MResultInfo.TYPE_ERROR);
			optName = CouponEnum.ListFromType.getDescByCode(coupon.getType());
			if(log.isInfoEnabled()){
				log.info("[API接口- {}优惠券列表 入参] = {}",optName,JsonUtil.convertObjToStr(coupon));
			}
			if(coupon.getType().equals(CouponEnum.ListFromType.LIST_FROM_USER.code)){//个人中心 - 验证查询状态
				AssertUtil.notScope(coupon.getStatus(), CouponEnum.Status.class,MResultInfo.TYPE_ERROR);
			}
			TokenCacheTO usr = authHelper.authToken(coupon.getToken());
			coupon.setUserid(usr.getUid());
			if(StringUtils.isBlank(coupon.getUuid())){
				coupon.setUuid(coupon.getToken());
			}
			MResultVO<Page<CouponVO>> result = couponAO.couponList(coupon);
			List<CouponVO> couponVOList =result.getData().getList();
			List<CouponVO> couponCanChangeXgMoneyVOList=new ArrayList<CouponVO>();
			List<CouponVO> couponNotCanChangeXgMoneyVOList=new ArrayList<CouponVO>();
			List<CouponVO> couponNewOrderVOList=new ArrayList<CouponVO>();
			for(int i=0,len=couponVOList.size();i<len;i++){
				CouponVO couponVO=couponVOList.get(i);
				if("1".equals(couponVO.getExchangeXgMoney())){//允许兑换西客币
					couponCanChangeXgMoneyVOList.add(couponVO);
				}else{
					couponNotCanChangeXgMoneyVOList.add(couponVO);
				}
				
			}
			couponNewOrderVOList.addAll(couponCanChangeXgMoneyVOList);
			couponNewOrderVOList.addAll(couponNotCanChangeXgMoneyVOList);
			result.getData().setList(couponNewOrderVOList);
			if(log.isInfoEnabled()){
				log.info("[API接口 -{}优惠券列表 返回值] = {}",optName,JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - {}优惠券列表  MobileException] = {}",optName,me.getMessage());
			log.error("[API接口 - {}优惠券列表  返回值] = {}",optName,JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 优惠券数量
	 * 当type为1的时候表示获取的是 用户中心的优惠券列表，此时的status是必传字段;
	 * 当type为3的时候表示提交订单页面选择可用优惠券，如果是通过立即购买进入的提交订单页面需要传入uuid，其他不需要传
	 * @param topic
	 * @return
	 */
	@RequestMapping(value="/queryordercouponcount", method=RequestMethod.POST)
	@ResponseBody
	public String queryOrderCouponCount(HttpServletRequest request){
		String optName = "";
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryCoupon coupon = (QueryCoupon) JsonUtil.getObjectByJsonStr(jsonStr, QueryCoupon.class);
			AssertUtil.notScope(coupon.getType(), CouponEnum.ListFromType.class,MResultInfo.TYPE_ERROR);
			optName = CouponEnum.ListFromType.getDescByCode(coupon.getType());
			if(log.isInfoEnabled()){
				log.info("[API接口- {}优惠券列表 入参] = {}",optName,JsonUtil.convertObjToStr(coupon));
			}
			if(coupon.getType().equals(CouponEnum.ListFromType.LIST_FROM_USER.code)){//个人中心 - 验证查询状态
				AssertUtil.notScope(coupon.getStatus(), CouponEnum.Status.class,MResultInfo.TYPE_ERROR);
			}
			TokenCacheTO usr = authHelper.authToken(coupon.getToken());
			coupon.setUserid(usr.getUid());
			if(StringUtils.isBlank(coupon.getUuid())){
				coupon.setUuid(coupon.getToken());
			}
			MResultVO<Integer> result = couponAO.queryOrderCouponCount(coupon);
			boolean isApp = RequestHelper.isAPP(coupon.getApptype());
			if(isApp && result.success()){
				return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.SUCCESS,new CouponCountVO(StringUtil.getStrByObj(result.getData()))));
			}
			if(log.isInfoEnabled()){
				log.info("[API接口 -{}订单结算时优惠券总数 返回值] = {}",optName,JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - {}订单结算时优惠券总数  MobileException] = {}",optName,me.getMessage());
			log.error("[API接口 - {}订单结算时优惠券总数  返回值] = {}",optName,JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 首次操作获取优惠券:1 新用户自动发放2 分享自动发放 
	 * @param topic
	 * @return
	 */
	@RequestMapping(value="/receiveonly", method=RequestMethod.POST)
	@ResponseBody
	public String receiveOnly(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryCoupon coupon = (QueryCoupon) JsonUtil.getObjectByJsonStr(jsonStr, QueryCoupon.class);
			if(log.isInfoEnabled()){
				log.info("[API接口- 首次操作获取优惠券 入参] = {}",JsonUtil.convertObjToStr(coupon));
			}
			AssertUtil.notBlank(coupon.getSendtype(), MResultInfo.TYPE_NULL);
			TokenCacheTO usr = authHelper.authToken(coupon.getToken());
			coupon.setTel(usr.getTel());
			MResultVO<MResultInfo> result = couponAO.receiveOnly(coupon);
			if(log.isInfoEnabled()){
				log.info("[API接口 -首次操作获取优惠券 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 首次操作获取优惠券  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 首次操作获取优惠券  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 获取线下活动兑换码
	 * @return
	 */
	@RequestMapping(value="/offlinecouponcode",method = RequestMethod.POST)
	@ResponseBody
	public String getOfflineCouponCode(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryUser userTO = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 获取线下优惠编码 入参] = {}",JsonUtil.convertObjToStr(userTO));
			}
			TokenCacheTO usr = authHelper.authToken(userTO.getToken());
			MResultVO<OfflineCouponCodeVO> result = couponAO.getOfflineCouponCode(usr.getUid());
			if(log.isInfoEnabled()){
				log.info("[API接口 - 获取线下优惠编码 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 获取线下优惠编码  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 获取线下优惠编码 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
}
