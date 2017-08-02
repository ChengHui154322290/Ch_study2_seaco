package com.tp.service.stg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.stg.OutputOrderInvoiceDao;
import com.tp.model.stg.OutputOrderInvoice;
import com.tp.service.BaseService;
import com.tp.service.stg.IOutputOrderInvoiceService;

@Service
public class OutputOrderInvoiceService extends BaseService<OutputOrderInvoice> implements IOutputOrderInvoiceService {

	@Autowired
	private OutputOrderInvoiceDao outputOrderInvoiceDao;
	
	@Override
	public BaseDao<OutputOrderInvoice> getDao() {
		return outputOrderInvoiceDao;
	}
}
