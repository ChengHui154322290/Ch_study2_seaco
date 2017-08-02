package com.tp.shop.controller.payment;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.query.pay.QueryPay;
import com.tp.m.query.pay.QueryPayway;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.AssertUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.pay.BasePayVO;
import com.tp.m.vo.pay.PayResultVO;
import com.tp.m.vo.pay.PaywayVO;
import com.tp.shop.ao.payment.PayAO;
import com.tp.shop.helper.AuthHelper;
import com.tp.shop.helper.RequestHelper;

/**
 * 支付控制器
 * @author zhuss
 * @2016年1月7日 下午4:17:48
 */
@Controller
@RequestMapping("/pay")
public class PayController {
	private static final Logger log = LoggerFactory.getLogger(PayController.class);

	@Autowired
	private PayAO payAO;
	
	@Autowired
	private AuthHelper authHelper;
	
	/**
	 * 支付方式列表
	 * @return
	 */
	@RequestMapping(value="/paywaylist",method = RequestMethod.GET)
	@ResponseBody
	public String getPaywayList(QueryPayway payway){
		try{
			if(log.isInfoEnabled()){
				log.info("[API接口 - 支付方式列表 入参] = {}",JsonUtil.convertObjToStr(payway));
			}
			AssertUtil.notBlank(payway.getOrdertype(), MResultInfo.ORDER_TYPE_NULL);
			AssertUtil.notBlank(payway.getChannelid(), MResultInfo.CHANNEL_NULL);
			MResultVO<List<PaywayVO>> result = payAO.getPaywayList(payway);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 支付方式列表 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 支付方式列表  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 支付方式列表 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 立即支付 :根据公共参数apptype区分wap和 app的支付
	 * 提交订单后的待支付列表：需要payid和payway
	 * 用户中心的待支付列表：需要ordercode和payway
	 * 
	 * @return
	 */
	@RequestMapping(value="/payorder",method = RequestMethod.POST)
	@ResponseBody
	public String paymentOrder(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryPay pay = (QueryPay) JsonUtil.getObjectByJsonStr(jsonStr, QueryPay.class);
			pay.setChannelcode(RequestHelper.getChannelCode(request));
			if(log.isInfoEnabled()){
				log.info("[API接口 - 立即支付 入参] = {}",JsonUtil.convertObjToStr(pay));
			}			
			AssertUtil.notBlank(pay.getApptype(), MResultInfo.PLATFORM_NULL);
			AssertUtil.notScope(pay.getPayway(), PaymentConstant.GATEWAY_TYPE.class,MResultInfo.PAYWAY_ERROR);
			if(StringUtil.equals(pay.getApptype(), PlatformEnum.WAP.name())&&
					(StringUtil.equals(pay.getPayway(), PaymentConstant.GATEWAY_TYPE.WEIXIN.code)||
							StringUtil.equals(pay.getPayway(), PaymentConstant.GATEWAY_TYPE.WEIXIN_EXTERNAL.code)))
				AssertUtil.notBlank(pay.getOpenid(), MResultInfo.OPENID_NULL);
			TokenCacheTO usr = authHelper.authToken(pay.getToken());
			pay.setUserid(usr.getUid());
			pay.setSdk(RequestHelper.isAPP(pay.getApptype()));
//			/*替换为WeixinExternalPay test start*/
//			if(usr.getUid().equals(1L) && "weixin".equals(pay.getPayway())){
//				pay.setPayway(PaymentConstant.GATEWAY_TYPE.WEIXIN_EXTERNAL.code);
//			}
//			/*替换为WeixinExternalPay test end*/

			MResultVO<BasePayVO> result = payAO.paymentOrder(pay);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 立即支付 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 立即支付  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 立即支付 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	

	/**
	 * 根据订单号或支付ID查看支付结果
	 * @return
	 */
	@RequestMapping(value="/payresult",method = RequestMethod.POST)
	@ResponseBody
	public String paymentResult(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryPay pay = (QueryPay) JsonUtil.getObjectByJsonStr(jsonStr, QueryPay.class);
			pay.setChannelcode(RequestHelper.getChannelCode(request));
			if(log.isInfoEnabled()){
				log.info("[API接口 - 查看支付结果 入参] = {}",JsonUtil.convertObjToStr(pay));
			}
			TokenCacheTO usr = authHelper.authToken(pay.getToken());
			pay.setUserid(usr.getUid());
			MResultVO<PayResultVO> result = payAO.paymentResult(pay);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 查看支付结果 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 查看支付结果  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 查看支付结果 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
}
