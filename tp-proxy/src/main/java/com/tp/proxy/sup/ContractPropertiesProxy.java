package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.ContractProperties;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.IContractPropertiesService;
/**
 * 合同关联供应商的附件属性表代理层
 * @author szy
 *
 */
@Service
public class ContractPropertiesProxy extends BaseProxy<ContractProperties>{

	@Autowired
	private IContractPropertiesService contractPropertiesService;

	@Override
	public IBaseService<ContractProperties> getService() {
		return contractPropertiesService;
	}
}
