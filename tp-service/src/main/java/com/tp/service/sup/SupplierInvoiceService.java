package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.SupplierInvoiceDao;
import com.tp.model.sup.SupplierInvoice;
import com.tp.service.BaseService;
import com.tp.service.sup.ISupplierInvoiceService;

@Service
public class SupplierInvoiceService extends BaseService<SupplierInvoice> implements ISupplierInvoiceService {

	@Autowired
	private SupplierInvoiceDao supplierInvoiceDao;
	
	@Override
	public BaseDao<SupplierInvoice> getDao() {
		return supplierInvoiceDao;
	}

}
