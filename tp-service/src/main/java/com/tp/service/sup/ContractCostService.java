package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.ContractCostDao;
import com.tp.model.sup.ContractCost;
import com.tp.service.BaseService;
import com.tp.service.sup.IContractCostService;

@Service
public class ContractCostService extends BaseService<ContractCost> implements IContractCostService {

	@Autowired
	private ContractCostDao contractCostDao;
	
	@Override
	public BaseDao<ContractCost> getDao() {
		return contractCostDao;
	}

}
