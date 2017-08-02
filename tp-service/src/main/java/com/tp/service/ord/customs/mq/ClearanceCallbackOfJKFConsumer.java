package com.tp.service.ord.customs.mq;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.tp.common.vo.customs.JKFConstant.JKFFeedbackType;
import com.tp.common.vo.customs.JKFConstant.JKFResultError;
import com.tp.model.ord.JKF.JkfBaseDO;
import com.tp.model.ord.JKF.JkfCallbackResponse;
import com.tp.mq.MqMessageCallBack;
import com.tp.service.ord.customs.JKF.callback.IJKFClearanceCallbackProcessService;

/**
 * 杭州保税区-电子口岸报关回执消息处理 
 */
@Service
public class ClearanceCallbackOfJKFConsumer implements MqMessageCallBack{

	private static final Logger logger = LoggerFactory.getLogger(ClearanceCallbackOfJKFConsumer.class);
	
	@Autowired
	private List<IJKFClearanceCallbackProcessService> callbackProcessServiceList;
	
	@Override
	public boolean execute(Object object) {
		if (object == null && false == (object instanceof JkfBaseDO)){
			logger.error("从MQ中获取的实体数据为:{}", JSONObject.toJSON(object));
		}else{
			clearanceCallbackProcess((JkfBaseDO)object);
		}
		return true;
	}
	
	private void clearanceCallbackProcess(JkfBaseDO result){
		JkfCallbackResponse response = validateReceiptResult(result);
		if (response != null) return;
		try{
			getProcessService(result.getReceiptType()).processCallback(result);	
		}catch(Exception e){
			logger.error("[JKF_CLEARANCE_CALLBACK]异步处理异常", e);
		}
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
		for(IJKFClearanceCallbackProcessService service : callbackProcessServiceList){
			if (service.checkReceiptType(type)){
				return service;
			}
		}
		return null;
	}	
}
