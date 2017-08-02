/**
 * 
 */
package com.tp.proxy.ord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.SubOrder;
import com.tp.model.ord.JKF.JkfCallbackResponse;
import com.tp.model.ord.JKF.JkfCancelOrderResult;
import com.tp.model.ord.JKF.JkfGoodsDeclarResult;
import com.tp.model.ord.JKF.JkfReceiptResult;
import com.tp.model.ord.JKF.JkfTaxIsNeedResult;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.customs.ISeaOrderDeliveryLocalService;
import com.tp.service.ord.customs.JKF.IJKFCallbackService;
import com.tp.service.ord.directOrder.IDirectOrderService;

/**
 * @author Administrator
 *
 */
@Service
public class JKFServiceProxy {
	
	private static final Logger logger = LoggerFactory.getLogger(JKFServiceProxy.class);
	
	@Autowired
	private IJKFCallbackService jkfCallbackService;  
	
	@Autowired
	private ISeaOrderDeliveryLocalService seaOrderDeliveryLocalService;
	
	@Autowired
	private ISubOrderService subOrderService;
	
	@Autowired
	private IDirectOrderService directOrderService;
	
	//供后台测试用
	public ResultInfo<Boolean> pushSingleSubOrder4Backend(Long orderCode){
		SubOrder subOrder = subOrderService.selectOneByCode(orderCode);
		if (subOrder == null) {
			return new ResultInfo<>(new FailInfo("订单不存在"));
		}
		return seaOrderDeliveryLocalService.declareSeaOrderToCustoms(subOrder);
	}
	//推送海外直邮订单 ,供后台测试用
	public ResultInfo<Boolean> pushSingleDirectSubOrder4Backend(Long orderCode){
		SubOrder subOrder = subOrderService.selectOneByCode(orderCode);
		if (subOrder == null) {
			return new ResultInfo<>(new FailInfo("订单不存在"));
		}
		return directOrderService.pushDirectOrderToCustoms(subOrder);
	}
		
	public ResultInfo<JkfCallbackResponse> customsDeclareCallback(JkfReceiptResult receiptResult){
		try {
			return new ResultInfo<>(jkfCallbackService.customsDeclareCallback(receiptResult));
		} catch (Exception e) {
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(e), logger, receiptResult);
			return new ResultInfo<JkfCallbackResponse>(failInfo);
		}
	}
	
	public ResultInfo<JkfCallbackResponse> goodsDeclCallback(JkfGoodsDeclarResult declResult){
		try {
			return new ResultInfo<>(jkfCallbackService.goodsDeclareCallback(declResult));
		} catch (Exception e) {
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(e), logger, declResult);
			return new ResultInfo<JkfCallbackResponse>(failInfo);
		}
	}
	
	public ResultInfo<JkfCallbackResponse> taxIsNeedCallback(JkfTaxIsNeedResult result){
		try {
			return new ResultInfo<>(jkfCallbackService.taxIsNeedCallback(result));
		} catch (Exception e) {
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(e), logger, result);
			return new ResultInfo<JkfCallbackResponse>(failInfo);
		}
	}
	
	public ResultInfo<JkfCallbackResponse> cancelOrderCallback(JkfCancelOrderResult result){
		try {
			return new ResultInfo<>(jkfCallbackService.cancelOrderCallback(result));
		} catch (Exception e) {
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(e), logger, result);
			return new ResultInfo<JkfCallbackResponse>(failInfo);
		}
	}	
}
