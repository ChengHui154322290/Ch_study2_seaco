package com.tp.service.ord.customs.JKF;

import java.util.List;

import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.SubOrder;

/**
 * 
 * 浙江电子口岸申报服务接口
 * 
 */
public interface IJKFDeclareOrderLocalService {
	
	/**
	 * 推送订单至电子口岸
	 * @param subOrders
	 * @return
	 */
	boolean pushOrderInfo(List<SubOrder> subOrders);
	
	/**
	 * 推送单个订单至电子口岸 
	 */
	ResultInfo<Boolean> pushOrderInfo(SubOrder subOrder);
	
}
