package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.ContractCost;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.IContractCostService;
/**
 * 合同-费用明细表代理层
 * @author szy
 *
 */
@Service
public class ContractCostProxy extends BaseProxy<ContractCost>{

	@Autowired
	private IContractCostService contractCostService;

	@Override
	public IBaseService<ContractCost> getService() {
		return contractCostService;
	}
}
