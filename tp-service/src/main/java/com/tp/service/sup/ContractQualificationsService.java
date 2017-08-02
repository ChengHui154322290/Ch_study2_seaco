package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.ContractQualificationsDao;
import com.tp.model.sup.ContractQualifications;
import com.tp.service.BaseService;
import com.tp.service.sup.IContractQualificationsService;

@Service
public class ContractQualificationsService extends BaseService<ContractQualifications> implements IContractQualificationsService {

	@Autowired
	private ContractQualificationsDao contractQualificationsDao;
	
	@Override
	public BaseDao<ContractQualifications> getDao() {
		return contractQualificationsDao;
	}

}
