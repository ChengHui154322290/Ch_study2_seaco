/**
 * 
 */
package com.tp.service.ord.customs.JKF.callback;

import org.springframework.stereotype.Service;

import com.tp.common.vo.customs.JKFConstant.JKFFeedbackType;
import com.tp.model.ord.JKF.JkfBaseDO;
import com.tp.model.ord.JKF.JkfCallbackResponse;
import com.tp.service.ord.customs.JKF.callback.IJKFClearanceCallbackProcessService;

/**
 * @author Administrator
 * 运单出区
 */
@Service
public class JkfBillCallbackProcessService implements IJKFClearanceCallbackProcessService{

	@Override
	public JkfCallbackResponse processCallback(JkfBaseDO receiptResult) {
		return new JkfCallbackResponse();
	}

	@Override
	public boolean checkReceiptType(JKFFeedbackType type) {
		if(JKFFeedbackType.CUSTOMS_BILL_CALLBACK == type) {
			return true;
		}
		return false;
	}
}
