/**
 * 
 */
package com.tp.service.ord.customs.JKF;

import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.SubOrder;

/**
 * @author Administrator
 *	取消海淘订单(需申报海关)
 */
public interface IJKFCancelOrderService {

	/**
	 *  取消海淘订单（申报海关）
	 *  @param subOrder 子订单
	 *  @param cancelReason 取消原因
	 *  @return
	 */
	public ResultInfo<Boolean> cancelSeaOrder(SubOrder subOrder, String cancelReason);
	
}
