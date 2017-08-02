package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.ContractPropertiesDao;
import com.tp.model.sup.ContractProperties;
import com.tp.service.BaseService;
import com.tp.service.sup.IContractPropertiesService;

@Service
public class ContractPropertiesService extends BaseService<ContractProperties> implements IContractPropertiesService {

	@Autowired
	private ContractPropertiesDao contractPropertiesDao;
	
	@Override
	public BaseDao<ContractProperties> getDao() {
		return contractPropertiesDao;
	}

}
