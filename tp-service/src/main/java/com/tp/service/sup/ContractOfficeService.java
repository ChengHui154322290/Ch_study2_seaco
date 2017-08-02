package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.ContractOfficeDao;
import com.tp.model.sup.ContractOffice;
import com.tp.service.BaseService;
import com.tp.service.sup.IContractOfficeService;

@Service
public class ContractOfficeService extends BaseService<ContractOffice> implements IContractOfficeService {

	@Autowired
	private ContractOfficeDao contractOfficeDao;
	
	@Override
	public BaseDao<ContractOffice> getDao() {
		return contractOfficeDao;
	}

}
