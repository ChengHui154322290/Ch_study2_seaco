package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.ContractSettlementRuleDao;
import com.tp.model.sup.ContractSettlementRule;
import com.tp.service.BaseService;
import com.tp.service.sup.IContractSettlementRuleService;

@Service
public class ContractSettlementRuleService extends BaseService<ContractSettlementRule> implements IContractSettlementRuleService {

	@Autowired
	private ContractSettlementRuleDao contractSettlementRuleDao;
	
	@Override
	public BaseDao<ContractSettlementRule> getDao() {
		return contractSettlementRuleDao;
	}

}
