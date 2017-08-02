package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.ContractChangeQualifications;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.IContractChangeQualificationsService;
/**
 * 合同变更-商品资质证明信息表代理层
 * @author szy
 *
 */
@Service
public class ContractChangeQualificationsProxy extends BaseProxy<ContractChangeQualifications>{

	@Autowired
	private IContractChangeQualificationsService contractChangeQualificationsService;

	@Override
	public IBaseService<ContractChangeQualifications> getService() {
		return contractChangeQualificationsService;
	}
}
