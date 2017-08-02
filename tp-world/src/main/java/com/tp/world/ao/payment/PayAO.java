package com.tp.world.ao.payment;

import java.util.HashMap;
import java.util.List;

import com.tp.common.vo.PaymentConstant;
import com.tp.dto.pay.AppPayData;
import com.tp.exception.ServiceException;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.query.pay.QueryPay;
import com.tp.m.query.pay.QueryPayway;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.pay.BasePayVO;
import com.tp.m.vo.pay.PayResultVO;
import com.tp.m.vo.pay.PaywayVO;
import com.tp.model.ord.OrderRedeemItem;
import com.tp.model.pay.PaymentGateway;
import com.tp.model.pay.PaymentInfo;
import com.tp.proxy.ord.OrderRedeemItemProxy;
import com.tp.proxy.pay.AppPaymentProxy;
import com.tp.proxy.pay.PayServiceProxy;
import com.tp.proxy.pay.PaymentGatewayProxy;
import com.tp.query.pay.AppPaymentCallDto;
import com.tp.world.convert.PayConvert;
import com.tp.world.helper.OrderHelper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 物流业务层
 * @author zhuss
 * @2016年1月7日 下午9:02:57
 */
@Service
public class PayAO {

	private static final Logger log = LoggerFactory.getLogger(PayAO.class);
	
	@Autowired
	private PayServiceProxy payServiceProxy;
	
	@Autowired
	private AppPaymentProxy appPaymentProxy;
	
	@Autowired
	private PaymentGatewayProxy paymentGatewayProxy;

	@Autowired
	private OrderHelper orderHelper;
	
	@Autowired
	private OrderRedeemItemProxy orderRedeemItemProxy;
	

	/**
	 * 获取支付方式列表
	 * @param pay
	 * @return
	 */
	public MResultVO<List<PaywayVO>> getPaywayList(QueryPayway payway){
		try{
			List<PaymentGateway> ways = paymentGatewayProxy.queryPaymentGateWayLists(StringUtil.getLongByStr(payway.getOrdertype()),StringUtil.getLongByStr(payway.getChannelid()));
			return new MResultVO<>(MResultInfo.SUCCESS,PayConvert.convertPaywayList(ways,payway));
		}catch (MobileException e) {
			log.error("[API接口 - 获取支付方式列表  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		}catch(ServiceException ex){
			log.error("[API接口 - 获取支付方式列表 ServiceException] = {}",ex.getMessage());
			return new MResultVO<>(ex.getMessage());
		}catch(Exception ex){
			log.error("[API接口 - 获取支付方式列表 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.SYSTEM_ERROR);
		}
	}
	
	/**
	 * 立即支付
	 * @param pay
	 * @return
	 */
	public MResultVO<BasePayVO> paymentOrder(QueryPay pay){
		try{
			PaymentInfo info = new PaymentInfo();
			if(StringUtil.isNotBlank(pay.getPayid())){
				info = payServiceProxy.queryPaymentInfoById(StringUtil.getLongByStr(pay.getPayid()));
			}else if(StringUtil.isNotBlank(pay.getOrdercode())&&StringUtil.isBlank(pay.getPayid())){
				info = payServiceProxy.queryPaymentInfoByBizCode(pay.getOrdercode());
				pay.setPayid(StringUtil.getStrByObj(info.getPaymentId()));
			}else return new MResultVO<>(MResultInfo.PARAM_ERROR); 
			info.setGatewayCode(pay.getPayway());
			//检验订单是否满足支付条件
			PayConvert.checkPayOrder(info,pay.getUserid());

			if(StringUtils.equals(pay.getPayway(),PaymentConstant.GATEWAY_TYPE.WEIXIN.code)) {
				boolean hasCOMMON_SEA = orderHelper.hasCOMMON_SEA(info);
				if(hasCOMMON_SEA){
					pay.setPayway(PaymentConstant.GATEWAY_TYPE.WEIXIN_EXTERNAL.code);
				}
			}
			AppPaymentCallDto callDto = PayConvert.convertPayQuery(pay);

			if(callDto.getParams()==null){
				callDto.setParams(new HashMap<String,Object>());
			}
			callDto.getParams().put("sysSource","world");

			AppPayData payData = appPaymentProxy.getAppData(callDto);
			if(pay.isSdk())return new MResultVO<>(MResultInfo.SUCCESS,PayConvert.convertAppPayInfo(payData, info,pay.getApptype()));
			else return new MResultVO<>(MResultInfo.SUCCESS,PayConvert.convertWapPayInfo(payData, info));
		}catch(MobileException ex){
			log.error("[API接口 - 立即支付 MobileException] = {}",ex.getMessage());
			return new MResultVO<>(ex);
		}catch(ServiceException ex){
			log.error("[API接口 - 立即支付 ServiceException] = {}",ex.getMessage());
			return new MResultVO<>(ex.getMessage());
		}catch(Exception ex){
			log.error("[API接口 - 立即支付 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.PAYINFO_NULL);
		}
	}
	
	/**
	 * 查看支付结果
	 * @param pay
	 * @return
	 */
	public MResultVO<PayResultVO> paymentResult(QueryPay pay){
		try{
			PaymentInfo info = new PaymentInfo();
			if(StringUtil.isNotBlank(pay.getPayid())){
				info = payServiceProxy.queryPaymentInfoById(StringUtil.getLongByStr(pay.getPayid()));
			}else if(StringUtil.isNotBlank(pay.getOrdercode())&&StringUtil.isBlank(pay.getPayid())){
				info = payServiceProxy.queryPaymentInfoByBizCode(pay.getOrdercode());
			}
			List<OrderRedeemItem>  OrderRedeemItems=orderRedeemItemProxy.getOrderRedeemItemByOrderCode(Long.valueOf(pay.getOrdercode()));
			return new MResultVO<>(MResultInfo.SUCCESS,PayConvert.convertPayResult(info,OrderRedeemItems));
		}catch(MobileException ex){
			log.error("[API接口 - 查看支付结果 MobileException] = {}",ex.getMessage());
			return new MResultVO<>(ex);
		}catch(ServiceException ex){
			log.error("[API接口 - 查看支付结果 ServiceException] = {}",ex.getMessage());
			return new MResultVO<>(ex.getMessage());
		}catch(Exception ex){
			log.error("[API接口 - 查看支付结果 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.SYSTEM_ERROR);
		}
	}
}
