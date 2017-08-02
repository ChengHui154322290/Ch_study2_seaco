package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.PolicyChangeDao;
import com.tp.model.mmp.PolicyChange;
import com.tp.service.BaseService;
import com.tp.service.mmp.IPolicyChangeService;

@Service
public class PolicyChangeService extends BaseService<PolicyChange> implements IPolicyChangeService {

	@Autowired
	private PolicyChangeDao policyChangeDao;
	
	@Override
	public BaseDao<PolicyChange> getDao() {
		return policyChangeDao;
	}


}
