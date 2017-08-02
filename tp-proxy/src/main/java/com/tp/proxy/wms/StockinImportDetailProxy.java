package com.tp.proxy.wms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.wms.StockinImportDetail;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.wms.IStockinImportDetailService;
/**
 * 入库单导入明细表代理层
 * @author szy
 *
 */
@Service
public class StockinImportDetailProxy extends BaseProxy<StockinImportDetail>{

	@Autowired
	private IStockinImportDetailService stockinImportDetailService;

	@Override
	public IBaseService<StockinImportDetail> getService() {
		return stockinImportDetailService;
	}
}
