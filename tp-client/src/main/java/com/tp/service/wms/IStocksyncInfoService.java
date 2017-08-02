package com.tp.service.wms;

import java.util.List;

import com.tp.common.vo.stg.WarehouseConstant.WMSCode;
import com.tp.dto.common.ResultInfo;
import com.tp.model.wms.StocksyncInfo;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 库存同步信息表接口
  */
public interface IStocksyncInfoService extends IBaseService<StocksyncInfo>{
	
	/**
	 * 库存同步 
	 */
	ResultInfo<Boolean> syncSkuInventory(List<StocksyncInfo> stocksyncInfos, WMSCode code);
}
