package com.tp.proxy.wms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.wms.StockoutCancel;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.wms.IStockoutCancelService;
/**
 * 取消配货表代理层
 * @author szy
 *
 */
@Service
public class StockoutCancelProxy extends BaseProxy<StockoutCancel>{

	@Autowired
	private IStockoutCancelService stockoutCancelService;

	@Override
	public IBaseService<StockoutCancel> getService() {
		return stockoutCancelService;
	}
}
