/**
 * 
 */
package com.tp.service.wms.thirdparty;

import com.tp.dto.common.ResultInfo;
import com.tp.model.wms.Stockout;

/**
 * @author Administrator
 * 
 */
public interface IStockoutServiceToTP {
	/**
	 *	推送出库单至第三方仓库 
	 *  @param stockoutDto 出库单
	 *  @return 推送是否成功 
	 */
	ResultInfo<Boolean> deliverStockoutOrder(Stockout stockout);
	
	/**
	 * 检查 
	 * @param stockout
	 * @return
	 */
	boolean check(Stockout stockout);
}
