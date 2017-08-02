/**
 * 
 */
package com.tp.service.ord;

import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.JKF.JkfCancelOrderRequest;
import com.tp.model.ord.JKF.JkfGoodsDeclareRequest;
import com.tp.model.ord.JKF.JkfImportOrderRequest;
import com.tp.model.ord.JKF.JkfReceiptResult;

/**
 * @author Administrator
 *
 */
public interface IJKFSoaService {
	
	/**
	 *	订单数据申报 
	 *  @param importOrderRequest
	 *  @param isEncrypt 是否加密 true加密
	 *  @return
	 */
	public ResultInfo<JkfReceiptResult> orderDeclare(JkfImportOrderRequest importOrderRequest, boolean isEncrypt);
	
	/**
	 *	个人物品申报
	 *  @param goodsDeclareRequest
	 *  @param isEncrypt 是否加密 true加密
	 *  @return 
	 */
	public ResultInfo<JkfReceiptResult> personalGoodsDeclare(JkfGoodsDeclareRequest goodsDeclareRequest, boolean isEncrypt);
	
	
	/** 删除海关订单 
	 * @param  cancelOrderRequest
	 * @param isEncrypt 是否加密 true加密
	 * @return
	 */
	public ResultInfo<JkfReceiptResult> cancelOrderDeclare(JkfCancelOrderRequest cancelOrderRequest, boolean isEncrypt);
	
	
}
