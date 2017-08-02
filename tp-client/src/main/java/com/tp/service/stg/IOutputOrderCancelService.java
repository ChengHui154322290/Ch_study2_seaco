/**
 * 
 */
package com.tp.service.stg;

import com.tp.dto.common.ResultInfo;

/**
 * @author Administrator
 * 出库订单取消
 */
public interface IOutputOrderCancelService {
	
	/**
	 * 取消发货，自营商品将向仓库发送取消出库指令，商家商品
	 * 
	 * @param orderCode
	 * 		子订单编号
	 * @return
	 * 		<pre>
	 * 			返回值code说明：
	 *			000 取消成功 
	 * 			002 取消失败，订单已发运或者订单不存在
	 * 			003 取消失败，订单进入仓内作业（此时订单可拦截，可根据客户预设，视为取消成功或者失败）
	 * 			009 取消失败，取消时发生异常
	 * 		</pre>
	 * @return
	 * @throws Exception 
	 */
	ResultInfo<Boolean> cancelOutputOrder(String orderCode, Long warehouseId,String reason);
}
