package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.ContractChange;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.IContractChangeService;
/**
 * 合同变更主表代理层
 * @author szy
 *
 */
@Service
public class ContractChangeProxy extends BaseProxy<ContractChange>{

	@Autowired
	private IContractChangeService contractChangeService;

	@Override
	public IBaseService<ContractChange> getService() {
		return contractChangeService;
	}
}
