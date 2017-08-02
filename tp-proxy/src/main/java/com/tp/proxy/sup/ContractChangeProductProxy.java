package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.ContractChangeProduct;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.IContractChangeProductService;
/**
 * 合同变更-商品表代理层
 * @author szy
 *
 */
@Service
public class ContractChangeProductProxy extends BaseProxy<ContractChangeProduct>{

	@Autowired
	private IContractChangeProductService contractChangeProductService;

	@Override
	public IBaseService<ContractChangeProduct> getService() {
		return contractChangeProductService;
	}
}
