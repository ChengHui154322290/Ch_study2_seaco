/**
 * 
 */
package com.tp.service.ord.customs.JKF.callback;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.vo.customs.MQCustomsConstant;
import com.tp.common.vo.customs.JKFConstant.JKFFeedbackType;
import com.tp.common.vo.customs.JKFConstant.JKFResultError;
import com.tp.model.ord.JKF.JkfBaseDO;
import com.tp.model.ord.JKF.JkfCallbackResponse;
import com.tp.mq.RabbitMqProducer;
import com.tp.service.ord.customs.JKF.callback.IJKFClearanceCallbackProcessService;
import com.tp.service.ord.customs.JKF.callback.IJkfClearanceCallbackService;

/**
 * @author Administrator
 * 报关回执
 */
@Service
public class JkfClearanceCallbackService implements IJkfClearanceCallbackService{
	
	private static final Logger logger = LoggerFactory.getLogger(JkfClearanceCallbackService.class);
	
	@Autowired
	private List<IJKFClearanceCallbackProcessService> processServiceList;
	
	@Autowired
    private RabbitMqProducer rabbitMqProducer;
	
	//同步处理
	@Override
	public JkfCallbackResponse syncClearanceCallback(JkfBaseDO receiptResult) throws Exception {
		logger.info("[JKF_CLEARANCE_CALLBACK]同步请求回执");
		JkfCallbackResponse response = validateReceiptResult(receiptResult);
		if (null != response) return response;
		
		IJKFClearanceCallbackProcessService processService = getProcessService(receiptResult.getReceiptType());		
		return processService.processCallback(receiptResult);
	}

	//异步处理(MQ消息)
	@Override
	public JkfCallbackResponse asyncClearanceCallback(JkfBaseDO receiptResult) throws Exception {
		logger.info("[JKF_CLEARANCE_CALLBACK]异步请求回执");
		JkfCallbackResponse response = validateReceiptResult(receiptResult);
		if (null != response) return response;
		sendClearanceCallbackMQMessage(receiptResult);
		return new JkfCallbackResponse();
	}
	
	private void sendClearanceCallbackMQMessage(JkfBaseDO receiptResult) throws Exception{
		rabbitMqProducer.sendP2PMessage(MQCustomsConstant.JKF_CLEARANCE_CALLBACK_P2P_QUEUE, receiptResult);	
	}
	
	private JkfCallbackResponse validateReceiptResult(JkfBaseDO receiptResult){
		JKFFeedbackType type = receiptResult.getReceiptType();
		if (type == null){
			logger.error("[JKF_CLEARANCE_CALLBACK]消息类型为空");
			return new JkfCallbackResponse(JKFResultError.INVALID_BUSINESS_TYPE);
		}
		IJKFClearanceCallbackProcessService processService = getProcessService(type);
		if (processService == null){
			logger.error("[JKF_CLEARANCE_CALLBACK]消息类型错误");
			return new JkfCallbackResponse(JKFResultError.INVALID_BUSINESS_TYPE); 
		}
		return null;
	}
	private IJKFClearanceCallbackProcessService getProcessService(JKFFeedbackType type){
		for(IJKFClearanceCallbackProcessService service : processServiceList){
			if (service.checkReceiptType(type)){
				return service;
			}
		}
		return null;
	}	
}
