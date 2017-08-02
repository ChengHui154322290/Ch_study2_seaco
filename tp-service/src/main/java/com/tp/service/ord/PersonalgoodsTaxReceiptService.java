package com.tp.service.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ord.PersonalgoodsTaxReceiptDao;
import com.tp.model.ord.PersonalgoodsTaxReceipt;
import com.tp.service.BaseService;
import com.tp.service.ord.IPersonalgoodsTaxReceiptService;

@Service
public class PersonalgoodsTaxReceiptService extends BaseService<PersonalgoodsTaxReceipt> implements IPersonalgoodsTaxReceiptService {

	@Autowired
	private PersonalgoodsTaxReceiptDao personalgoodsTaxReceiptDao;
	
	@Override
	public BaseDao<PersonalgoodsTaxReceipt> getDao() {
		return personalgoodsTaxReceiptDao;
	}

}
