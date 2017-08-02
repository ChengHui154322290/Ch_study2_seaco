/**
 * 
 */
package com.tp.service.wms.thirdparty;

import java.util.List;

import com.tp.common.vo.stg.WarehouseConstant.WMSCode;
import com.tp.dto.common.ResultInfo;
import com.tp.model.wms.StocksyncInfo;

/**
 * @author Administrator
 *
 */
public interface IStocksyncTPService {
	
	/**
	 * 校验同步 
	 */
	boolean check(List<StocksyncInfo> syInfos, WMSCode wmsCode);
	
	/**
	 * 库存同步 
	 */
	ResultInfo<Boolean> syncSkuInventory(List<StocksyncInfo> syInfos);
}
