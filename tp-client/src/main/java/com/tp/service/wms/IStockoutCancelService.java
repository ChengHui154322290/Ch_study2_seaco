package com.tp.service.wms;

import com.tp.dto.common.ResultInfo;
import com.tp.model.wms.StockoutCancel;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 取消配货表接口
  */
public interface IStockoutCancelService extends IBaseService<StockoutCancel>{

	/**
	 *	取消配货 
	 * 	@param orderCode
	 *  @return
	 */
	ResultInfo<Boolean> cancelOutputOrder(String orderCode);
}
