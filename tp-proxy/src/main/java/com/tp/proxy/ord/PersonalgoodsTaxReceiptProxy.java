package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.PersonalgoodsTaxReceipt;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.IPersonalgoodsTaxReceiptService;
/**
 * 个人物品申报税费回执代理层
 * @author szy
 *
 */
@Service
public class PersonalgoodsTaxReceiptProxy extends BaseProxy<PersonalgoodsTaxReceipt>{

	@Autowired
	private IPersonalgoodsTaxReceiptService personalgoodsTaxReceiptService;

	@Override
	public IBaseService<PersonalgoodsTaxReceipt> getService() {
		return personalgoodsTaxReceiptService;
	}
}
