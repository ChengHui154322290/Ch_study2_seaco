package com.tp.proxy.wms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.wms.StockinImportLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.wms.IStockinImportLogService;
/**
 * 入库单导入日志表代理层
 * @author szy
 *
 */
@Service
public class StockinImportLogProxy extends BaseProxy<StockinImportLog>{

	@Autowired
	private IStockinImportLogService stockinImportLogService;

	@Override
	public IBaseService<StockinImportLog> getService() {
		return stockinImportLogService;
	}
}
