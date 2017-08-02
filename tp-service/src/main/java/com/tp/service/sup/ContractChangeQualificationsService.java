package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.ContractChangeQualificationsDao;
import com.tp.model.sup.ContractChangeQualifications;
import com.tp.service.BaseService;
import com.tp.service.sup.IContractChangeQualificationsService;

@Service
public class ContractChangeQualificationsService extends BaseService<ContractChangeQualifications> implements IContractChangeQualificationsService {

	@Autowired
	private ContractChangeQualificationsDao contractChangeQualificationsDao;
	
	@Override
	public BaseDao<ContractChangeQualifications> getDao() {
		return contractChangeQualificationsDao;
	}

}
