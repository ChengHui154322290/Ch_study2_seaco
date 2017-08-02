package com.tp.proxy.wms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.wms.StockoutDetail;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.wms.IStockoutDetailService;
/**
 * 出库订单明细代理层
 * @author szy
 *
 */
@Service
public class StockoutDetailProxy extends BaseProxy<StockoutDetail>{

	@Autowired
	private IStockoutDetailService stockoutDetailService;

	@Override
	public IBaseService<StockoutDetail> getService() {
		return stockoutDetailService;
	}
}
