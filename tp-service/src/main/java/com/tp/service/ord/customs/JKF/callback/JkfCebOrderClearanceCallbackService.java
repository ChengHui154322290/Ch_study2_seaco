/**
 * 
 */
package com.tp.service.ord.customs.JKF.callback;

import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tp.common.vo.OrderConstant.ClearanceStatus;
import com.tp.common.vo.OrderConstant.DeclareCustoms;
import com.tp.common.vo.OrderConstant.PutCustomsStatus;
import com.tp.common.vo.OrderConstant.PutCustomsType;
import com.tp.common.vo.customs.JKFConstant.JKFFeedbackType;
import com.tp.common.vo.customs.JKFConstant.JKFResultError;
import com.tp.common.vo.ord.LogTypeConstant;
import com.tp.model.ord.CustomsClearanceLog;
import com.tp.model.ord.OrderDeclareReceiptLog;
import com.tp.model.ord.OrderStatusLog;
import com.tp.model.ord.SubOrder;
import com.tp.model.ord.JKF.JkfBaseDO;
import com.tp.model.ord.JKF.JkfCallbackResponse;
import com.tp.model.ord.ceb.CebOrderResponse;
import com.tp.model.ord.ceb.CebOrderResponse.OrderReturn;
import com.tp.service.ord.ICustomsClearanceLogService;
import com.tp.service.ord.IOrderDeclareReceiptLogService;
import com.tp.service.ord.IOrderStatusLogService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.customs.JKF.callback.IJKFClearanceCallbackProcessService;
import com.tp.util.StringUtil;

/**
 * @author Administrator
 * 海关总署回执处理逻辑
 */
@Service
public class JkfCebOrderClearanceCallbackService implements IJKFClearanceCallbackProcessService{

	private static final Logger logger = LoggerFactory.getLogger(JkfCebOrderClearanceCallbackService.class);
	
	@Autowired
	private ISubOrderService subOrderService;
	
	@Autowired
	private IOrderDeclareReceiptLogService orderDeclareReceiptLogService;
	
	@Autowired
	private IOrderStatusLogService orderStatusLogService;
	
	@Autowired
	private ICustomsClearanceLogService customsClearanceLogService;
	
	@Override
	public boolean checkReceiptType(JKFFeedbackType type) {
		if (JKFFeedbackType.CUSTOMS_CEB_CALLBACK_ORDER == type){
			return true;
		}
		return false;
	}

	@Override
	public JkfCallbackResponse processCallback(JkfBaseDO receiptResult) throws Exception {
		CebOrderResponse response = (CebOrderResponse)receiptResult;
		if (CollectionUtils.isEmpty(response.getOrderReturn())){
			logger.error("[CEB_ORDER_CUSTOMS_CALLBACK]总署订单回执数据为空");
			return new JkfCallbackResponse(JKFResultError.INVALID_CONTENT);
		}
		for(OrderReturn cebOrderReturn : response.getOrderReturn()){
			cebOrderReturn = preProcessSingleCebOrderCallback(cebOrderReturn);
			if (cebOrderReturn == null) continue;
			processSingleCebOrderCallback(cebOrderReturn);
		}
		return new JkfCallbackResponse();
	}
	
	//单个订单回执
	private void processSingleCebOrderCallback(OrderReturn orderReturn){
		Long orderCode = Long.valueOf(orderReturn.getOrderNo());
		SubOrder subOrder = subOrderService.selectOneByCode(orderCode);
		
		orderDeclareReceiptLogService.insert(createOrderDeclareReceiptLog(orderReturn));
		if (false == checkOrderCallbackWithDeclareStatus(subOrder)) return;
		
		PutCustomsStatus orderStatus = convertCebDeclareResultToCustomsStatus(Integer.valueOf(orderReturn.getReturnStatus()));
		if (orderStatus == null) return;
		
		updateSubOrderByAuditResult(orderStatus, subOrder);
		
		orderStatusLogService.insert(createOrderStatusLog(subOrder, orderReturn.getReturnInfo()));
		customsClearanceLogService.insert(createCustomsClearanceLog(orderReturn));
	}
	
	private void updateSubOrderByAuditResult(PutCustomsStatus orderStatus, SubOrder subOrder){
		//更新状态
		SubOrder so = new SubOrder();
		so.setId(subOrder.getId());
		if (PutCustomsStatus.AUDIT_FAIL == orderStatus) {
			so.setClearanceStatus(ClearanceStatus.AUDIT_FAILED.getCode());
		}else if(PutCustomsStatus.FAIL == orderStatus){
			so.setClearanceStatus(ClearanceStatus.PUT_FAILED.getCode());
		}
		so.setPutOrderStatus(orderStatus.getCode());	
		so.setUpdateTime(new Date());
		subOrderService.updateNotNullById(so);		
	}
	
	private boolean checkOrderCallbackWithDeclareStatus(SubOrder subOrder){
		Integer clearanceStatus = subOrder.getClearanceStatus();
		if (ClearanceStatus.hasAudit(clearanceStatus)){
			logger.error("[CUSTOMS_CALLBACK][{}]申报单海关已审核，当前审核结果：{}, 重复反馈", 
					subOrder.getOrderCode(), ClearanceStatus.getClearanceDescByCode(clearanceStatus));
			return false;
		}
		Integer orderDeclareStatus = subOrder.getPutOrderStatus();
		if (false == PutCustomsStatus.isSuccess(orderDeclareStatus)){
			logger.error("[CUSTOMS_CALLBACK][{}]订单不是推送成功状态,回执无效,当前推单状态：{}", 
					subOrder.getOrderCode(), PutCustomsStatus.getCustomsStatusDescByCode(orderDeclareStatus));
			return false;
		}
		return true;
	}
	
	private CustomsClearanceLog createCustomsClearanceLog(OrderReturn orderReturn){
		Integer result = convertCebOrderDeclareResult(Integer.parseInt(orderReturn.getReturnStatus()));
		CustomsClearanceLog log = new CustomsClearanceLog();
		log.setOrderCode(Long.valueOf(orderReturn.getOrderNo()));
		log.setResult(result == 1?1:0);
		log.setResultDesc(orderReturn.getReturnInfo());
		log.setType(PutCustomsType.ORDER_DECLARE.index);
		log.setCustomsCode(DeclareCustoms.CEB.code);
		log.setCreateTime(new Date());
		return log;
	}
	
	private OrderStatusLog createOrderStatusLog(SubOrder subOrder, String resultMessage){
		OrderStatusLog orderStatusLog = new OrderStatusLog();
		orderStatusLog.setParentOrderCode(subOrder.getParentOrderCode());
		orderStatusLog.setOrderCode(subOrder.getOrderCode());
		orderStatusLog.setPreStatus(subOrder.getOrderStatus());
		orderStatusLog.setCurrStatus(subOrder.getOrderStatus());
		orderStatusLog.setCreateTime(new Date());
		orderStatusLog.setCreateUserId(1L);
		orderStatusLog.setCreateUserName("系统回调消息");
		orderStatusLog.setCreateUserType(LogTypeConstant.LOG_TYPE.SYSTEM.code);
		orderStatusLog.setName("海淘订单审核信息");
		orderStatusLog.setContent("海淘审核返回信息："+resultMessage);
		return orderStatusLog;
	}
	
	private OrderDeclareReceiptLog createOrderDeclareReceiptLog(OrderReturn orderReturn){
		String orderCode = orderReturn.getOrderNo();
		OrderDeclareReceiptLog declareReceiptLog = new OrderDeclareReceiptLog();
		declareReceiptLog.setOrderCode(Long.valueOf(orderCode));
		declareReceiptLog.setDeclareNo(orderCode);
		declareReceiptLog.setCustomsCode(DeclareCustoms.CEB.code);
		declareReceiptLog.setResult(convertCebOrderDeclareResult(Integer.parseInt(orderReturn.getReturnStatus())));
		declareReceiptLog.setResultDetail(orderReturn.getReturnInfo());
		declareReceiptLog.setResMsg(JSONObject.toJSONString(orderReturn));
		declareReceiptLog.setRemark("");
		declareReceiptLog.setCreateTime(new Date());
		return declareReceiptLog;
	}
	
	private PutCustomsStatus convertCebDeclareResultToCustomsStatus(Integer cebStatus){
		if (cebStatus < 0) return PutCustomsStatus.FAIL;
		if (cebStatus == 4) return PutCustomsStatus.FAIL;
		if (cebStatus == 100) return PutCustomsStatus.AUDIT_FAIL;
		return null;
	}
	
	//0无效状态1成功2失败
	private Integer convertCebOrderDeclareResult(Integer cebStatus){
		PutCustomsStatus status = convertCebDeclareResultToCustomsStatus(cebStatus);
		if (status == null) return 0;
		if (PutCustomsStatus.AUDIT_SUCCESS == status || PutCustomsStatus.SUCCESS == status) return 1;
		if (PutCustomsStatus.FAIL == status || PutCustomsStatus.AUDIT_FAIL == status) return 2;
		return 0;
	}
	
	private OrderReturn preProcessSingleCebOrderCallback(OrderReturn orderReturn){
		boolean validate = validateCebOrder(orderReturn);
		if (validate == false) return null;
		return orderReturn;
	}
	
	private boolean validateCebOrder(OrderReturn orderReturn){
		String orderCode = orderReturn.getOrderNo();
		if (StringUtil.isEmpty(orderCode)){
			logger.error("订单编号为空");
			return false;
		}
		SubOrder subOrder = subOrderService.selectOneByCode(Long.valueOf(orderCode));	
		if (null == subOrder) {
			logger.error("[CEB_CUSTOMS_CALLBACK][{}]订单不存在", orderCode);
			return false;
		}
		return true;
	}
}
