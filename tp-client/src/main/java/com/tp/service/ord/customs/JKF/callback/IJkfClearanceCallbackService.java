package com.tp.service.ord.customs.JKF.callback;

import com.tp.model.ord.JKF.JkfBaseDO;
import com.tp.model.ord.JKF.JkfCallbackResponse;

public interface IJkfClearanceCallbackService {
	
	/**
	 * 杭州保税区报关回执接口(异步处理)（消息模式） 
	 * @param JkfBaseDO
	 * @return JkfCallbackResponse
	 */
	JkfCallbackResponse asyncClearanceCallback(JkfBaseDO receiptResult) throws Exception;
	
	/**
	 * 杭州保税区报关回执接口(同步模式）
	 * @param JkfBaseDO
	 * @return JkfCallbackResponse 
	 */
	JkfCallbackResponse syncClearanceCallback(JkfBaseDO receiptResult) throws Exception;
}
