/**
 * 
 */
package com.tp.service.wms.thirdparty;

import com.tp.dto.common.ResultInfo;
import com.tp.model.wms.Stockout;
import com.tp.model.wms.StockoutCancel;

/**
 * @author Administrator
 *
 */
public interface IStockoutCancelToTPService {
	
	/**
	 * 校验取消
	 * @param stockout 
	 */
	boolean check(Stockout stockout);
	
	/**
	 * 取消订单
	 * @param orderCode
	 * @return 
	 */
	ResultInfo<StockoutCancel> cancelOrder(String orderCode); 
}
