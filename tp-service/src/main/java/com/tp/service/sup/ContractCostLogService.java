package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.ContractCostLogDao;
import com.tp.model.sup.ContractCostLog;
import com.tp.service.BaseService;
import com.tp.service.sup.IContractCostLogService;

@Service
public class ContractCostLogService extends BaseService<ContractCostLog> implements IContractCostLogService {

	@Autowired
	private ContractCostLogDao contractCostLogDao;
	
	@Override
	public BaseDao<ContractCostLog> getDao() {
		return contractCostLogDao;
	}

}
