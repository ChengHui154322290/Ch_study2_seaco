package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.ContractProductDao;
import com.tp.model.sup.ContractProduct;
import com.tp.service.BaseService;
import com.tp.service.sup.IContractProductService;

@Service
public class ContractProductService extends BaseService<ContractProduct> implements IContractProductService {

	@Autowired
	private ContractProductDao contractProductDao;
	
	@Override
	public BaseDao<ContractProduct> getDao() {
		return contractProductDao;
	}

}
