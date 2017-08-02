package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.ContractProduct;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.IContractProductService;
/**
 * 合同-商品表代理层
 * @author szy
 *
 */
@Service
public class ContractProductProxy extends BaseProxy<ContractProduct>{

	@Autowired
	private IContractProductService contractProductService;

	@Override
	public IBaseService<ContractProduct> getService() {
		return contractProductService;
	}
}
