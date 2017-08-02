package com.tp.proxy.wms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.wms.StockoutInvoice;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.wms.IStockoutInvoiceService;
/**
 * 订单发票代理层
 * @author szy
 *
 */
@Service
public class StockoutInvoiceProxy extends BaseProxy<StockoutInvoice>{

	@Autowired
	private IStockoutInvoiceService stockoutInvoiceService;

	@Override
	public IBaseService<StockoutInvoice> getService() {
		return stockoutInvoiceService;
	}
}
