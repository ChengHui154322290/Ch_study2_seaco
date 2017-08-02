package com.tp.proxy.wms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.stg.WarehouseConstant.WMSCode;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.wms.StocksyncInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.wms.IStocksyncInfoService;
/**
 * 库存同步信息表代理层
 * @author szy
 *
 */
@Service
public class StocksyncInfoProxy extends BaseProxy<StocksyncInfo>{

	@Autowired
	private IStocksyncInfoService stocksyncInfoService;

	@Override
	public IBaseService<StocksyncInfo> getService() {
		return stocksyncInfoService;
	}
	
	/** 库存同步 */
	public ResultInfo<Boolean> processStocksync(List<StocksyncInfo> stocksyncInfos, WMSCode code){
		try {
			return stocksyncInfoService.syncSkuInventory(stocksyncInfos, code);
		} catch (Exception e) {
			ExceptionUtils.print(new FailInfo(e),logger, JSONObject.toJSONString(stocksyncInfos), code);
			return new ResultInfo<>(new FailInfo("处理出库单回执异常"));
		}
	}
}
