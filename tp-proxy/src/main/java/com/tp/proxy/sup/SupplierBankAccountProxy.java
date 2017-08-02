package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.SupplierBankAccount;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.ISupplierBankAccountService;
/**
 * 供应商-银行帐号表代理层
 * @author szy
 *
 */
@Service
public class SupplierBankAccountProxy extends BaseProxy<SupplierBankAccount>{

	@Autowired
	private ISupplierBankAccountService supplierBankaccountService;

	@Override
	public IBaseService<SupplierBankAccount> getService() {
		return supplierBankaccountService;
	}
}
