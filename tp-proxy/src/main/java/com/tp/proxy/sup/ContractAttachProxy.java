package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.ContractAttach;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.IContractAttachService;
/**
 * 合同-附件表代理层
 * @author szy
 *
 */
@Service
public class ContractAttachProxy extends BaseProxy<ContractAttach>{

	@Autowired
	private IContractAttachService contractAttachService;

	@Override
	public IBaseService<ContractAttach> getService() {
		return contractAttachService;
	}
}
