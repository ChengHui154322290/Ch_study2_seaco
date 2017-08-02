/**
 * 
 */
package com.tp.service.wms.thirdparty;

import com.tp.dto.common.ResultInfo;
import com.tp.model.wms.Stockout;
import com.tp.model.wms.StockoutBack;

/**
 * @author Administrator
 * 回执
 */
public interface IStockoutBackTPService {

	/**
	 * 校验 
	 */
	boolean check(StockoutBack stockoutBack, Stockout stockout);
	
	/**
	 * 处理第三方出库反馈
	 * @param stockoutBack
	 * @param stockout
	 * @return 
	 */
	ResultInfo<StockoutBack> processStockoutBack(StockoutBack stockoutBack, Stockout stockout);
	
}
