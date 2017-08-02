package com.tp.service.ord.local;

import com.tp.model.ord.SubOrder;



/**
 * 子订单取消服务
 * 
 * @author szy
 * @version 0.0.1
 */
public interface IOrderCancelLocalService {
	/**
	 * 付款后子订单取消
	 * 
	 * @param subOrder
	 * @param userId
	 * @param userName
	 * @return
	 */
	void cancelOrderByPaymentedForJob(SubOrder subOrderDO, Long userId,String userName);
}
