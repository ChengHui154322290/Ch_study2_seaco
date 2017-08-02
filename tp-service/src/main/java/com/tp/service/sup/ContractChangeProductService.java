package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.ContractChangeProductDao;
import com.tp.model.sup.ContractChangeProduct;
import com.tp.service.BaseService;
import com.tp.service.sup.IContractChangeProductService;

@Service
public class ContractChangeProductService extends BaseService<ContractChangeProduct> implements IContractChangeProductService {

	@Autowired
	private ContractChangeProductDao contractChangeProductDao;
	
	@Override
	public BaseDao<ContractChangeProduct> getDao() {
		return contractChangeProductDao;
	}

}
