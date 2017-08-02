package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.SupplierBankAccountDao;
import com.tp.model.sup.SupplierBankAccount;
import com.tp.service.BaseService;
import com.tp.service.sup.ISupplierBankAccountService;

@Service
public class SupplierBankAccountService extends BaseService<SupplierBankAccount> implements ISupplierBankAccountService {

	@Autowired
	private SupplierBankAccountDao supplierBankaccountDao;
	
	@Override
	public BaseDao<SupplierBankAccount> getDao() {
		return supplierBankaccountDao;
	}

}
