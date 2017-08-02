/**
 * 
 */
package com.tp.service.ord.customs;

import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.SubOrder;

/**
 * @author Administrator
 * 海淘自营订单推送逻辑处理
 */
public interface ISeaOrderDeliveryLocalService {
	/**
	 * 推送海淘订单至海关
	 * @param
	 * @return
	 */
	void declareSeaOrderToCustoms();
	
	ResultInfo<Boolean> declareSeaOrderToCustoms(SubOrder subOrder);
	
}
