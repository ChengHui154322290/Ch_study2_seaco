package com.tp.world.ao.promotion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.mmp.CouponConstant;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.MyCouponDTO;
import com.tp.dto.mmp.OrderCouponDTO;
import com.tp.dto.mmp.enums.CouponSendType;
import com.tp.dto.mmp.enums.CouponType;
import com.tp.m.base.MResultVO;
import com.tp.m.base.Page;
import com.tp.m.enums.CouponEnum;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.query.promotion.QueryCoupon;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.coupon.CouponVO;
import com.tp.m.vo.coupon.OfflineCouponCodeVO;
import com.tp.model.mmp.Coupon;
import com.tp.model.mmp.CouponUser;
import com.tp.model.mmp.ExchangeCouponChannelCode;
import com.tp.proxy.mmp.CouponProxy;
import com.tp.proxy.mmp.CouponUserProxy;
import com.tp.proxy.mmp.ExchangeCouponChannelCodeProxy;
import com.tp.proxy.mmp.PointDetailProxy;
import com.tp.proxy.mmp.facade.CouponFacadeProxy;
import com.tp.proxy.ord.OrderCouponProxy;
import com.tp.world.convert.CouponConvert;
import com.tp.world.helper.RequestHelper;

/**
 * 优惠券业务层
 * @author zhuss
 * @2016年1月4日 下午6:55:20
 */
@Service
public class CouponAO {

	private static final Logger log=LoggerFactory.getLogger(CouponAO.class);
	
	@Autowired
	private CouponFacadeProxy couponFacadeProxy;
	
	@Autowired
	private CouponUserProxy couponUserProxy;
	
	@Autowired
	private OrderCouponProxy orderCouponProxy;
	
	@Autowired
	private CouponProxy couponProxy;
	@Autowired
	private ExchangeCouponChannelCodeProxy exchangeCouponChannelCodeProxy;
	@Autowired
	private PointDetailProxy pointDetailProxy;
	
	public MResultVO<?> exchange(QueryCoupon coupon){
		Map<String,Object> params = new HashMap<String,Object>();
		Number couponId=null;//兑换码批次
		if(StringUtils.isBlank(coupon.getCcode())){//现金券兑换西客币
			params.put("id", coupon.getCid());
			CouponUser couponUser=couponUserProxy.queryById(Long.valueOf(coupon.getCid())).getData();
			if (CouponConstant.COUPON_VALIDD!=couponUser.getStatus()){
				return new MResultVO<>("该现金券已无效");
			}else{
				couponId=couponUser.getBatchId();
				coupon.setCouponId(String.valueOf(couponId));
			}
			
		}else{
			params.put("exchangeCode", coupon.getCcode());
			ExchangeCouponChannelCode exchangeCouponChannelCode = exchangeCouponChannelCodeProxy.queryUniqueByParams(params).getData();
			if(null==exchangeCouponChannelCode){
				return new MResultVO<>("优惠券编码已使用");
			}else{
				couponId=exchangeCouponChannelCode.getCouponId();
				coupon.setCid(String.valueOf(couponId));
			}
		}
		
		
		
		Coupon couponDto = couponProxy.queryById(couponId).getData();
		if(CouponType.NO_CONDITION.ordinal()!=couponDto.getCouponType()){//不是现金券不能兑换成功积分
			coupon.setPoint(null);
		}
		if("point".equals(coupon.getPoint())){
			return exchangePoint(coupon);
		}else{
			return exchangeCoupon(coupon);
		}
	}
	
	/**
	 * 兑换优惠券
	 * @param coupon
	 * @return
	 */
	public MResultVO<?> exchangeCoupon(QueryCoupon coupon){
		Map<String,String> param = new HashMap<String,String>();
		param.put("type", "EXCHANGE_SUCCESS");
		try {
			ResultInfo<?> result = couponFacadeProxy.exchangeCouponsCode(CouponConvert.convertExchangeQuery(coupon));
			if(result.isSuccess())return new MResultVO<>(MResultInfo.EXCHANGE_SUCCESS,param);
			return new MResultVO<>(result.getMsg().getMessage());
		}catch (MobileException e) {
			log.error("[API接口 - 内部领取优惠券  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		} catch (Exception e) {
			log.error("[API接口 - 内部领取优惠券 Exception]={}", e);
			return new MResultVO<>(MResultInfo.EXCHANGE_FAILED);
		}
	}
	
	/**
	 * 兑换优惠券
	 * @param coupon
	 * @return
	 */
	public MResultVO<?> exchangePoint(QueryCoupon coupon){
		Map<String,String> param = new HashMap<String,String>();
		param.put("type", "EXCHANGE_POINT_SUCCESS");
		try {
			ResultInfo<?> result = pointDetailProxy.exchangePoint(CouponConvert.convertExchangeQuery(coupon));
			if(result.isSuccess())return new MResultVO<>(MResultInfo.EXCHANGE_POINT_SUCCESS,param);
			return new MResultVO<>(result.getMsg().getMessage());
		}catch (MobileException e) {
			log.error("[API接口 - 内部领取优惠券兑换积分  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		} catch (Exception e) {
			log.error("[API接口 - 内部领取优惠券兑换积分 Exception]={}", e);
			return new MResultVO<>(MResultInfo.EXCHANGE_FAILED);
		}
	}
	
	/**
	 * 领取优惠券：可以根据用户token领取，也可以是手机号(用户不需要登录,不过需要手机验证码)
	 * @param coupon
	 * @return
	 */
	public MResultVO<MResultInfo> receive(QueryCoupon coupon){
		try {
			ResultInfo result = couponFacadeProxy.receiveCoupon(CouponConvert.convertReceiveQuery(coupon));
			if(result.isSuccess()){
				return new MResultVO<>(MResultInfo.RECEIVE_SUCCESS);
			}
			return new MResultVO<>(result.getMsg().getMessage());
		}catch (MobileException e) {
			log.error("[API接口 - 领取优惠券  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		} catch (Exception e) {
			log.error("[API接口 - 领取优惠券 Exception]={}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
	
	/**
	 * 根据手机号获取多张优惠券
	 * @param coupon
	 * @return
	 */
	public MResultVO<MResultInfo> receiveCouponsByTel(QueryCoupon coupon){
		try {
			ResultInfo result = new ResultInfo();
			for(String code : coupon.getCcodes()){
				coupon.setCcode(code);
				result = couponFacadeProxy.receiveCoupon(CouponConvert.convertReceiveQuery(coupon));
				if(!result.isSuccess()){
					if(result.getMsg().getCode().intValue() != -3)break;
				}
			}
			if(result.isSuccess()){
				return new MResultVO<>(MResultInfo.RECEIVE_SUCCESS);
			}
			Integer code= result.getMsg().getCode();
			return new MResultVO<>(code == null?"-1":code.toString(),result.getMsg().getMessage());
		}catch (MobileException e) {
			log.error("[API接口 - 领取优惠券  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		} catch (Exception e) {
			log.error("[API接口 - 领取优惠券 Exception]={}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
	
	/**
	 * 优惠券列表
	 * @param coupon
	 * @return
	 */
	public MResultVO<Page<CouponVO>> couponList(QueryCoupon coupon){
		try {
			//移动端个人中心返回优惠券或者红包不要翻页，最简单的办法是设置一个大的page size
			if(coupon.getType().equals(CouponEnum.ListFromType.LIST_FROM_USER.code)){//个人中心
				return queryCouponListByUser(coupon);
			}else if(coupon.getType().equals(CouponEnum.ListFromType.LIST_FROM_CAN_RECEIVE.code)){//可领取优惠券列表
					return new MResultVO<>("可领取优惠券 功能暂不提供 敬请期待!");
			}else if(coupon.getType().equals(CouponEnum.ListFromType.LIST_FROM_ORDER.code)){//订单
				return queryCouponListByOrder(coupon);
			}
			return new MResultVO<>(MResultInfo.FAILED);
		} catch (MobileException e) {
			log.error("[API接口 - 优惠券列表 MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		}catch (Exception e) {
			log.error("[API接口 - 优惠券列表 Exception]={}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
	
	/**
	 * 用户中心的优惠券列表
	 * @return
	 */
	public MResultVO<Page<CouponVO>> queryCouponListByUser(QueryCoupon coupon){
		ResultInfo<PageInfo<MyCouponDTO>> myCL = couponFacadeProxy.myCoupon(CouponConvert.convertCouponQuery(coupon));
		if(myCL.isSuccess()){
			PageInfo<MyCouponDTO> clist= myCL.getData();
			return new MResultVO<>(MResultInfo.SUCCESS,CouponConvert.convertCouponList(clist));
		}
		log.error("[调用Service接口 - 用户中心优惠券列表(myCoupon) FAILED] = {}",myCL.getMsg().toString());
		return new MResultVO<>(myCL.getMsg().getMessage());
	}
	
	/**
	 * 提交订单页面的可用优惠券列表
	 * @return
	 */
	public MResultVO<Page<CouponVO>> queryCouponListByOrder(QueryCoupon coupon){
		//Map<String, List<CouponDTO>>key=优惠券类型 0 : 满减券 1：现金券 ，value=用户的优惠券列表 CartConstant.TYPE_SEA
		ResultInfo<Map<String, List<OrderCouponDTO>>> res = orderCouponProxy.queryOrderCoupon(coupon.getUserid(), coupon.getUuid(), RequestHelper.getPlatformByName(coupon.getApptype()).code,0L);
		if(res.isSuccess()){
			Map<String, List<OrderCouponDTO>> ocm = res.getData();
			return new MResultVO<>(MResultInfo.SUCCESS,CouponConvert.convertCouponList(ocm));
		}
		log.error("[调用Service接口 - 提交订单页面的可用优惠券列表(myCoupon) FAILED] = {}",res.getMsg().toString());
		return new MResultVO<>(res.getMsg().getMessage());
	}
	
	/**
	 * 提交订单页面的可用优惠券总数
	 * @return
	 */
	public MResultVO<Integer> queryOrderCouponCount(QueryCoupon coupon){
		//Map<String, List<CouponDTO>>key=优惠券类型 0 : 满减券 1：现金券 ，value=用户的优惠券列表 CartConstant.TYPE_SEA
		ResultInfo<Integer> res = orderCouponProxy.queryOrderCouponCount(coupon.getUserid(), coupon.getUuid(), RequestHelper.getPlatformByName(coupon.getApptype()).code,0L);
		if(res.isSuccess()){
			Integer ocm = res.getData();
			return new MResultVO<>(MResultInfo.SUCCESS,ocm);
		}
		log.error("[调用Service接口 - 提交订单页面的可用优惠券总数(myCoupon) FAILED] = {}",res.getMsg().toString());
		return new MResultVO<>(res.getMsg().getMessage());
	}
	
	
	/**
	 * 首次操作获取优惠券:1 新用户自动发放2 分享自动发放 
	 * @param coupon
	 * @return
	 */
	public MResultVO<MResultInfo> receiveOnly(QueryCoupon coupon){
		try {
			CouponSendType couponSendType = CouponSendType.parse(StringUtil.getIntegerByStr(coupon.getSendtype()));
			ResultInfo<String> result = couponUserProxy.sendAutoCouponBySendType(coupon.getTel(),couponSendType);
			if(result.isSuccess()){
				return new MResultVO<>(MResultInfo.RECEIVE_SUCCESS);
			}
			return new MResultVO<>(result.getMsg().getMessage());
		}catch (MobileException e) {
			log.error("[API接口 - 首次操作获取优惠券  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		} catch (Exception e) {
			log.error("[API接口 - 首次操作获取优惠券 Exception]={}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
	
	/**
	 * 获取线下推广优惠码
	 * @param uid
	 * @return
	 */
	public MResultVO<OfflineCouponCodeVO> getOfflineCouponCode(Long uid) {
		try{
			String code = couponProxy.getOfflineCouponCode(String.format("%08d", uid));
			//String qrcodeImg = activityCacheHelper.getQrcodeCache();
			return new MResultVO<>(MResultInfo.SUCCESS,new OfflineCouponCodeVO(code,""));
		}catch(MobileException ex){
			log.error("[API接口 - 获取线下推广优惠码] = {}",ex);
			return new MResultVO<>(ex);
		}catch(Exception ex){
			log.error("[API接口 - 获取线下推广优惠码] = {}",ex);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
}
