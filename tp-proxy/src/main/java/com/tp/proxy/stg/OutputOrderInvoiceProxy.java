package com.tp.proxy.stg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.stg.OutputOrderInvoice;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.stg.IOutputOrderInvoiceService;
/**
 * 订单发票代理层
 * @author szy
 *
 */
@Service
public class OutputOrderInvoiceProxy extends BaseProxy<OutputOrderInvoice>{

	@Autowired
	private IOutputOrderInvoiceService outputOrderInvoiceService;

	@Override
	public IBaseService<OutputOrderInvoice> getService() {
		return outputOrderInvoiceService;
	}
}
