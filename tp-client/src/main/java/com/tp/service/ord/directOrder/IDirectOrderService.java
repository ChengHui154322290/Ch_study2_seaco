package com.tp.service.ord.directOrder;

import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.SubOrder;

public interface IDirectOrderService{
	/**
	 * 推送直邮订单
	 * @param 
	 * @return
	 */
	void pushDirectOrderForJob();
	
	/**
	 * 手动推送订单
	 * @param subOrder
	 * @return
	 */
	ResultInfo<Boolean> pushDirectOrderToCustoms(SubOrder subOrder);
	
	/**
	 * 获取运单信息
	 */
	void searchDirectOrderForJob();
}
