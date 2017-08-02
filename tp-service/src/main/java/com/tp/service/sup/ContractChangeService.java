package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.ContractChangeDao;
import com.tp.model.sup.ContractChange;
import com.tp.service.BaseService;
import com.tp.service.sup.IContractChangeService;

@Service
public class ContractChangeService extends BaseService<ContractChange> implements IContractChangeService {

	@Autowired
	private ContractChangeDao contractChangeDao;
	
	@Override
	public BaseDao<ContractChange> getDao() {
		return contractChangeDao;
	}

}
