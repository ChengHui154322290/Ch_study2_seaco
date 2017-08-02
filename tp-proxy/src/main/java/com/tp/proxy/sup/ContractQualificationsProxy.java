package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.ContractQualifications;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.IContractQualificationsService;
/**
 * 合同-产品-资质证明信息表代理层
 * @author szy
 *
 */
@Service
public class ContractQualificationsProxy extends BaseProxy<ContractQualifications>{

	@Autowired
	private IContractQualificationsService contractQualificationsService;

	@Override
	public IBaseService<ContractQualifications> getService() {
		return contractQualificationsService;
	}
}
