/**
 * 
 */
package com.tp.service.ord.customs;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.vo.OrderConstant.PutCustomsStatus;
import com.tp.common.vo.OrderConstant.PutCustomsType;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.CustomsClearanceLog;
import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.SubOrder;
import com.tp.model.pay.PaymentInfo;
import com.tp.service.ord.ICustomsClearanceLogService;
import com.tp.service.ord.IOrderConsigneeService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.customs.ISeaCustomsInfoDeliveryService;
import com.tp.service.pay.IPaymentService;

/**
 * @author Administrator
 * 支付单申报
 */
@Service
public class SeaPayInfoDeliveryService implements ISeaCustomsInfoDeliveryService{

	private static final Logger logger = LoggerFactory.getLogger(SeaPayInfoDeliveryService.class);
			
	@Autowired
	private IPaymentService paymentService;
	
	@Autowired
	private IOrderConsigneeService orderConsigneeService;
	
	@Autowired
	private ISubOrderService subOrderService;
	
	@Autowired
	private ICustomsClearanceLogService customsClearanceLogService;
	
	@Override
	public ResultInfo<Boolean> delivery(SubOrder subOrder) {
		OrderConsignee consignee = orderConsigneeService.selectOneByOrderId(subOrder.getParentOrderId());
		if (null == consignee) {
			logger.error("[PUSH_PAYMENT_INFO][{}]收件人信息不存在", subOrder.getOrderCode());
			return new ResultInfo<>(new FailInfo("收件人信息不存在"));
		}
		
		ResultInfo<PaymentInfo> pushResult = null;	
		pushResult = paymentService.putPaymentInfoToCustoms(subOrder, consignee);
		ResultInfo<Boolean> result = pushResult.isSuccess()?new ResultInfo<>(Boolean.TRUE):new ResultInfo<>(pushResult.getMsg());
		
		PutCustomsStatus putPayStatus = pushResult.isSuccess() ? PutCustomsStatus.SUCCESS : PutCustomsStatus.FAIL;
		logger.error("[PUSH_PAYMENT_INFO][{}]推送支付单结果：{}", subOrder.getOrderCode(), putPayStatus.getDesc());

		updateSubOrderWithDeliveryResult(subOrder, putPayStatus);
		customsClearanceLogService.insert(createCustomsClearanceLog(result, subOrder));
		return result;
	}

	@Override
	public boolean checkDelivery(SubOrder subOrder) {
		Integer putStatus = subOrder.getPutPayStatus();
		if (!subOrder.getPutPayOrder()){
			logger.error("[PUSH_PAYMENT_INFO][{}]支付单不需推送", subOrder.getOrderCode());
			return false;
		}
		if (PutCustomsStatus.isSuccess(putStatus)) {
			logger.info("[PUSH_PAYMENT_INFO][{}]支付单不需要重复推送", subOrder.getOrderCode());
			return false;
		}
		return true;
	}

	@Override
	public ResultInfo<Boolean> prepareDelivery(SubOrder subOrder) {
//		if (false == PutCustomsStatus.isInit(subOrder.getPutPayStatus())) {
//			logger.info("[PUSH_PAYMENT_INFO][{}]支付单不是初始状态，请重置后重新推送", subOrder.getOrderCode());
//			return new ResultInfo<>(new FailInfo("支付单推送状态未初始化"));
//		}
		return new ResultInfo<>(Boolean.TRUE);
	}

	@Override
	public boolean checkPutCustomsType(PutCustomsType type) {
		if (PutCustomsType.PAY_DECLARE == type){
			return true;
		}
		return false;
	}
	
	private CustomsClearanceLog createCustomsClearanceLog(ResultInfo<Boolean> pushResult, SubOrder subOrder){
		CustomsClearanceLog log = new CustomsClearanceLog();
		log.setCustomsCode("");
		log.setOrderCode(subOrder.getOrderCode());
		log.setType(PutCustomsType.PAY_DECLARE.getIndex());
		log.setResult(pushResult.isSuccess() == true ? 1 : 0);
		log.setResultDesc(pushResult.isSuccess() == true ? "推送成功":pushResult.getMsg().getMessage());
		log.setCreateTime(new Date());
		return log;
	}
	
	private void updateSubOrderWithDeliveryResult(SubOrder subOrder, PutCustomsStatus status){
		SubOrder so = new SubOrder();
		so.setId(subOrder.getId());
		so.setPutPayStatus(status.code);
		subOrderService.updateNotNullById(so);
	}
}
