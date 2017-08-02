package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.ContractCostLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.IContractCostLogService;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class ContractCostLogProxy extends BaseProxy<ContractCostLog>{

	@Autowired
	private IContractCostLogService contractCostLogService;

	@Override
	public IBaseService<ContractCostLog> getService() {
		return contractCostLogService;
	}
}
