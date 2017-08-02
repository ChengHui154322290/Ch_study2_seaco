/**
 * 
 */
package com.tp.service.ord.customs.JKF.callback;

import com.tp.common.vo.customs.JKFConstant.JKFFeedbackType;
import com.tp.model.ord.JKF.JkfBaseDO;
import com.tp.model.ord.JKF.JkfCallbackResponse;

/**
 * @author Administrator
 *
 */
public interface IJKFClearanceCallbackProcessService {

	/**
	 * 校验回执类型
	 * @param type
	 * @return boolean  
	 */
	boolean checkReceiptType(JKFFeedbackType type);
	
	/**
	 * 处理回执
	 * @param receiptResult
	 * @return JkfCallbackResponse 
	 */
	JkfCallbackResponse processCallback(JkfBaseDO receiptResult) throws Exception;
}
