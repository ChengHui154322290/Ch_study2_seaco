package com.tp.world.controller.order;

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

import com.tp.common.vo.PaymentConstant;
import com.tp.enums.common.PlatformEnum;
import com.tp.m.base.MResultVO;
import com.tp.m.base.Page;
import com.tp.m.enums.MResultInfo;
import com.tp.m.enums.OrderEnum;
import com.tp.m.exception.MobileException;
import com.tp.m.query.order.QueryOrder;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.AssertUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.order.BuyNowVO;
import com.tp.m.vo.order.OrderDetailVO;
import com.tp.m.vo.order.OrderVO;
import com.tp.m.vo.order.PayOrderLineVO;
import com.tp.m.vo.order.SubmitOrderInfoVO;
import com.tp.m.vo.pay.BasePayVO;
import com.tp.service.ord.IOrderRedeemItemService;
import com.tp.world.ao.order.OrderAO;
import com.tp.world.helper.AuthHelper;
import com.tp.world.helper.RequestHelper;


/**
 * 订单控制器
 * @author zhuss
 * @2016年1月7日 下午4:17:02
 */
@Controller
@RequestMapping("/order")
public class OrderController {

	private static final Logger log = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	private OrderAO orderAO;
	
	@Autowired
	private AuthHelper authHelper;
	@Autowired
	IOrderRedeemItemService  orderRedeemItemService;
	
	/**
	 * 获取提交订单页面的信息
	 * @return
	 */
	@RequestMapping(value="/submitinfo",method = RequestMethod.POST)
	@ResponseBody
	public String getSubmitInfo(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryOrder order = (QueryOrder) JsonUtil.getObjectByJsonStr(jsonStr, QueryOrder.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 获取提交订单页面的信息 入参] = {}",JsonUtil.convertObjToStr(order));
			}
			TokenCacheTO usr = authHelper.authToken(order.getToken());
			order.setUserid(usr.getUid());
			order.setReceiveTel(usr.getTel());
			order.setIp(RequestHelper.getIpAddr(request));
			order.setShopId(0L);
			MResultVO<SubmitOrderInfoVO> result = orderAO.getSubmitOrderInfo(order);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 获取提交订单页面的信息 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 获取提交订单页面的信息  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 获取提交订单页面的信息 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 提交订单
	 * @return
	 */
	@RequestMapping(value="/submit",method = RequestMethod.POST)
	@ResponseBody
	public String submit(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryOrder order = (QueryOrder) JsonUtil.getObjectByJsonStr(jsonStr, QueryOrder.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 提交订单 入参] = {}",JsonUtil.convertObjToStr(order));
			}
			AssertUtil.notBlank(order.getAid(), MResultInfo.ADDRESS_ID_NULL);
			TokenCacheTO usr = authHelper.authToken(order.getToken());
			order.setUserid(usr.getUid());
			order.setIp(RequestHelper.getIpAddr(request));
			order.setShopId(0L);
			MResultVO<List<PayOrderLineVO>> result = orderAO.submit(order,usr);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 提交订单 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 提交订单  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 提交订单 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 合并提交订单支付
	 * @return
	 */
	@RequestMapping(value="/mergesubmit",method = RequestMethod.POST)
	@ResponseBody
	public String mergeSubmit(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryOrder order = (QueryOrder) JsonUtil.getObjectByJsonStr(jsonStr, QueryOrder.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 合并提交订单支付 入参] = {}",JsonUtil.convertObjToStr(order));
			}
			
			if(StringUtils.isBlank(order.getReceiveTel())){
				AssertUtil.notBlank(order.getAid(), MResultInfo.ADDRESS_ID_NULL);
			}else{//团购券防止报错
					order.setAid("0"); 
			}
			AssertUtil.notBlank(order.getApptype(), MResultInfo.PLATFORM_NULL);
			AssertUtil.notScope(order.getPayway(), PaymentConstant.GATEWAY_TYPE.class,MResultInfo.PAYWAY_ERROR);
			if(StringUtil.equals(order.getApptype(), PlatformEnum.WAP.name())&&
					(StringUtil.equals(order.getPayway(), PaymentConstant.GATEWAY_TYPE.WEIXIN.code)||
							StringUtil.equals(order.getPayway(), PaymentConstant.GATEWAY_TYPE.WEIXIN_EXTERNAL.code)))
				AssertUtil.notBlank(order.getOpenid(), MResultInfo.OPENID_NULL);
			TokenCacheTO usr = authHelper.authToken(order.getToken());
			order.setUserid(usr.getUid());
			order.setIp(RequestHelper.getIpAddr(request));
//			/*替换为WeixinExternalPay test start*/
//
//			if(usr.getUid().equals(1L) && "weixin".equals(order.getPayway())){
//				order.setPayway(PaymentConstant.GATEWAY_TYPE.WEIXIN_EXTERNAL.code);
//			}
//			/*替换为WeixinExternalPay test end*/
			
			/* 2016.9.27安卓版本存在bug,临时应对保税仓货物测试首单 */
			if (StringUtil.equals(order.getApptype(), PlatformEnum.ANDROID.name()) && 
					PaymentConstant.GATEWAY_TYPE.ALIPAY.code.equals(order.getPayway())) {
				order.setPayway(PaymentConstant.GATEWAY_TYPE.MEGER_ALIPAY.code);
			}
			order.setShopId(0L);
			MResultVO<BasePayVO> result = orderAO.mergeSubmit(order,usr);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 合并提交订单支付 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 合并提交订单支付  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 合并提交订单支付 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 订单列表
	 * @return
	 */
	@RequestMapping(value="/list",method = RequestMethod.POST)
	@ResponseBody
	public String getOrderList(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryOrder order = (QueryOrder) JsonUtil.getObjectByJsonStr(jsonStr, QueryOrder.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 订单列表 入参] = {}",JsonUtil.convertObjToStr(order));
			}
			AssertUtil.notScope(order.getType(), OrderEnum.QueryType.class,MResultInfo.TYPE_ERROR);
			TokenCacheTO usr = authHelper.authToken(order.getToken());
			order.setUserid(usr.getUid());
			order.setShopId(0L);
			MResultVO<Page<OrderVO>> result = orderAO.getOrderList(order);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 订单列表 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 订单列表  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 订单列表 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 订单详情
	 * @return
	 */
	@RequestMapping(value="/detail",method = RequestMethod.POST)
	@ResponseBody
	public String detail(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryOrder order = (QueryOrder) JsonUtil.getObjectByJsonStr(jsonStr, QueryOrder.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 订单详情 入参] = {}",JsonUtil.convertObjToStr(order));
			}
			AssertUtil.notBlank(order.getOrdercode(), MResultInfo.ORDER_CODE_NULL);
			TokenCacheTO usr = authHelper.authToken(order.getToken());
			order.setUserid(usr.getUid());
			MResultVO<OrderDetailVO> result = orderAO.detail(order);
			if(StringUtils.isBlank(result.getData().getReceiveTel())){
				result.getData().setReceiveTel(usr.getTel());
			}
			if(log.isInfoEnabled()){
				log.info("[API接口 - 订单详情 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 订单详情  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 订单详情 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 取消和删除订单
	 * @return
	 */
	@RequestMapping(value="/calordel",method = RequestMethod.POST)
	@ResponseBody
	public String calordel(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryOrder order = (QueryOrder) JsonUtil.getObjectByJsonStr(jsonStr, QueryOrder.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 取消和删除订单 入参] = {}",JsonUtil.convertObjToStr(order));
			}
			AssertUtil.notBlank(order.getOrdercode(), MResultInfo.ORDER_CODE_NULL);
			AssertUtil.notScope(order.getType(), OrderEnum.CalOrDelType.class,MResultInfo.TYPE_ERROR);
			TokenCacheTO usr = authHelper.authToken(order.getToken());
			order.setUserid(usr.getUid());
			MResultVO<MResultInfo> result = orderAO.calordel(order,usr);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 取消和删除订单 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			//更新兑换码状态
			orderRedeemItemService.cancleRedeemInfo(Long.valueOf(order.getOrdercode()));
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 取消和删除订单  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 取消和删除订单 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 确认订单
	 * @return
	 */
	@RequestMapping(value="/confirm",method = RequestMethod.POST)
	@ResponseBody
	public String confirm(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryOrder order = (QueryOrder) JsonUtil.getObjectByJsonStr(jsonStr, QueryOrder.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 确认订单 入参] = {}",JsonUtil.convertObjToStr(order));
			}
			AssertUtil.notBlank(order.getOrdercode(), MResultInfo.ORDER_CODE_NULL);
			TokenCacheTO usr = authHelper.authToken(order.getToken());
			order.setUserid(usr.getUid());
			MResultVO<MResultInfo> result = orderAO.confirm(order);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 确认订单 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 确认订单  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 确认订单 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 立即购买
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value ="/buynow",method = RequestMethod.POST)
	public String buyNow(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryOrder order = (QueryOrder) JsonUtil.getObjectByJsonStr(jsonStr, QueryOrder.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 立即购买 入参] = {}",JsonUtil.convertObjToStr(order));
			}
			AssertUtil.notBlank(order.getTid(), MResultInfo.TOPIC_ID_NULL);
			AssertUtil.notBlank(order.getSku(), MResultInfo.ITEM_SKU_NULL);
			AssertUtil.notBlank(order.getCount(), MResultInfo.ITEM_COUNT_NULL);
			AssertUtil.notBlank(order.getRegcode(), MResultInfo.AREA_ID_NULL);
			TokenCacheTO usr = authHelper.authToken(order.getToken());
			order.setUserid(usr.getUid());
			order.setIp(RequestHelper.getIpAddr(request));
			MResultVO<BuyNowVO> result = orderAO.buyNow(order);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 立即购买 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 立即购买  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 立即购买 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
}
