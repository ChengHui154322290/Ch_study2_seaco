package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.ContractOffice;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.IContractOfficeService;
/**
 * 合同-国内联系方表代理层
 * @author szy
 *
 */
@Service
public class ContractOfficeProxy extends BaseProxy<ContractOffice>{

	@Autowired
	private IContractOfficeService contractOfficeService;

	@Override
	public IBaseService<ContractOffice> getService() {
		return contractOfficeService;
	}
}
