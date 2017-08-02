package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.SupplierInvoice;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.ISupplierInvoiceService;
/**
 * 供应商-开票信息表代理层
 * @author szy
 *
 */
@Service
public class SupplierInvoiceProxy extends BaseProxy<SupplierInvoice>{

	@Autowired
	private ISupplierInvoiceService supplierInvoiceService;

	@Override
	public IBaseService<SupplierInvoice> getService() {
		return supplierInvoiceService;
	}
}
