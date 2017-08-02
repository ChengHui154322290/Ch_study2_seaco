package com.tp.service.wms;

import java.util.List;

import com.tp.dto.common.ResultInfo;
import com.tp.model.wms.StockoutBack;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 出库单回执接口
  */
public interface IStockoutBackService extends IBaseService<StockoutBack>{
	
	/**
	 * 处理出库单回执
	 * @param stockoutBack
	 * @return 
	 */
	ResultInfo<Boolean> processStockoutBack(StockoutBack stockoutBack)  throws Exception;
	
	/**
	 * 处理出库单回执
	 * @param stockoutBack
	 * @return 
	 */
	ResultInfo<Boolean> processStockoutBack(List<StockoutBack> stockoutBacks) throws Exception;
}
