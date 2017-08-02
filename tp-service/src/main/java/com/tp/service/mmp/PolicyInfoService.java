package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.PolicyInfoDao;
import com.tp.model.mmp.PolicyInfo;
import com.tp.service.BaseService;
import com.tp.service.mmp.IPolicyInfoService;

@Service
public class PolicyInfoService extends BaseService<PolicyInfo> implements IPolicyInfoService {

	@Autowired
	private PolicyInfoDao policyInfoDao;
	
	@Override
	public BaseDao<PolicyInfo> getDao() {
		return policyInfoDao;
	}

}
