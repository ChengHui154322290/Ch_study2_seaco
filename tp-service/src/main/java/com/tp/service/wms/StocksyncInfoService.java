package com.tp.service.wms;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tp.common.dao.BaseDao;
import com.tp.common.vo.stg.WarehouseConstant.WMSCode;
import com.tp.dao.wms.StocksyncInfoDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.wms.StocksyncInfo;
import com.tp.service.BaseService;
import com.tp.service.wms.IStocksyncInfoService;
import com.tp.service.wms.thirdparty.IStocksyncTPService;

@Service
public class StocksyncInfoService extends BaseService<StocksyncInfo> implements IStocksyncInfoService {

	private static final Logger logger = LoggerFactory.getLogger(StocksyncInfoService.class);
	
	@Autowired
	private StocksyncInfoDao stocksyncInfoDao;

	
	@Autowired
	private List<IStocksyncTPService> stocksyncTPServiceList;
	
	
	@Override
	public BaseDao<StocksyncInfo> getDao() {
		return stocksyncInfoDao;
	}

	/**
	 * 库存同步 
	 */
	public ResultInfo<Boolean> syncSkuInventory(List<StocksyncInfo> stocksyncInfos, WMSCode code){
		logger.info("[STOCK_SKU_SYNC]库存同步：{}", JSONObject.toJSONString(stocksyncInfos));
		if (CollectionUtils.isEmpty(stocksyncInfos)) {
			return new ResultInfo<>(Boolean.TRUE);
		}
		return syncInventory(stocksyncInfos, code);

	}
	
	private ResultInfo<Boolean> syncInventory(List<StocksyncInfo> stocksyncInfos, WMSCode code){
		for (IStocksyncTPService service : stocksyncTPServiceList) {
			if (service.check(stocksyncInfos, code)) {
				return service.syncSkuInventory(stocksyncInfos);
			}
		}
		return new ResultInfo<>(new FailInfo("仓库不存在"));
	}
}
