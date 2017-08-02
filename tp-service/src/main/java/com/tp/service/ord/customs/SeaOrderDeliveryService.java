/**
 * 
 */
package com.tp.service.ord.customs;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.OrderConstant.PutCustomsStatus;
import com.tp.common.vo.OrderConstant.PutCustomsType;
import com.tp.common.vo.bse.ClearanceChannelsEnum;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.CustomsClearanceLog;
import com.tp.model.ord.SubOrder;
import com.tp.service.ord.ICustomsClearanceLogService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.customs.ISeaCustomsInfoDeliveryService;
import com.tp.service.ord.customs.JKF.IJKFDeclareOrderLocalService;

/**
 * @author Administrator
 * 订单申报
 */
@Service
public class SeaOrderDeliveryService implements ISeaCustomsInfoDeliveryService{

	private static final Logger logger = LoggerFactory.getLogger(SeaOrderDeliveryService.class);
	
	@Autowired
	private IJKFDeclareOrderLocalService jkfDeclareOrderLocalService;
	
	@Autowired
	private ISubOrderService subOrderService;
	
	@Autowired
	private ICustomsClearanceLogService customsClearanceLogService;
	
	@Override
	public ResultInfo<Boolean> delivery(SubOrder subOrder) {
		ResultInfo<Boolean> pushResult = null;		
		if (ClearanceChannelsEnum.HANGZHOU.id.equals(subOrder.getSeaChannel())) {	//杭州保税区
			pushResult = jkfDeclareOrderLocalService.pushOrderInfo(subOrder);
		}else{
			logger.error("[PUSH_ORDER_INFO][{}]不支持{}申报", subOrder.getOrderCode(), subOrder.getSeaChannelName());
			return new ResultInfo<>(new FailInfo("系统不支持订单所在保税区申报"));
		}
		PutCustomsStatus putStatus = pushResult.isSuccess() ? PutCustomsStatus.SUCCESS : PutCustomsStatus.FAIL;
		logger.error("[PUSH_ORDER_INFO][{}]推送订单至海关结果:{}",subOrder.getOrderCode(), putStatus.getDesc());
		
		customsClearanceLogService.insert(createCustomsClearanceLog(pushResult, subOrder));
		updateSubOrderWithDeliveryResult(subOrder, putStatus);
		return pushResult;
	}

	@Override
	public boolean checkDelivery(SubOrder subOrder) {	
		Long orderCode = subOrder.getOrderCode();
		if (!subOrder.getPutOrder()){
			logger.error("[PUSH_ORDER_INFO][{}]订单不需要推送", orderCode);
			return false;
		}
		if (PutCustomsStatus.isSuccess(subOrder.getPutOrderStatus())){
			logger.error("[PUSH_ORDER_INFO][{}]订单不重复推送", orderCode);
			return false;
		}
		return true;
	}

	@Override
	public ResultInfo<Boolean> prepareDelivery(SubOrder subOrder) {
//		if (false == PutCustomsStatus.isInit(subOrder.getPutOrderStatus())) {
//			logger.info("[PUSH_ORDER_INFO][{}]订单推送不为初始状态，请重置后重新推送", subOrder.getOrderCode());
//			return new ResultInfo<>(new FailInfo("订单推送不是初始状态"));
//		}
		return new ResultInfo<>(Boolean.TRUE);
	}

	@Override
	public boolean checkPutCustomsType(PutCustomsType type) {
		if (PutCustomsType.ORDER_DECLARE == type){
			return true;
		}
		return false;
	}

	private CustomsClearanceLog createCustomsClearanceLog(ResultInfo<Boolean> result, SubOrder subOrder){
		CustomsClearanceLog log = new CustomsClearanceLog();
		log.setCustomsCode("");
		log.setOrderCode(subOrder.getOrderCode());
		log.setType(PutCustomsType.ORDER_DECLARE.getIndex());
		log.setResult(result.isSuccess() == true ? 1 : 0);
		log.setResultDesc(result.isSuccess() == true ? "推送成功":result.getMsg().getMessage());
		log.setCreateTime(new Date());
		return log;
	}
	
	private void updateSubOrderWithDeliveryResult(SubOrder subOrder, PutCustomsStatus status){
		SubOrder so = new SubOrder();
		so.setId(subOrder.getId());
		so.setPutOrderStatus(status.code);
		subOrderService.updateNotNullById(so);
	}
}
