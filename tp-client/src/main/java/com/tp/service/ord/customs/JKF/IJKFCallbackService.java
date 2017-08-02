package com.tp.service.ord.customs.JKF;

import com.tp.model.ord.JKF.JkfCallbackResponse;
import com.tp.model.ord.JKF.JkfCancelOrderResult;
import com.tp.model.ord.JKF.JkfGoodsDeclarResult;
import com.tp.model.ord.JKF.JkfReceiptResult;
import com.tp.model.ord.JKF.JkfTaxIsNeedResult;

/**
 *	电子口岸回执逻辑处理 
 */
public interface IJKFCallbackService {
	
	/**通关平台的回执（业务检验时的异步回执）（包括订单回执，个人物品信息回执及其他回执）
	 * @param receiptResult
	 * @return JkfCallbackResponse
	 */
	JkfCallbackResponse customsDeclareCallback(JkfReceiptResult receiptResult) throws Exception;
	
	/** 
	 * 个人物品申报回执  
	 * @param goodsDeclarResult
	 * @return JkfCallbackResponse
	 */
	JkfCallbackResponse goodsDeclareCallback(JkfGoodsDeclarResult goodsDeclarResult);
	
	/**
	 * 税费回执
	 * @param taxResult
	 * @return JkfCallbackResponse
	 */
	JkfCallbackResponse taxIsNeedCallback(JkfTaxIsNeedResult taxResult);
	
	/**
	 * 删除(取消)订单接口
	 * @param cancelOrderResult
	 * @return JkfCallbackResponse
	 */
	JkfCallbackResponse cancelOrderCallback(JkfCancelOrderResult cancelOrderResult);
}
