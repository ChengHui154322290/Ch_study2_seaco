package com.tp.proxy.wms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.wms.StockoutBackDetail;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.wms.IStockoutBackDetailService;
/**
 * 出库单回执明细代理层
 * @author szy
 *
 */
@Service
public class StockoutBackDetailProxy extends BaseProxy<StockoutBackDetail>{

	@Autowired
	private IStockoutBackDetailService stockoutBackDetailService;

	@Override
	public IBaseService<StockoutBackDetail> getService() {
		return stockoutBackDetailService;
	}
}
