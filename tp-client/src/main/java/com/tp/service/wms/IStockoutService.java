package com.tp.service.wms;

import com.tp.dto.common.ResultInfo;
import com.tp.dto.wms.StockoutDto;
import com.tp.model.wms.Stockout;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 出库订单接口
  */
public interface IStockoutService extends IBaseService<Stockout>{

	/**
	 *	推送出库单至仓库 
	 *  @param stockoutDto 出库单
	 *  @return
	 */
	ResultInfo<Boolean> deliverStockoutOrder(StockoutDto stockoutDto);
	
	/**
	 * 查询出库单信息
	 * @param orderCode 
	 */
	ResultInfo<Stockout> queryStockoutByOrderCode(String orderCode);
}
