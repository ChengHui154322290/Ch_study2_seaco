package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.ContractSettlementRule;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.IContractSettlementRuleService;
/**
 * 合同-结算规则表代理层
 * @author szy
 *
 */
@Service
public class ContractSettlementRuleProxy extends BaseProxy<ContractSettlementRule>{

	@Autowired
	private IContractSettlementRuleService contractSettlementRuleService;

	@Override
	public IBaseService<ContractSettlementRule> getService() {
		return contractSettlementRuleService;
	}
}
