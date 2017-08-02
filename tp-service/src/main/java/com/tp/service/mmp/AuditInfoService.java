package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.AuditInfoDao;
import com.tp.model.mmp.AuditInfo;
import com.tp.service.BaseService;
import com.tp.service.mmp.IAuditInfoService;

@Service
public class AuditInfoService extends BaseService<AuditInfo> implements IAuditInfoService {

	@Autowired
	private AuditInfoDao auditInfoDao;
	
	@Override
	public BaseDao<AuditInfo> getDao() {
		return auditInfoDao;
	}

}
