/**
 * 
 */
package com.tp.service.ord.customs;

import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.CancelInfo;
import com.tp.model.ord.SubOrder;

/**
 * @author Administrator
 *
 */
public interface ISeaOrderCancelLocalService {
	
	/**
	 * 取消海淘订单 
	 * @param subOrder
	 * @param cancelInfo
	 * @return
	 */
	ResultInfo<Boolean> cancelSeaOrder(SubOrder subOrder, String cancelReason);
}
